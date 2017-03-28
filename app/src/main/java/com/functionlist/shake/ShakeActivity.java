package com.functionlist.shake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.functionlist.R;

public class ShakeActivity extends AppCompatActivity implements ShakeSensor.OnShakeListenr {

    private ShakeSensor shakeSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        shakeSensor = new ShakeSensor(this);
        shakeSensor.setOnShakeListenr(this);
        shakeSensor.init();
    }

    @Override
    public void onShake() {
      //实现操作
       Toast t =  Toast.makeText(this,"摇一摇成功",Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER,0,0);
        t.show();
    }

    @Override
    protected void onDestroy() {
        shakeSensor.unRegisterListener();
        super.onDestroy();
    }
}
