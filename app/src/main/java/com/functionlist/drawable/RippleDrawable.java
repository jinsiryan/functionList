package com.functionlist.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by yan on 2018/1/12.
 *
 * 1.首先确定点击的位置，设置为启动时的圆心
 * 2.圆心是移动的
 * 3.圆
 *
 * 退出动画
 * 实现背景减淡的过程
 *
 *
 */

public class RippleDrawable extends Drawable {


    private int mAlpha = 200;

    private int mRippleColor = 0;
    //画笔
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 圆心坐标
     */
    private float mRipplePointX,mRipplePointY;

    //半径
    private float mRippleRadius = 0;




    public RippleDrawable(){
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //防抖动
        mPaint.setDither(true);
        setRippleColor(0x30000000);

//        setColorFilter(new LightingColorFilter(0xFFFF0000,0x00330000));

    }

    public void setRippleColor(int color){
        mRippleColor = color;
        onColorOrAlphaChange();
    }
    private boolean mEnterDone;
    //进入时的动画进度值
    private float mEnterProgress = 0;
    private float mEnterIncrement = 16f/360;
    //进入动画差值器，用于实现从快到慢的效果
    private Interpolator mEnterInterpolator = new DecelerateInterpolator(2);
    private Runnable mEnterRunnable = new Runnable() {
        @Override
        public void run() {
            mEnterProgress += mEnterIncrement;
            if(mEnterProgress > 1){
                onEnterProgress(1);
                onEnterDone();
                return;
            }
            float realProgress = mEnterInterpolator.getInterpolation(mEnterProgress);
            onEnterProgress(realProgress);
            //延迟16毫秒，保证界面
            scheduleSelf(this, SystemClock.uptimeMillis() + 16);
        }
    };
    private void onEnterDone(){
        mEnterDone = true;
        if(mTouchRelease){
            startExitRunnable();
        }
    }
    private void onEnterProgress(float progress){
        mRippleRadius = getProgressValue(mStartRadius,mEndRadius,progress);
        mRipplePointX  = getProgressValue(mDonePointX,mCenterPointX,progress);
        mRipplePointY = getProgressValue(mDonePointY,mCenterPointY,progress);

        mBgAlpha= (int) getProgressValue(0,182,progress);

        invalidateSelf();
    }


    //退出时的动画进度值
    private float mExitProgress = 0;
    private float mExitIncrement = 16f/300;
    //退出动画差值器，用于实现从慢到快的效果
    private Interpolator mExitInterpolator = new AccelerateInterpolator(2);
    private Runnable mExitRunnable = new Runnable() {
        @Override
        public void run() {
            if(!mEnterDone){
                return;
            }
            mExitProgress += mExitIncrement;
            if(mExitProgress > 1){
                onExitProgress(1);
                onExitDone();
                return;
            }
            float realProgress = mExitInterpolator.getInterpolation(mExitProgress);
            onExitProgress(realProgress);
            //延迟16毫秒，保证界面
            scheduleSelf(this, SystemClock.uptimeMillis() + 16);
        }
    };
    private void onExitDone(){

    }
    private void onExitProgress(float progress){
        //背景减淡
        mBgAlpha = (int) getProgressValue(182,0,progress);
        //圆形减淡
        mCircleAlpha = (int) getProgressValue(255,0,progress);
        invalidateSelf();
    }
    private float getProgressValue(float start,float end,float progress){
        return start + (end - start) * progress;
    }
    //按下时点击的点
    private float mDonePointX,mDonePointY;
    //控件的中心区域点
    private float mCenterPointX,mCenterPointY;
    //开始和结束的半径
    private float mStartRadius,mEndRadius;

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mCenterPointX = bounds.centerX();
        mCenterPointY = bounds.centerY();
        //得到圆的最大半径
        float maxRadius = Math.max(mCenterPointX,mCenterPointY);
        mStartRadius = maxRadius * 0f;
        mEndRadius = maxRadius * 0.8f;
    }
    private int mBgAlpha;
    private int mCircleAlpha;

    private int getCircleAlpha(int preAlpha,int bgAlpha){
        int dAlpha = preAlpha - bgAlpha;
        return (int) ((dAlpha * 255f)/(255f - bgAlpha));
    }
    @Override
    public void draw(@NonNull Canvas canvas) {
        int preAlpha = mPaint.getAlpha();
        int bgAlpha = (int) (preAlpha * (mBgAlpha * (mBgAlpha/255f)));
        int maxCircleAlpha = getCircleAlpha(preAlpha,bgAlpha);
        int circleAlpha = (int) (maxCircleAlpha * (mCircleAlpha / 255f));

        mPaint.setAlpha(bgAlpha);
        canvas.drawColor(mPaint.getColor());

        mPaint.setAlpha(circleAlpha);
        canvas.drawCircle(mRipplePointX,mRipplePointY,mRippleRadius,mPaint);

        mPaint.setAlpha(preAlpha);
    }

    /**
     *
     * @param alpha
     */
    @Override
    public void setAlpha(int alpha) {
        mAlpha = alpha;
        onColorOrAlphaChange();
    }
    private void onColorOrAlphaChange(){
        mPaint.setColor(mRippleColor);
        if(mAlpha != 255){
            //颜色透明度
            int pAlpha = mPaint.getAlpha();

            int realAlpha = (int) (pAlpha * (mAlpha / 255f));

            mPaint.setAlpha(realAlpha);
        }
        invalidateSelf();

    }

    @Override
    public int getAlpha() {
        return mAlpha;
    }
    private void startEnterRunnable(){
        mEnterProgress = 0;
        mEnterDone = false;
        mCircleAlpha = 255;
        unscheduleSelf(mEnterRunnable);
        unscheduleSelf(mExitRunnable);
        scheduleSelf(mEnterRunnable, SystemClock.uptimeMillis());
    }
    private void startExitRunnable(){
        mExitProgress = 0;
        unscheduleSelf(mEnterRunnable);
        unscheduleSelf(mExitRunnable);
        scheduleSelf(mExitRunnable, SystemClock.uptimeMillis());
    }
    /**
     * 颜色滤镜
     * @param colorFilter
     */
    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
       if(mPaint.getColorFilter() != colorFilter){
           mPaint.setColorFilter(colorFilter);
           invalidateSelf();
       }
    }
    //标示用户手是否抬起
    private boolean mTouchRelease;
    public void onTouchEvent(MotionEvent event){
        //判断点击操作类型
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                onTouchDown(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_UP:
                onTouchUp(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_CANCEL:
                onTouchCancel(event.getX(),event.getY());
                break;


        }
    }
    private void onTouchDown(float x, float y){
        mDonePointX = x;
        mDonePointY = y;
        mTouchRelease = false;
        startEnterRunnable();
    }
    private void onTouchUp(float x, float y){
        mTouchRelease = true;
        if(mEnterDone){
            startExitRunnable();
        }
    }
    private void onTouchCancel(float x, float y){
        mTouchRelease = true;
        if(mEnterDone){
            startExitRunnable();
        }
    }
    private void onTouchMove(float x, float y){

    }


    @Override
    public int getOpacity() {
        int alpha = mPaint.getAlpha();
        if(alpha == 255){
            //不透明
            return PixelFormat.OPAQUE;
        }else if(alpha == 0){
            //全透明
            return PixelFormat.TRANSPARENT;
        }else {
            //半透明
            return PixelFormat.TRANSLUCENT;
        }
    }
}
