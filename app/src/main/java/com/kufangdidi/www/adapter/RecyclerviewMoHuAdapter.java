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

import com.kufangdidi.www.R;
import com.kufangdidi.www.activity.product.ProductHomeActivity;
import com.kufangdidi.www.bean.HouseBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.kufangdidi.www.utils.Constant.SERVER_PIC_HOSTds;

/*
 * Effect:
 * Author: jww
 * Date: 2018/11/17
 */
public class RecyclerviewMoHuAdapter extends RecyclerView.Adapter<RecyclerviewMoHuAdapter.ViewHolder> {

    private Context context;
    private List<HouseBean.DataBean> list;

    public interface OnCallBack {
        void onClick(String state);
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    private OnCallBack onCallBack;

    public RecyclerviewMoHuAdapter(Context context, List<HouseBean.DataBean> data) {
        this.context = context;
        if (list == null) list = new ArrayList<>();
        list.clear();
        list.addAll(data);
    }

    public void setData(List<HouseBean.DataBean> data) {
        if (list == null) list = new ArrayList<>();
        list.clear();
        list.addAll(data);
    }

    public List<HouseBean.DataBean> getData() {
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final HouseBean.DataBean item = list.get(position);

        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        holder.iv.measure(w, h);
        int height = holder.iv.getMeasuredHeight();
        int width = holder.iv.getMeasuredWidth();
        if (item.getTitlePicture().length() != 0) {
            Picasso.with(context)
                    .load(SERVER_PIC_HOSTds + item.getTitlePicture())
                    .resize(width, height)
                    .into(holder.iv);
        }

        holder.tvTitle.setText(item.getTitle());
        holder.tvAddress.setText("地址：" + item.getAddress());
        holder.product_price.setText(item.getRent());
        holder.tv_mianji.setText("面积：" + item.getArea() + "㎡");
        holder.ll_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductHomeActivity.class);
                intent.putExtra("product_id", item.getProductId());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv;
        private TextView tvTitle;
        private TextView tvAddress;
        private TextView product_price;
        private TextView tv_mianji;
        private LinearLayout ll_product;

        public ViewHolder(View itemView) {
            super(itemView);
            //绑定组件
            iv = itemView.findViewById(R.id.img_product_pic1);
            tvTitle = itemView.findViewById(R.id.product_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            product_price = itemView.findViewById(R.id.product_price);
            tv_mianji = itemView.findViewById(R.id.tv_mianji);
            ll_product = itemView.findViewById(R.id.ll_product);
        }
    }
}
