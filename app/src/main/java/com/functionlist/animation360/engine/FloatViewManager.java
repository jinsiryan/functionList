package com.functionlist.animation360.engine;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import com.functionlist.animation360.view.FloatCircleView;

/**
 * Created by imo on 2016/10/7.
 */

public class FloatViewManager {
    private final WindowManager wm;//通过这个windowmanager来操作窗体的显示和隐藏以及位置的改变
    private Context context;
    private FloatCircleView circleView;
    private static FloatViewManager instance;
    private FloatViewManager(Context context){
        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        circleView = new FloatCircleView(context);

    }
    public static FloatViewManager getInstance(Context context){
        if(instance == null){
            synchronized (FloatViewManager.class){
              if(instance == null){
                  instance = new FloatViewManager(context);
              }
            }
        }
        return instance;
    }
    public void showFloatCircleView(){
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = circleView.width;
        params.height = circleView.height;
        params.gravity = Gravity.TOP|Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT ;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.format = PixelFormat.RGBA_8888;
        wm.addView(circleView,params);
    }
}
