package com.functionlist.droidserver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Created by imo on 2016/9/29.
 */

public class UploadImageHandler implements IResourceUriHandler{
    private String acceptPrefix = "/upload_image/";
    @Override
    public boolean accept(String uri) {
        return uri.startsWith(acceptPrefix);
    }

    @Override
    public void handle(String uri, HttpContext httpContext) throws IOException {
        String tmpPath = "/mnt/sdcard/test_upload.jpg";
        long totalLength = Long.parseLong(httpContext.getRequestHeaderValue("Contnet-Length"));
        FileOutputStream fos = new FileOutputStream(tmpPath);
        InputStream nis = httpContext.getUnderlySocket().getInputStream();
        byte[] buffer = new byte[1024];
        int nReaded = 0;
        long nLeftLength = totalLength;
        while ((nReaded = nis.read(buffer)) > 0 && nLeftLength > 0){
            fos.write(buffer,0,nReaded);
            nLeftLength -= nReaded;
        }
        fos.close();
        OutputStream nos = httpContext.getUnderlySocket().getOutputStream();
        PrintStream printStream = new PrintStream(nos);
        printStream.println("HTTP/1.1 200 OK");
        printStream.println();
        onImageLoaded(tmpPath);
    }

    protected void onImageLoaded(String tmpPath) {

    }
}
