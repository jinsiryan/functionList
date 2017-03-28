package com.functionlist.animation360.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.functionlist.animation360.engine.FloatViewManager;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        FloatViewManager manager = FloatViewManager.getInstance(this);
        manager.showFloatCircleView();
        super.onCreate();

    }
}
