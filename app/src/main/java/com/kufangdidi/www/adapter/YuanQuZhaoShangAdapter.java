package com.kufangdidi.www.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kufangdidi.www.R;
import com.kufangdidi.www.activity.YQZSDetailActivity;
import com.kufangdidi.www.bean.YuanQuZhaoShangBean;
import com.kufangdidi.www.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/*
 * Effect:
 * Author: jww
 * Date: 2018/11/17
 */
public class YuanQuZhaoShangAdapter extends RecyclerView.Adapter<YuanQuZhaoShangAdapter.ViewHolder> {

    private Context context;
    private List<YuanQuZhaoShangBean.Data.Content> list;

    public interface OnCallBack {
        void onClick(String state);
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    private OnCallBack onCallBack;

    public YuanQuZhaoShangAdapter(Context context) {
        this.context = context;
    }

    public void setdata(List<YuanQuZhaoShangBean.Data.Content> data) {
        if (list == null) list = new ArrayList<YuanQuZhaoShangBean.Data.Content>();
        list.clear();
        list.addAll(data);
        LogUtils.d("YuanQuZhaoShangAdapter list size:" + list.size());
    }

    public List<YuanQuZhaoShangBean.Data.Content> getData() {
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_yuanquzhaoshang_list,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final YuanQuZhaoShangBean.Data.Content data = list.get(position);
        LogUtils.d("name:" + data + " position:" + position);
        holder.yqzs_tittle.setText(data.getTitle());
        holder.yqzs_address.setText(data.getParkAddress());
        holder.yqzs_price.setText(data.getParkprice() + "");
        if (data.getPictureStorage()!=null&&data.getPictureStorage().size() > 0) {
            /*
            Picasso.with(context)
                    .load(Constant.SERVER_PIC_HOSTds + data.getPictureStorage().get(0).getUrl())
                    .into(holder.yqzs_picture);
                    */
            /*
            Picasso.with(context)
                    .load(data.getPictureStorage().get(0).getUrl())
                    .into(holder.yqzs_picture);
*/


            RequestOptions options = new RequestOptions()
                    .error(R.mipmap.home_list_demo)
                    .placeholder(R.mipmap.home_list_demo);
            Glide.with(context).load(data.getTitlePicture()).apply(options).into(holder.yqzs_picture);

            LogUtils.d("url:"+data.getTitlePicture());
        }
        holder.yqxq_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, YQZSDetailActivity.class);
                intent.putExtra("bean",data);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                LogUtils.d("onclick:"+1);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView yqzs_tittle, yqzs_address, yqzs_price;
        private ImageView yqzs_picture;
        private LinearLayout yqxq_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            //绑定组件
            yqzs_tittle = itemView.findViewById(R.id.yqzs_tittle);
            yqzs_picture = itemView.findViewById(R.id.yqzs_picture);
            yqzs_address = itemView.findViewById(R.id.yqzs_address);
            yqzs_price = itemView.findViewById(R.id.yqzs_price);
            yqxq_layout = itemView.findViewById(R.id.yqxq_layout);
        }
    }
}
