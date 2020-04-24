package com.kufangdidi.www.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kufangdidi.www.R;
import com.kufangdidi.www.activity.news.NewsDetailActivity;
import com.kufangdidi.www.modal.NewsModal;
import com.kufangdidi.www.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * Effect:
 * Author: jww
 * Date: 2018/11/17
 */
public class RecyclerviewNewsAdapter extends RecyclerView.Adapter<RecyclerviewNewsAdapter.ViewHolder> {

    private Context context;
    private List<NewsModal> list;

    public interface OnCallBack {
        void onClick(String state);
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    private OnCallBack onCallBack;

    public RecyclerviewNewsAdapter(Context context) {
        this.context = context;
    }

    public void setdata(List<NewsModal> data) {
        if(list == null)list = new ArrayList<>();
        list.clear();
        list.addAll(data);
    }

    public List<NewsModal> getData(){
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_news_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final NewsModal modal = list.get(position);
        holder.title.setText(modal.getTitle());
        holder.title.setText(modal.getTitle());
        LogUtils.d("title:"+modal.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onClick("");
            }
        });

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("modal",modal);
                context.startActivity(intent);
//                LogUtils.d("onclick:"+1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private RelativeLayout itemLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            //绑定组件
            title = itemView.findViewById(R.id.title);
            itemLayout = itemView.findViewById(R.id.itemLayout);
        }
    }
}
