package com.functionlist.droidserver;

import java.net.Socket;
import java.util.HashMap;

/**
 * Created by imo on 2016/9/29.
 */

public class HttpContext {
    private HashMap<String,String> requestHeaders;
    public HttpContext(){
        requestHeaders = new HashMap<>();
    }
    private Socket underlySocket;

    public Socket getUnderlySocket() {
        return underlySocket;
    }

    public void setUnderlySocket(Socket underlySocket) {
        this.underlySocket = underlySocket;
    }
    public void addRequestHeader(String headerName,String headerValue){
        requestHeaders.put(headerName,headerValue);
    }
    public String getRequestHeaderValue(String headerName){
        return requestHeaders.get(headerName);
    }
}
