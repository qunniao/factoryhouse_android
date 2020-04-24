package com.kufangdidi.www.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kufangdidi.www.R;
import com.kufangdidi.www.activity.baike.BaiKeDetailActivity;
import com.kufangdidi.www.bean.BaiKeBean;
import com.kufangdidi.www.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/*
 * Effect:
 * Author: jww
 * Date: 2018/11/17
 */
public class BaiKeAdapter extends RecyclerView.Adapter<BaiKeAdapter.ViewHolder> {

    private Context context;
    private List<BaiKeBean.Data.Content> list;

    public interface OnCallBack {
        void onClick(String state);
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    private OnCallBack onCallBack;

    public BaiKeAdapter(Context context) {
        this.context = context;
    }

    public void setdata(List<BaiKeBean.Data.Content> content) {
        if (list == null) list = new ArrayList<BaiKeBean.Data.Content>();
        list.clear();
        list.addAll(content);
        LogUtils.d("BaiKeAdapter list size:" + list.size());
    }

    public List<BaiKeBean.Data.Content> getData() {
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_baike_listview,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final BaiKeBean.Data.Content content = list.get(position);
        LogUtils.d("name:" + content + " position:" + position);
        holder.bk_item_title.setText(content.getTitle());
        holder.bk_item_data.setText(content.getCreateTime());
        if (content.getEncyclopediaAnswer().size() > 0) {
            holder.bk_item_huida.setText(content.getEncyclopediaAnswer().size() + "回答");
            holder.bk_item_jiejue.setText("已解决");
        }
        holder.bk_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BaiKeDetailActivity.class);
                intent.putExtra("bean", content);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                LogUtils.d("onclick:界面跳转" + 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView bk_item_title, bk_item_huida, bk_item_jiejue, bk_item_data;
        private LinearLayout bk_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            //绑定组件
            bk_item_title = itemView.findViewById(R.id.bk_item_title);
            bk_item_huida = itemView.findViewById(R.id.bk_item_huida);
            bk_item_jiejue = itemView.findViewById(R.id.bk_item_jiejue);
            bk_item_data = itemView.findViewById(R.id.bk_item_data);
            bk_layout = itemView.findViewById(R.id.bk_layout);
        }
    }
}
