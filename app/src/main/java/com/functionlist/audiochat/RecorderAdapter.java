package com.functionlist.audiochat;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.functionlist.R;


public class RecorderAdapter extends ArrayAdapter<Recorder>{
	private List<Recorder> mDatas;
	private Context mContext;
	
	private int mMinItemWidth;
	private int mMaxItemWidth;
	
	private LayoutInflater mInflater;

	@SuppressLint("ServiceCast")
	public RecorderAdapter(Context context, List<Recorder> datas) {
		super(context,-1, datas);
		mContext = context;
		mDatas = datas;
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		
		mMaxItemWidth = (int) (outMetrics.widthPixels * 0.7f);
		mMinItemWidth = (int) (outMetrics.widthPixels * 0.15f);
		
		mInflater = LayoutInflater.from(context);
	}
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder = null;
    	if(convertView == null){
    		convertView = mInflater.inflate(R.layout.item_recorder, parent,false);
    		holder = new ViewHolder();
    		holder.seconds = (TextView) convertView.findViewById(R.id.id_recorder_time);
    		holder.length = convertView.findViewById(R.id.id_recorder_length);
    		convertView.setTag(holder);
    	}else{
    		holder = (ViewHolder) convertView.getTag();
    	}
    	Recorder recorder = getItem(position);
    	holder.seconds.setText(Math.round(recorder.getTime()) +"\"");
    	ViewGroup.LayoutParams lp = holder.length.getLayoutParams();
    	lp.width = (int) (mMinItemWidth + (mMaxItemWidth /60f * recorder.getTime()));
    	return convertView;
    }
    private class ViewHolder{
    	TextView seconds;
    	View length;
    }
}
