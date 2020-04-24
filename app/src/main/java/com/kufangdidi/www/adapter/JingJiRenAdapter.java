package com.kufangdidi.www.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kufangdidi.www.R;
import com.kufangdidi.www.activity.DianPuActivity;
import com.kufangdidi.www.modal.JingJiRenModal;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.mob.MobSDK.getContext;

/*
 * Effect:
 * Author: jww
 * Date: 2018/11/17
 */
public class JingJiRenAdapter extends RecyclerView.Adapter<JingJiRenAdapter.ViewHolder> {

    private Context context;
    private List<JingJiRenModal> list;

    public interface OnCallBack {
        void onClick(String state);
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    private OnCallBack onCallBack;

    public JingJiRenAdapter(Context context) {
        this.context = context;
    }

    public void setdata(List<JingJiRenModal> data) {
        if (list == null) list = new ArrayList<JingJiRenModal>();
        list.clear();
        list.addAll(data);
    }

    public List<JingJiRenModal> getData() {
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_jingjiren_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final JingJiRenModal modal = list.get(position);
        if (modal.getUserName() == null || modal.getUserName().length() == 0) {
            holder.tv_name.setText(modal.getUserPhone());
        } else {
            holder.tv_name.setText(modal.getUserName());
        }
        holder.quyu.setText(modal.getMainArea());
        holder.renqi.setText("人气："+modal.getPopularValue());
        holder.fangyuan.setText("房源："+modal.getFplHouse()+"套");
        if (modal.getAvatarUrl()!=null&&modal.getAvatarUrl().length()!=0){
            Picasso.with(getContext())
                    .load(Constant.SERVER_PIC_HOSTds + modal.getAvatarUrl())
                    .into(holder.touxiang);
        }
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DianPuActivity.class);
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

        private TextView tv_name, quyu, renqi, fangyuan;
        private RelativeLayout itemLayout;
        private ImageView duanxin, dianhua;
        private CircleImageView touxiang;

        public ViewHolder(View itemView) {
            super(itemView);
            //绑定组件
            tv_name = itemView.findViewById(R.id.tv_name);
            quyu = itemView.findViewById(R.id.quyu);
            renqi = itemView.findViewById(R.id.jjr_renqi);
            fangyuan = itemView.findViewById(R.id.fangyuan);
            duanxin = itemView.findViewById(R.id.duanxin);
            dianhua = itemView.findViewById(R.id.dianhua);
            itemLayout = itemView.findViewById(R.id.itemlayout);
            touxiang = itemView.findViewById(R.id.touxiang);
        }
    }
}
