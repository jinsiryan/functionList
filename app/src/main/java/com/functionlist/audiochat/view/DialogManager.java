package com.functionlist.audiochat.view;

import com.functionlist.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogManager {
	private Dialog mDialog;

	private ImageView mIcon;
	private ImageView mVoice;

	private TextView mLable;

	private Context mContext;

	public DialogManager(Context context){
		this.mContext = context;
	}
	public void showRecordingDialog(){
		mDialog = new Dialog(mContext, R.style.Them_AudioDialog);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.dialog_recorder, null);
		mDialog.setContentView(view);

		mIcon = (ImageView) view.findViewById(R.id.id_recorder_dialog_icon);
		mVoice = (ImageView) view.findViewById(R.id.id_recorder_dialog_voice);
		mLable = (TextView) view.findViewById(R.id.id_recorder_dialog_label);
		mDialog.show();
	}
	public void recording(){
		if(mDialog != null && mDialog.isShowing()){
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.VISIBLE);
			mLable.setVisibility(View.VISIBLE);

			mIcon.setImageResource(R.mipmap.recorder);
			mLable.setText("手指上滑，取消发送");
		}
	}
	public void wantToCancel(){
		if(mDialog != null && mDialog.isShowing()){
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.GONE);
			mLable.setVisibility(View.VISIBLE);

			mIcon.setImageResource(R.mipmap.cancel);
			mLable.setText("松开手指，取消发送");
		}
	}
	public void tooShort(){
		if(mDialog != null && mDialog.isShowing()){
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.GONE);
			mLable.setVisibility(View.VISIBLE);

			mIcon.setImageResource(R.mipmap.voice_to_short);
			mLable.setText("录音时间过短");
		}
	}
	public void dimissDialog(){
		if(mDialog != null && mDialog.isShowing()){
			mDialog.dismiss();
			mDialog = null;
		}
	}
	/**
	 * 通过level去更新voice上的图片
	 * @param level
	 */
	public void updateVoiceLevel(int level){
		if(mDialog != null && mDialog.isShowing()){
			int resId = mContext.getResources().getIdentifier("v"+ level,
					"drawable", mContext.getPackageName());
			mVoice.setImageResource(resId);
		}
	}

}
