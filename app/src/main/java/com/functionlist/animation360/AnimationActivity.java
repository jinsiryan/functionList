package com.functionlist.animation360;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.functionlist.R;
import com.functionlist.animation360.engine.FloatViewManager;
import com.functionlist.animation360.service.MyService;

public class AnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
    }
    public void startService(View view){
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        finish();
//        FloatViewManager manager = FloatViewManager.getInstance(this);
//        manager.showFloatCircleView();
    }
}
