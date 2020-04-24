package com.kufangdidi.www.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.kufangdidi.www.R;
import com.kufangdidi.www.activity.product.ProductHomeActivity;
import com.kufangdidi.www.modal.FplHouseModal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hqyl on 2017/7/26.
 */

public class ProductListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<FplHouseModal> list = new ArrayList<>();
    private Activity mContext;

    public ProductListAdapter(Activity context) {
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void setList(List<FplHouseModal> list){
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public FplHouseModal getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tab1_list_item, null);
        }

            final ImageView img_product_pic1 = (ImageView) convertView.findViewById(R.id.img_product_pic1);
            TextView tv_name = (TextView) convertView.findViewById(R.id.product_name);
            TextView tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            TextView product_price = (TextView) convertView.findViewById(R.id.product_price);
            TextView product_area = (TextView) convertView.findViewById(R.id.product_area);
            TextView danwei_name = (TextView) convertView.findViewById(R.id.danwei_name);


            RelativeLayout rl_product = (RelativeLayout) convertView.findViewById(R.id.rl_product);

        final FplHouseModal modal = list.get(position);


        tv_name.setText(modal.getAhtName());
        tv_address.setText(modal.getRegionName());
        product_price.setText(modal.getSalePrice().doubleValue()+"");
        product_area.setText(modal.getAhtavailableArea()+"");
        danwei_name.setText("元/m²/月");

        if(modal.getUrlList().size()>0){
            Picasso.with(mContext)
                    .load(modal.getUrlList().get(0))
                    .into(img_product_pic1);
        }

        rl_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(parent.getContext(), ProductHomeActivity.class);
                FplHouseModal modal = list.get(position);

                i.putExtra("productId",modal.getProductId());
                parent.getContext().startActivity(i);
            }
        });

        return convertView;
    }



}