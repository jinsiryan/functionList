package com.functionlist.audiochat;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.functionlist.R;
import com.functionlist.audiochat.view.AudioRecorderButton;

import java.util.ArrayList;
import java.util.List;

public class AudioChatActivity extends AppCompatActivity {
    private ListView mListView;
    private ArrayAdapter<Recorder> mAdapter;
    private List<Recorder> mDatas = new ArrayList<Recorder>();
    private AudioRecorderButton mAudioRecorderButton;
    private View animview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_chat);
        mListView = (ListView) findViewById(R.id.id_listview);
        mAudioRecorderButton = (AudioRecorderButton) findViewById(R.id.id_recorder_button);
        mAudioRecorderButton.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {

            @Override
            public void onRinish(float seconds, String filePath) {
                Recorder recorder = new Recorder(seconds, filePath);
                mDatas.add(recorder);
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(mDatas.size() - 1);
            }
        });
        mAdapter = new RecorderAdapter(this, mDatas);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //
                if(animview != null){
                    animview.setBackgroundResource(R.mipmap.adj);
                    animview = null;
                }
                animview= view.findViewById(R.id.id_recorder_anim);
                animview.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable anim = (AnimationDrawable) animview.getBackground();
                anim.start();

                MediaManager.playSound(mDatas.get(position).filePath,new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if(animview != null)
                            animview.setBackgroundResource(R.mipmap.adj);

                    }
                });


            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }
}
