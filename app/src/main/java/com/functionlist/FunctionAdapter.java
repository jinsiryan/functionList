package com.functionlist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.functionlist.WeChatImage.WechatClippingActivity;
import com.functionlist.animation360.AnimationActivity;
import com.functionlist.audiochat.AudioChatActivity;
import com.functionlist.camera.CameraActivity;
import com.functionlist.camera.CustomCameraActivity;
import com.functionlist.drawable.DrawableActivity;
import com.functionlist.droidserver.ServerActivity;
import com.functionlist.shake.ShakeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imo on 2016/9/25.
 */
public class FunctionAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<FunctionItem> functionItems;
    private OnItemClick onItemClick = new OnItemClick();
    public FunctionAdapter(Context context,List<FunctionItem> items){
        this.context = context;
        if(items == null){
            functionItems = new ArrayList<>();
        }else {
            functionItems = items;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private Button btView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.btView = (Button) itemView;
        }
        public Button getButtonView(){
            return btView;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new Button(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        FunctionItem item = functionItems.get(position);
        vh.getButtonView().setText(item.getDesc());
        vh.getButtonView().setTag(item.getId());
        vh.getButtonView().setOnClickListener(onItemClick);
    }

    @Override
    public int getItemCount() {
        return functionItems.size();
    }

    class OnItemClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch ((String)view.getTag()){
                case "shake":
                    intent = new Intent(context, ShakeActivity.class);
                    break;
                case "server":
                    intent =  new Intent(context, ServerActivity.class);
                    break;
                case "audiochat":
                    intent = new Intent(context, AudioChatActivity.class);
                    break;
                case "camera":
                    intent = new Intent(context, CameraActivity.class);
                    break;
                case "floatview":
                    intent = new Intent(context, AnimationActivity.class);
                    break;
                case "wechatclipping":
                    intent = new Intent(context, WechatClippingActivity.class);
                    break;
                case "drawable":
                    intent = new Intent(context, DrawableActivity.class);
                    break;
                case "liveApp":
                    intent = new Intent(context, DrawableActivity.class);
                    break;
            }
            context.startActivity(intent);
        }
    }
}
