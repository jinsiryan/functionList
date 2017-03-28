package com.functionlist.droidserver;

import java.io.IOException;

/**
 * Created by imo on 2016/9/29.
 */

public interface IResourceUriHandler {
    boolean accept(String uri);
    void handle(String uri,HttpContext httpContext) throws IOException;
}
