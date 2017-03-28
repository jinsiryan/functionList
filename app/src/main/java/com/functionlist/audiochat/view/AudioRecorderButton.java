package com.functionlist.audiochat.view;

import com.functionlist.R;
import com.functionlist.audiochat.view.AudioManager;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class AudioRecorderButton extends Button implements AudioManager.AudioStateListener {
	private static final int DISTANCE_Y_CANCEL = 50;
	private static final int STATE_NOMAL = 1;
	private static final int STATE_RECORDING = 2;
	private static final int STATE_WANT_TO_CANCEL = 3;

	private int mCurState = STATE_NOMAL;
	/**
	 * 判断是否已经开始录音
	 */
	private boolean isRecording;

	private DialogManager mDialogManager;

	private AudioManager mAudioManager;

	private float mTime;
	//是否触发longclik
	private boolean mReady;


	public interface AudioFinishRecorderListener{
		void onRinish(float seconds,String filePath);
	}
	private AudioFinishRecorderListener audioFinishRecorderListener;

	public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener){
		this.audioFinishRecorderListener = listener;
	}

	public AudioRecorderButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}



	public AudioRecorderButton(Context context) {
		super(context);
		init();
	}
	private void init() {
		mDialogManager = new DialogManager(getContext());

		String dir = Environment.getExternalStorageDirectory() + "/imooc_recorder_audios";
		mAudioManager = AudioManager.getInstance();
		mAudioManager.setDir(dir);
		mAudioManager.setOnAudioStateListener(this);
		setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				mReady = true;
				mAudioManager.prepareAudio();
				return false;
			}
		});
	}
	/**
	 * 获取音量大小
	 */
	private Runnable mGetVoiceLevelRunnable = new Runnable() {

		@Override
		public void run() {
			while(isRecording){
				try {
					Thread.sleep(100);
					mTime += 0.1f;
					mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
	private static final int MSG_AUDIO_PREPARED = 0X110;
	private static final int MSG_VOICE_CHANGED = 0X111;
	private static final int MSG_DIALOG_DIMISS = 0X112;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case MSG_AUDIO_PREPARED:
					//
					mDialogManager.showRecordingDialog();
					isRecording = true;
					new Thread(mGetVoiceLevelRunnable).start();
					break;
				case MSG_VOICE_CHANGED:
					mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
					break;
				case MSG_DIALOG_DIMISS:
					mDialogManager.dimissDialog();
					break;
				default:
					break;
			}
		}
	};
	@Override
	public void wellPrepared() {
		mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				changeState(STATE_RECORDING);
				break;
			case MotionEvent.ACTION_MOVE:
				if (isRecording) {
					// 根据x,y的坐标，判断是否想要取消
					if (wantToCanCancel(x, y)) {
						changeState(STATE_WANT_TO_CANCEL);
					} else {
						changeState(STATE_RECORDING);
					}
				}

				break;
			case MotionEvent.ACTION_UP:
				if(!mReady){//onlongclick还没有触发 重置状态就可以了
					reset();
					return super.onTouchEvent(event);
				}
				if(!isRecording || mTime < 0.6f){//已经开始准备了，但还没有准备完毕
					mDialogManager.tooShort();
					mAudioManager.cancel();
					mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);
				}else if (mCurState == STATE_RECORDING) {//正常录制结束
					mDialogManager.dimissDialog();
					mAudioManager.release();
					if(audioFinishRecorderListener != null){
						audioFinishRecorderListener.onRinish(mTime, mAudioManager.getCurrentFilePath());
					}
				} else if (mCurState == STATE_WANT_TO_CANCEL) {
					mDialogManager.dimissDialog();
					mAudioManager.cancel();
				}
				reset();
				break;
			default:
				break;
		}

		return super.onTouchEvent(event);
	}

	/**
	 * 恢复状态及标志位
	 */
	private void reset() {
		isRecording = false;
		mTime = 0;
		mReady = false;
		changeState(STATE_NOMAL);
	}

	private boolean wantToCanCancel(int x, int y) {
		if(x < 0  || x > getWidth()){
			return true;
		}
		if(y < - DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL){
			return true;
		}
		return false;
	}

	private void changeState(int state) {
		if (mCurState != state) {
			mCurState = state;
			switch (state) {
				case STATE_NOMAL:
					setBackgroundResource(R.drawable.btn_recorder_nomal);
					setText(R.string.str_recorder_normal);
					break;
				case STATE_WANT_TO_CANCEL:
					setBackgroundResource(R.drawable.btn_recording);
					setText(R.string.str_recorder_want_cancel);
					mDialogManager.wantToCancel();
					break;
				case STATE_RECORDING:
					setBackgroundResource(R.drawable.btn_recording);
					setText(R.string.str_recorder_recording);
					if(isRecording){
						mDialogManager.recording();
					}
					break;
				default:
					break;
			}
		}
	}




}
