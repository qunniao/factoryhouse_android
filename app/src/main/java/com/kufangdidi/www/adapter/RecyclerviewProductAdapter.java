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

import com.squareup.picasso.Picasso;
import com.kufangdidi.www.R;
import com.kufangdidi.www.activity.product.ProductHomeActivity;
import com.kufangdidi.www.bean.ProductBean;

import java.util.ArrayList;
import java.util.List;

import static com.kufangdidi.www.utils.Constant.SERVER_PIC_HOSTds;

/*
 * Effect:
 * Author: jww
 * Date: 2018/11/17
 */
public class RecyclerviewProductAdapter extends RecyclerView.Adapter<RecyclerviewProductAdapter.ViewHolder> {

    private Context context;
    private List<ProductBean.DataBean.ContentBean> list;

    public interface OnCallBack {
        void onClick(String state);
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    private OnCallBack onCallBack;

    public RecyclerviewProductAdapter(Context context,List<ProductBean.DataBean.ContentBean> data) {
        this.context = context;
        if(list == null)list = new ArrayList<>();
        list.clear();
        list.addAll(data);
    }

    public void setData(List<ProductBean.DataBean.ContentBean> data) {
        if(list == null)list = new ArrayList<>();
        list.clear();
        list.addAll(data);
    }

    public List<ProductBean.DataBean.ContentBean> getData(){
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ProductBean.DataBean.ContentBean item = list.get(position);

        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        holder.iv.measure(w, h);
        int height = holder.iv.getMeasuredHeight();
        int width = holder.iv.getMeasuredWidth();

        Picasso.with(context)
                .load(SERVER_PIC_HOSTds+item.getTitlePicture())
                .resize(width,height)
                .into(holder.iv);

        holder.tvTitle.setText(item.getTitle());
        holder.tvAddress.setText("地址：" + item.getAddress());
        holder.product_price.setText(item.getRent()+item.getRentUnit());
        holder.tv_mianji.setText("面积：" + item.getArea() + item.getAreaUnit());
        holder.ll_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductHomeActivity.class);
                intent.putExtra("product_id", item.getProductId());
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
