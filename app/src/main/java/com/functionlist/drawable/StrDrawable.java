package com.functionlist.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

/**
 * Created by yan on 2017/3/31.
 */

public class StrDrawable extends Drawable {
    private int colorbg;
    private String str;
    private TextPaint textPaint;
    private int mRound = 0;
    private Paint mPaint;
    public StrDrawable(int colorbg, String str){
        this.colorbg = colorbg;
        this.str = str;
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(colorbg);
    }
    public void setStr(String str){
        this.str = str;
    }

    public void setRound(int mRound) {
        this.mRound = mRound;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if(str == null){
            str= "";
        }
        Rect rect = getBounds();
        RectF rectF = new RectF(rect);
        canvas.drawRoundRect(rectF,mRound,mRound,mPaint);
        String text = str;
        int x = rect.width();
        int y = rect.height();
        textPaint.setTextSize((x - 10) / (text.length() == 0 ? 1 : text.length()) * (float)0.9);
        StaticLayout sl = new StaticLayout(text, textPaint, x - 8, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        float tX = (x - getFontlength(textPaint, text)) / 2;
        float tY = (y - getFontHeight(textPaint)) / 2;
        canvas.translate(tX, tY);
        sl.draw(canvas);



    }
    public  float getFontlength(Paint paint, String str) {
        return paint.measureText(str);
    }
    /**
     * @return 返回指定笔的文字高度
     */
    public float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }
    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
