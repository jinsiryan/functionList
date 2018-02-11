package com.functionlist.programLiveApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.functionlist.R;

public class LiveListActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_list);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_live1){
            Intent intent = new Intent(this,LiveActivity.class);
            intent.putExtra("url","http://117.169.120.217:8080/live/fhchinese/.m3u8");
            intent.putExtra("title","凤凰中文HD");
            startActivity(intent);
        }
    }
}
