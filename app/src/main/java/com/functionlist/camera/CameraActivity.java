package com.functionlist.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.functionlist.R;

import java.io.File;

public class CameraActivity extends AppCompatActivity {
    private static int REQ_1= 1;
    private static int REQ_2= 2;
    private ImageView iv;
    private String mFilePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        iv = (ImageView) findViewById(R.id.iv);
        mFilePath = Environment.getExternalStorageDirectory().getPath();
        mFilePath = mFilePath + "/" + "temp.png";

    }
    public void startCamera1(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQ_1);
    }
    public void startCamera2(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoUri = Uri.fromFile(new File(mFilePath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
        startActivityForResult(intent,REQ_2);
    }
    public void customCamera(View view){
        startActivity(new Intent(this,CustomCameraActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQ_1){
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                iv.setImageBitmap(bitmap);
            }else if(requestCode == REQ_2){
                Bitmap bitmap = BitmapFactory.decodeFile(mFilePath);
                iv.setImageBitmap(bitmap);
            }
        }
    }
}
