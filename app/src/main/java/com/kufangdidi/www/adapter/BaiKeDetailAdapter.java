package com.kufangdidi.www.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kufangdidi.www.R;
import com.kufangdidi.www.bean.BaiKeBean;
import com.kufangdidi.www.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * Effect:
 * Author: jww
 * Date: 2018/11/17
 */
public class BaiKeDetailAdapter extends RecyclerView.Adapter<BaiKeDetailAdapter.ViewHolder> {

    private Context context;
    private List<BaiKeBean.Data.Content.EncyclopediaAnswer> list;

    public interface OnCallBack {
        void onClick(String state);
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    private OnCallBack onCallBack;

    public BaiKeDetailAdapter(Context context) {
        this.context = context;
    }

    public void setdata(List<BaiKeBean.Data.Content.EncyclopediaAnswer> content) {
        if (list == null) list = new ArrayList<BaiKeBean.Data.Content.EncyclopediaAnswer>();
        list.clear();
        list.addAll(content);
        LogUtils.d("BaiKeAdapter list size:" + list.size());
    }

    public List<BaiKeBean.Data.Content.EncyclopediaAnswer> getData() {
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_baike_detail_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final BaiKeBean.Data.Content.EncyclopediaAnswer answer = list.get(position);
        LogUtils.d("name:" + answer + " position:" + position);
        LogUtils.d("CreateName:"+answer.getCreateName());
        if (answer.getCreateName()==null) {
            holder.bkxq_item_name.setText("匿名");
        } else {
            holder.bkxq_item_name.setText(answer.getCreateName());
        }
        holder.bkxq_item_content.setText(answer.getContent());
        holder.bkxq_item_time.setText(answer.getCreateTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView bkxq_item_name, bkxq_item_content, bkxq_item_time;

        public ViewHolder(View itemView) {
            super(itemView);
            //绑定组件
            bkxq_item_name = itemView.findViewById(R.id.bkxq_item_name);
            bkxq_item_content = itemView.findViewById(R.id.bkxq_item_content);
            bkxq_item_time = itemView.findViewById(R.id.bkxq_item_time);
        }
    }
}
