package com.functionlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<FunctionItem> functionItems = new ArrayList<>();
    private RecyclerView recycleView;
    private FunctionAdapter functionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        initData();
        functionAdapter = new FunctionAdapter(this,functionItems);
        recycleView.setAdapter(functionAdapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    private void initData() {
        functionItems.add(new FunctionItem("shake","摇一摇"));
        functionItems.add(new FunctionItem("server","手机微服务器"));
        functionItems.add(new FunctionItem("audiochat","语音聊天"));
        functionItems.add(new FunctionItem("camera","摄像头拍照"));
        functionItems.add(new FunctionItem("floatview","悬浮球"));
        functionItems.add(new FunctionItem("wechatclipping","微信聊天图片显示风格"));
        functionItems.add(new FunctionItem("drawable","drawable应用"));
    }
}
