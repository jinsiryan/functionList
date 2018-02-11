package com.functionlist.programLiveApp;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.functionlist.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

public class LiveActivity extends FragmentActivity {
    private static final int RETRY_TIMES = 5;
    private static final int AUTO_HIDE_TIME = 5000;
    private String mUrl;
    private String mTitle;
    private ImageView mBackButton;
    private TextView mTitleText;
    private TextView mSysTime;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private VideoView mVideoView;
    private int mCount = 0;
    private RelativeLayout mLoadingLayout;
    private RelativeLayout mRootView;
    private LinearLayout mTopLayout;
    private LinearLayout mBottomLayout;
    private ImageView mPlayButton;
    private Handler mHandler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 隐藏当前Activity的标题
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
        initView();
        initPlayer();
    }

    private void initPlayer() {
        Vitamio.isInitialized(getApplication());
        mVideoView = ((VideoView) findViewById(R.id.surface_view));
        mVideoView.setVideoURI(Uri.parse(mUrl));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if(mCount > RETRY_TIMES){
                    new AlertDialog.Builder(LiveActivity.this)
                            .setMessage(R.string.video_error_message)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).setCancelable(false).show();
                } else {
                    mVideoView.stopPlayback();
                    mVideoView.setVideoURI(Uri.parse(mUrl));
                }
                mCount++;
                return false;
            }
        });
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what){
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        mLoadingLayout.setVisibility(View.VISIBLE);
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        mLoadingLayout.setVisibility(View.GONE);
                    break;

                }
                return false;
            }
        });
    }

    private void initView() {
        mBackButton = (ImageView)findViewById(R.id.iv_black);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleText = ((TextView) findViewById(R.id.tv_title));
        mTitleText.setText(mTitle);
        mSysTime = ((TextView) findViewById(R.id.tv_systime));
        mSysTime.setText(getCurrentTime());
        mLoadingLayout = findViewById(R.id.rl_loading_layout);

        mRootView = findViewById(R.id.activity_live);
        mTopLayout = findViewById(R.id.ll_top_layout);
        mBottomLayout = findViewById(R.id.ll_play_layout);
        mRootView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mBottomLayout.getVisibility() == View.VISIBLE
                        || mTopLayout.getVisibility() == View.VISIBLE){
                    mBottomLayout.setVisibility(View.GONE);
                    mTopLayout.setVisibility(View.GONE);
                    return;
                }
                if(mVideoView.isPlaying()){
                    mPlayButton.setImageResource(R.drawable.ic_pause_white_24dp);
                }else {
                    mPlayButton.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                }
                mBottomLayout.setVisibility(View.VISIBLE);
                mTopLayout.setVisibility(View.VISIBLE);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBottomLayout.setVisibility(View.GONE);
                        mTopLayout.setVisibility(View.GONE);
                    }
                },AUTO_HIDE_TIME);
            }
        });

        mPlayButton = findViewById(R.id.iv_play);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVideoView.isPlaying()){
                    mVideoView.stopPlayback();
                }else {
                    mVideoView.setVideoURI(Uri.parse(mUrl));
                    mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mVideoView.start();
                        }
                    });
                    mPlayButton.setImageResource(R.drawable.ic_pause_white_24dp);
                }
            }
        });

    }
    private String getCurrentTime(){
        return dateFormat.format(System.currentTimeMillis());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCount = 0;
        if(mVideoView != null){
            mVideoView.stopPlayback();
        }
    }
}
