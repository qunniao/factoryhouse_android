package com.kufangdidi.www.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.kufangdidi.www.R;
import com.kufangdidi.www.bean.ProductBean;
import com.kufangdidi.www.utils.LogUtils;

import java.util.List;

import static com.kufangdidi.www.utils.Constant.SERVER_PIC_HOSTds;

public class ProductRvAdapter extends BaseQuickAdapter<ProductBean.DataBean.ContentBean, BaseViewHolder> {
    public ProductRvAdapter(int layoutResId, @Nullable List<ProductBean.DataBean.ContentBean> data) {
        super(layoutResId, data);
    }

    public ProductRvAdapter(@Nullable List<ProductBean.DataBean.ContentBean> data) {
        super(R.layout.item_product, data);
    }

    public ProductRvAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductBean.DataBean.ContentBean item) {
        ImageView iv = helper.itemView.findViewById(R.id.img_product_pic1);
        TextView tvTitle = helper.itemView.findViewById(R.id.product_name);
        TextView tvAddress = helper.itemView.findViewById(R.id.tv_address);
        TextView product_price = helper.itemView.findViewById(R.id.product_price);
        TextView tv_mianji = helper.itemView.findViewById(R.id.tv_mianji);
        RequestOptions options = new RequestOptions()
                .error(R.mipmap.home_list_demo)
                .placeholder(R.mipmap.home_list_demo);
        LogUtils.d("image url:"+SERVER_PIC_HOSTds+item.getTitlePicture());
        //Glide.with(mContext).load(SERVER_PIC_HOSTds+item.getTitlePicture()).apply(options).into(iv);

        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        iv.measure(w, h);
        int height = iv.getMeasuredHeight();
        int width = iv.getMeasuredWidth();
        if(height==0 && width==0){
            width=100;
            height=100;
        }
        Picasso.with(mContext)
                .load(SERVER_PIC_HOSTds+item.getTitlePicture())
                .resize(width,height)
                .into(iv);

        tvTitle.setText(item.getTitle());
        tvAddress.setText("地址：" + item.getAddress());
        product_price.setText(item.getRent()+item.getRentUnit());
        tv_mianji.setText("面积：" + item.getArea() +item.getAreaUnit());

    }
}
