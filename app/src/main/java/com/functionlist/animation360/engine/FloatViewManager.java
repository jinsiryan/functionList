package com.functionlist.animation360.engine;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.functionlist.animation360.view.FloatCircleView;

/**
 * Created by imo on 2016/10/7.
 */

public class FloatViewManager {
    private final WindowManager wm;//通过这个windowmanager来操作窗体的显示和隐藏以及位置的改变
    private Context context;
    private FloatCircleView mFloatRiskBtn;


    private boolean isRiskMove;
    private int mRiskLastX;
    private int mRiskLastY;
    private static FloatViewManager instance;
    private FloatViewManager(Context context){
        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mFloatRiskBtn = new FloatCircleView(context);
        initFloatViewListener();

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
        params.width = mFloatRiskBtn.width;
        params.height = mFloatRiskBtn.height;
        params.gravity = Gravity.TOP|Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT ;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.format = PixelFormat.RGBA_8888;
        wm.addView(mFloatRiskBtn,params);
    }

    private void initFloatViewListener() {
        mFloatRiskBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int x = (int) motionEvent.getRawX();
                int y = (int) motionEvent.getRawY();
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        //计算距离上次移动了多远
                        int deltaX = x - mRiskLastX;
                        int deltaY = y - mRiskLastY;
//                        if(deltaX > ScreenUtil.dip2px(10)
//                                || deltaY > ScreenUtil.dip2px(10)){
//                            isRiskMove = true;
//                        }
                        int translationX = (int) (mFloatRiskBtn.getTranslationX() + deltaX);
                        int translationY = (int) (mFloatRiskBtn.getTranslationY() + deltaY);
                        //使mFloatRiskBtn根据手指滑动平移
                        mFloatRiskBtn.setTranslationX(translationX);
                        mFloatRiskBtn.setTranslationY(translationY);
                        break;
                    case MotionEvent.ACTION_UP:
//                        //平移回到该view水平方向的初始点
//                        mFloatRiskBtn.setTranslationX(0);
                        //判断什么情况下需要回到原点
//                        if(mFloatRiskBtn.getY()<0) {
//                            mFloatRiskBtn.setTranslationY(0);
//                        }else if(mFloatRiskBtn.getY()>(options.bounds.bottom -mFloatRiskBtn.getMeasuredHeight())){
//                            mFloatRiskBtn.setTranslationY(options.bounds.bottom -mFloatRiskBtn.getMeasuredHeight());
//                        }
//                        if(mFloatRiskBtn.getX() < 0){
//                            mFloatRiskBtn.setTranslationX(options.y);
//                        }else if(mFloatRiskBtn.getX() > options.bounds.right - mFloatRiskBtn.getMeasuredWidth()){
//                            mFloatRiskBtn.setTranslationX(options.bounds.right - mFloatRiskBtn.getMeasuredWidth());
//                        }
                        break;
                    default:
                        break;
                }
                //记录上次手指离开时的位置
                mRiskLastX = x;
                mRiskLastY = y;
                return false;
            }
        });

    }
}
