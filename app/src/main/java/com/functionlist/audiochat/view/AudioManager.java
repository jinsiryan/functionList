package com.functionlist.audiochat.view;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.media.MediaRecorder;
import android.util.Log;

public class AudioManager {
	private MediaRecorder mMediaRecorder;
	private String mDir;
	private String mCurrentFilePath;
	private boolean isPrepared;


	private static AudioManager mInstance;

	public static AudioManager getInstance(){
		if(mInstance == null){
			synchronized (AudioManager.class) {
				if(mInstance == null){
					mInstance = new AudioManager();
				}
			}
		}
		return mInstance;
	}
	private AudioManager(){}

	public void setDir(String dir){
		mDir = dir;
	}
	/**
	 * 准备完毕 的回调
	 * @author PC
	 *
	 */
	public interface AudioStateListener{
		void wellPrepared();
	}
	public AudioStateListener mListener;

	public void setOnAudioStateListener(AudioStateListener listener){
		mListener = listener;
	}

	/**
	 * 准备
	 */
	public void prepareAudio(){
		try {
			isPrepared = false;
			File dir = new File(mDir);
			if(!dir.exists()){
				dir.mkdirs();
			}
			String fileName = generateFileName();
			File file = new File(dir,fileName);
			mCurrentFilePath = file.getAbsolutePath();
			mMediaRecorder = new MediaRecorder();
			//设置输出文件
			mMediaRecorder.setOutputFile(file.getAbsolutePath());
			//设置音频源为麦克风
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			//设置音频的格式
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			//设置音频的编码为amr
			mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mMediaRecorder.prepare();
			mMediaRecorder.start();
			//准备结束
			isPrepared = true;
			if(mListener != null){
				mListener.wellPrepared();
			}
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 随机生成文件的名称
	 * @return
	 */
	private String generateFileName() {
		return UUID.randomUUID().toString();
	}
	/**
	 * 获取音量等级
	 * @return
	 */
	public int getVoiceLevel(int maxLevel){
		if(isPrepared){
			try {
				//mMediaRecorder.getMaxAmplitude() 在1-32767之间
				int m = maxLevel * mMediaRecorder.getMaxAmplitude() / 32768 +1;
				Log.d("AudioManager",  "获取音量=" + m);
				return m;
			} catch (Exception e) {

			}
		}
		return 1;
	}
	/**
	 * 释放
	 */
	public void release(){
		mMediaRecorder.stop();
		mMediaRecorder.release();
		mMediaRecorder = null;
		isPrepared = false;
	}
	/**
	 * 取消
	 */
	public void cancel(){

		release();
		if(mCurrentFilePath != null){
			File file = new File(mCurrentFilePath);
			file.delete();
		}

	}
	public String getCurrentFilePath() {
		// TODO Auto-generated method stub
		return mCurrentFilePath;
	}
}
