package com.functionlist.programLiveApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.functionlist.R;

import java.util.ArrayList;
import java.util.List;

public class LiveListActivity extends AppCompatActivity{
    private LiveListAdapter adapter;
    private List<LiveItem> liveItems = new ArrayList<>();
    private RecyclerView recycleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("直播电视节目");
        setContentView(R.layout.activity_live_list);
        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        initLiveList();
        adapter = new LiveListAdapter(this,liveItems);
        recycleView.setAdapter(adapter);
        recycleView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recycleView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter.setOnItemClickListener(new LiveListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LiveItem item) {
                Intent intent = new Intent(LiveListActivity.this,LiveActivity.class);
                intent.putExtra("url",item.getUrl());
                intent.putExtra("title",item.getTitle());
                startActivity(intent);
            }
        });
    }

    private void initLiveList() {
        liveItems.add(new LiveItem("凤凰中文HD","http://117.169.120.217:8080/live/fhchinese/.m3u8"));
    }
}
