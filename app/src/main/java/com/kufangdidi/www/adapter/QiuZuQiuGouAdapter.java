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
import com.kufangdidi.www.activity.QZQGXiangQingActivity;
import com.kufangdidi.www.modal.QiuZuQiuGouModal;
import com.kufangdidi.www.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/*
 * Effect:
 * Author: jww
 * Date: 2018/11/17
 */
public class QiuZuQiuGouAdapter extends RecyclerView.Adapter<QiuZuQiuGouAdapter.ViewHolder> {

    private Context context;
    private List<QiuZuQiuGouModal> list;

    public interface OnCallBack {
        void onClick(String state);
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    private OnCallBack onCallBack;

    public QiuZuQiuGouAdapter(Context context) {
        this.context = context;
    }

    public void setdata(List<QiuZuQiuGouModal> data) {
        if (list == null) list = new ArrayList<QiuZuQiuGouModal>();
        list.clear();
        list.addAll(data);
    }

    public List<QiuZuQiuGouModal> getData() {
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_qiuzu_list, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final QiuZuQiuGouModal modal = list.get(position);
        if (modal.getStatus() == 2) {
            holder.m_qiuzu.setText("求购");
            holder.m_qiuzu.setTextColor(context.getResources().getColor(R.color.colorPink));
            holder.m_qiuzu.setBackground(context.getResources().getDrawable(R.drawable.pink_view1));
        }
        holder.name.setText(modal.getTitle());
        holder.area.setText(modal.getRegion());
        holder.time.setText(modal.getCreateTime());
        LogUtils.d("name:" + modal.getContact());
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QZQGXiangQingActivity.class);
                intent.putExtra("modal", modal);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                LogUtils.d("onclick:" + 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, area, time, m_qiuzu;
        private LinearLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            //绑定组件

            name = itemView.findViewById(R.id.qzqg_name);
            area = itemView.findViewById(R.id.qzqg_area);
            time = itemView.findViewById(R.id.qzqg_time);
            itemLayout = itemView.findViewById(R.id.qzqg_layout);
            m_qiuzu = itemView.findViewById(R.id.m_qiuzu);
        }
    }
}
