package com.functionlist.droidserver;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Created by yan on 2016/9/29.
 */

public class ResourceInAssetsHandler implements IResourceUriHandler{
    private String acceptPrefix = "/static/";
    private Context context;
    public ResourceInAssetsHandler(Context context){
        this.context = context;
    }
    @Override
    public boolean accept(String uri) {
        return uri.startsWith(acceptPrefix);
    }

    @Override
    public void handle(String uri, HttpContext httpContext) throws IOException {
        int startIndex = acceptPrefix.length();
        String assetsPath = uri.substring(startIndex);
        InputStream fis = context.getAssets().open(assetsPath);
        byte[] raw = StreamToolKit.readRawFromStream(fis);
        fis.close();
        OutputStream nos = httpContext.getUnderlySocket().getOutputStream();
        PrintStream writer = new PrintStream(nos);
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Length:" +raw.length);
        if(assetsPath.endsWith(".html")){
            writer.println("Content-Type:text/html");
        } else if(assetsPath.endsWith(".js")){
            writer.println("Content-Type:text/js");
        } else if(assetsPath.endsWith(".css")){
            writer.println("Content-Type:text/css");
        } else if(assetsPath.endsWith(".jpg")){
            writer.println("Content-Type:text/jpg");
        } else if(assetsPath.endsWith(".png")){
            writer.println("Content-Type:text/png");
        }
        writer.println();
        writer.write(raw);
    }
}
