package com.functionlist.httpUpload.Utils;

import android.util.Log;

/**
 * Created by yan on 2018/1/9.
 */

public class Logger {
    public static final String TAG = "httpUpload";

    public static final boolean DEBUG = true;

    public static String getMessage(Object o){
        return o == null ? "null" : o.toString();
    }
    public static void i(Object msg){
        if(DEBUG)
           Log.i(TAG,getMessage(msg));
    }
    public static void d(Object msg){
        if(DEBUG)
            Log.d(TAG,getMessage(msg));
    }
    public static void w(Object msg){
        if(DEBUG)
            Log.w(TAG,getMessage(msg));
    }
    public static void e(Object msg){
        if(DEBUG)
            Log.e(TAG,getMessage(msg));
    }
}
