package com.functionlist.WeChatImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by yan on 2017/4/6.
 */

public class ClipImageView extends ImageView {


    public ClipImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    private Paint paint;
    private int clipbg;
    private int modifiColor;

    public void setClipbg(int clipbg) {
        this.clipbg = clipbg;
    }

    public void setModifiColor(int modifiColor) {
        this.modifiColor = modifiColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(paint == null){
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        }
        super.onDraw(canvas);
        Bitmap bitmap_bg = getBitmap();
        Rect rect = new Rect(0,0,getWidth(),getHeight());
        bitmap_bg.getNinePatchChunk();
        NinePatch patch = new NinePatch(bitmap_bg, bitmap_bg.getNinePatchChunk(), null);
        patch.draw(canvas,rect,paint);
    }
    public Bitmap getBitmap(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        int bg = modifiColor;
        options.inMutable = true;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), clipbg,options);
        int mBitmapWidth = bitmap.getWidth();
        int mBitmapHeight = bitmap.getHeight();
        for (int i = 0; i < mBitmapHeight; i++) {
            for (int j = 0; j < mBitmapWidth; j++) {
                //获得Bitmap 图片中每一个点的color颜色值
                int color = bitmap.getPixel(j, i);
                //将颜色值存在一个数组中 方便后面修改
                //如果你想做的更细致的话 可以把颜色值的R G B 拿到做响应的处理 笔者在这里就不做更多解释
                if (!checkNinePath(mBitmapWidth, mBitmapHeight, j, i)) {
                    continue;
                }
                if (color == Color.TRANSPARENT) {
                    bitmap.setPixel(j, i, bg);
                } else {
                    bitmap.setPixel(j, i, Color.TRANSPARENT);
                }
            }
        }
        return  bitmap;
    }
        public boolean checkNinePath(int mBitmapWidth,int mBitmapHeight,int w,int h){
//            if(w <= 1){
//                return false;
//            }
//            if(h <=1){
//                return false;
//            }
//            if(mBitmapWidth - w <= 1){
//                return false;
//            }
//            if(mBitmapHeight - h <= 1){
//                return false;
//            }
            return true;
        }
}
