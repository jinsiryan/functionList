package com.functionlist;

import android.app.Application;
import android.support.multidex.MultiDex;

/**
 * Created by yan on 2018/1/15.
 */

public class FunctionApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
