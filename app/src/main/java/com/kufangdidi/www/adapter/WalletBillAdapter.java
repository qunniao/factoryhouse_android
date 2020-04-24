package com.kufangdidi.www.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kufangdidi.www.R;
import com.kufangdidi.www.bean.WalletBillBean;
import com.kufangdidi.www.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * Effect:
 * Author: jww
 * Date: 2018/11/17
 */
public class WalletBillAdapter extends RecyclerView.Adapter<WalletBillAdapter.ViewHolder> {

    private Context context;
    private List<WalletBillBean.Data> list;

    public interface OnCallBack {
        void onClick(String state);
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    private OnCallBack onCallBack;

    public WalletBillAdapter(Context context) {
        this.context = context;
    }

    public void setdata(List<WalletBillBean.Data> data) {
        if (list == null) list = new ArrayList<WalletBillBean.Data>();
        list.clear();
        list.addAll(data);
        LogUtils.d("WalletBillAdapter list size:" + list.size());
    }

    public List<WalletBillBean.Data> getData() {
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_user_wallet_bill_list,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final WalletBillBean.Data data = list.get(position);
        LogUtils.d("name:" + data + " position:" + position);
        if (data.getType() == 1) {
            holder.wbl_money.setText("+" + data.getOrderMoney());
            holder.wbl_picture.setBackgroundResource(R.mipmap.icn_qianbao);
        }
        if (data.getType() == 2) {
            holder.wbl_money.setText("-" + data.getOrderMoney());
            holder.wbl_picture.setBackgroundResource(R.mipmap.me_bar_huiyuan);
        }
        holder.wbl_time.setText(data.getSuccessTime());
        holder.wbl_method.setText(data.getOrderName());
        holder.wbl_name.setText(data.getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView wbl_money, wbl_time, wbl_method, wbl_name;
        private ImageView wbl_picture;

        public ViewHolder(View itemView) {
            super(itemView);
            //绑定组件
            wbl_money = itemView.findViewById(R.id.wbl_money);
            wbl_time = itemView.findViewById(R.id.wbl_time);
            wbl_method = itemView.findViewById(R.id.wbl_method);
            wbl_name = itemView.findViewById(R.id.wbl_name);
            wbl_picture = itemView.findViewById(R.id.wbl_picture);

        }
    }
}
