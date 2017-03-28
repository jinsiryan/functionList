package com.functionlist.droidserver;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by imo on 2016/9/29.
 */

public class SimpleHttpServer {
    private final WebConfiguration webConfig;
    private boolean isEnable;
    private ServerSocket socket;
    private final ExecutorService threadPool;
    private Set<IResourceUriHandler> resourceHandlers;

    public SimpleHttpServer(WebConfiguration config){
        this.webConfig = config;
        threadPool = Executors.newCachedThreadPool();
        resourceHandlers = new HashSet<>();
    }

    /**
     * 启动server
     */
    public void sartAsync(){
        isEnable = true;
        new Thread(){
            @Override
            public void run() {
               doProcSync();
            }
        }.start();
        
    }
    public void stopAsync() throws IOException{
        if(!isEnable){
            return;
        }
        isEnable = false;
        socket.close();
        socket = null;
    }


    private void doProcSync() {
        InetSocketAddress socketAddr = new InetSocketAddress(webConfig.getPort());
        try {
            socket = new ServerSocket();
            socket.bind(socketAddr);
            Log.d("spy","a bind...socketAddr" + socket.getLocalSocketAddress());
            while (isEnable){
                final Socket remotePeer = socket.accept();
                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("spy","a remote peet accepted..." + remotePeer.getRemoteSocketAddress().toString());
                         onAcceptRemotePeer(remotePeer);
                    }
                });
            }
        } catch (IOException e) {
           Log.e("spy",e.toString());
        }

    }
    public void registerResourceHandler(IResourceUriHandler iResourceUriHandler){
        resourceHandlers.add(iResourceUriHandler);
    }

    private void onAcceptRemotePeer(Socket remotePeer) {
        try {
            HttpContext httpContext = new HttpContext();
            httpContext.setUnderlySocket(remotePeer);
//            remotePeer.getOutputStream().write("congratulations,connected successful".getBytes());
            InputStream nis = remotePeer.getInputStream();
            String headerLine = null;
            String resourceUri = headerLine = StreamToolKit.readLine(nis).split(" ")[1];
            Log.d("spy",resourceUri);
            while ((headerLine = StreamToolKit.readLine(nis)) != null){
                if(headerLine.equals("\r\n")){
                    break;
                }
                Log.d("spy","header line = " + headerLine);
                String[] pair = headerLine.split(": ");
                httpContext.addRequestHeader(pair[0],pair[1]);
            }
            for (IResourceUriHandler handler: resourceHandlers) {
                if(!handler.accept(resourceUri)){
                    continue;
                }
                handler.handle(resourceUri,httpContext);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
