package com.functionlist.animation360.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by imo on 2016/10/7.
 */

public class FloatCircleView extends View{
    public int width = 150;
    public int height = 150;
    private Paint circlePaint;
    private Paint textPaint;
    private String text = "50%";

    public FloatCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initPaints() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setTextSize(25);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
    }

    public FloatCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public FloatCircleView(Context context) {
        super(context);
        initPaints();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
       canvas.drawCircle(width/2,height/2,width/2,circlePaint);
        float textWidth = textPaint.measureText(text);
        float x= width /2 - textWidth /2;
        Paint.FontMetrics metrices = textPaint.getFontMetrics();
        float dy = (metrices.descent + metrices.ascent) /2;
        float y = height / 2 + dy;
        canvas.drawText(text,x,y,textPaint);
    }
}
