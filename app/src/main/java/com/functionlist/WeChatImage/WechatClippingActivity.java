package com.functionlist.WeChatImage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.functionlist.R;

public class WechatClippingActivity extends AppCompatActivity {


    private ImageView iv_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_clipping);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        showImage();

    }
    private void showImage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap_bg = BitmapFactory.decodeResource(getResources(), R.drawable.chat_adapter_to_bg);
                Bitmap bitmap_in = BitmapFactory.decodeResource(getResources(), R.drawable.aa);
                final Bitmap bp = getRoundCornerImage(bitmap_bg, bitmap_in);
                final Bitmap bp2 = getShardImage(bitmap_bg, bp);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_image.setImageBitmap(bp2);
                    }
                });
            }
        }).start();
    }

    public static Bitmap getRoundCornerImage(Bitmap bitmap_bg,Bitmap bitmap_in)
    {
        Bitmap roundConcerImage = Bitmap.createBitmap(500,500, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundConcerImage);
        Paint paint = new Paint();
        Rect rect = new Rect(0,0,500,500);
        Rect rectF = new Rect(0, 0, bitmap_in.getWidth(), bitmap_in.getHeight());
        paint.setAntiAlias(true);
        NinePatch patch = new NinePatch(bitmap_bg, bitmap_bg.getNinePatchChunk(), null);
        patch.draw(canvas, rect);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap_in, rectF, rect, paint);
        return roundConcerImage;
    }
    public static Bitmap getShardImage(Bitmap bitmap_bg,Bitmap bitmap_in)
    {
        Bitmap roundConcerImage = Bitmap.createBitmap(500,500, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundConcerImage);
        Paint paint = new Paint();
        Rect rect = new Rect(0,0,500,500);
        paint.setAntiAlias(true);
        NinePatch patch = new NinePatch(bitmap_bg, bitmap_bg.getNinePatchChunk(), null);
        patch.draw(canvas, rect);
        Rect rect2 = new Rect(2,2,498,498);
        canvas.drawBitmap(bitmap_in, rect, rect2, paint);
        return roundConcerImage;
    }

}
