package com.functionlist.droidserver;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.functionlist.R;

import java.io.IOException;

public class ServerActivity extends AppCompatActivity {

    private SimpleHttpServer shs;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        imageView = (ImageView) findViewById(R.id.imageView);

        WebConfiguration wc = new WebConfiguration();
        wc.setPort(8088);
        wc.setMaxParallels(50);
        shs = new SimpleHttpServer(wc);
        shs.registerResourceHandler(new ResourceInAssetsHandler(this));
        shs.registerResourceHandler(new UploadImageHandler(){
            @Override
            protected void onImageLoaded(String tmpPath) {
                showImage(tmpPath);
            }
        });
        shs.sartAsync();

    }

    private void showImage(final String tmpPath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeFile(tmpPath);
                imageView.setImageBitmap(bitmap);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            shs.stopAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
