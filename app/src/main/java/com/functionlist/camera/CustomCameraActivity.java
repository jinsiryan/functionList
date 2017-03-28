package com.functionlist.camera;

import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.functionlist.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CustomCameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private Camera camera;
    private SurfaceView preview;
    private SurfaceHolder holder;
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File tempFile = new File("/sdcard/temp.png");
            try {
                FileOutputStream fos = new FileOutputStream(tempFile);
                fos.write(data);
                Intent intent = new Intent(CustomCameraActivity.this,ResultActivity.class);
                intent.putExtra("picPath",tempFile.getAbsolutePath());
                startActivity(intent);
                finish();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        preview = (SurfaceView) findViewById(R.id.preview);
        holder = preview.getHolder();
        holder.addCallback(this);
        preview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                camera.autoFocus(null);
            }
        });
    }
    public void capture(View veiw){
        final Camera.Parameters parameters = camera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setPreviewSize(800,400);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(success){
                    camera.takePicture(null,null,pictureCallback);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(camera == null){
            camera = getCamera();
            if(holder != null){
                setStartPreview(camera,holder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private Camera getCamera(){
        Camera camera;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            camera = null;
            e.printStackTrace();
        }
        return camera;

    }

    /**
     * 开始预览相机内容
     */
    private void setStartPreview(Camera camera,SurfaceHolder holder){
        try {
            camera.setPreviewDisplay(holder);
            //将系统Camera预览
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void releaseCamera(){
        if(camera != null){
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
          setStartPreview(camera,holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        camera.stopPreview();
        setStartPreview(camera,holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
         releaseCamera();
    }
}
