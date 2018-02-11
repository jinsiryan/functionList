package com.functionlist.programLiveApp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.functionlist.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imo on 2016/9/25.
 */
public class LiveListAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<LiveItem> functionItems;
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(LiveItem item);
    }

    public LiveListAdapter(Context context, List<LiveItem> items) {
        this.context = context;
        if (items == null) {
            functionItems = new ArrayList<>();
        } else {
            functionItems = items;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.tv_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(functionItems.get(getLayoutPosition()));
                    }
                }
            });
        }

        public TextView getTextView() {
            return textView;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.text_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        TextView textView = vh.getTextView();
        LiveItem item = functionItems.get(position);
        textView.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return functionItems.size();
    }

}
