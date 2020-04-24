package com.kufangdidi.www.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.squareup.picasso.Picasso;
import com.kufangdidi.www.R;
import com.kufangdidi.www.activity.JingJiRenActivity;
import com.kufangdidi.www.activity.QiuZuActivity;
import com.kufangdidi.www.activity.YuanQuZhaoShangActivity;
import com.kufangdidi.www.activity.baike.BaiKeActivity;
import com.kufangdidi.www.activity.news.XinWenActivity;
import com.kufangdidi.www.activity.product.ProductListActivity;
import com.kufangdidi.www.activity.product.SearchProductActivity;
import com.kufangdidi.www.activity.user.UserZhaoFangActivity;
import com.kufangdidi.www.adapter.RecyclerviewProductAdapter;
import com.kufangdidi.www.app.Api;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.bean.ProductBean;
import com.kufangdidi.www.modal.AdModal;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by admin on 2017/5/16.
 */

public class FragmentOne extends Fragment implements View.OnClickListener {
    private Context mContext;
    private RelativeLayout rl_search;


    private ArrayList<Fragment> fragmentList;
    private View dot0,dot1;
    LayoutInflater inflater;

    //存放图片的id
    private int[] imageIds = new int[]{
            R.mipmap.lunbo1,
            R.mipmap.lunbo2,
            R.mipmap.lunbo3
    };
    private ViewPager mViewPaper;
    private List<ImageView> images;
    private List<View> dots;
    private int currentItem;
    private ViewPagerAdapter adapter;
    private static TextView tv_city;
    ImageView img_moren_bannar;
    //记录上一次点的位置
    private int oldPosition = 0;
    private ScheduledExecutorService scheduledExecutorService;

    private LinearLayout ll_baike;

    private RelativeLayout rl_menu1,rl_menu2,rl_menu3,rl_menu4,rl_menu5,rl_menu6,rl_menu7,rl_menu8;

    private List<ProductBean.DataBean.ContentBean> data;
    private RecyclerviewProductAdapter productRvAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab1, container, false);
    }
    private View dot_one,dot_two,dot_three;
    private View view;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d("FragmentOne启动");
        mContext = getContext();
        view = getView();
        // 注册订阅者

        inflater= (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        dot_one = view.findViewById(R.id.dot_one);
        dot_two = view.findViewById(R.id.dot_two);
        dot_three = view.findViewById(R.id.dot_three);

/*
        LinearLayout bandHome = (LinearLayout) view.findViewById(R.id.bandHome);
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        bandHome.measure(w, h);
        */

        //height = bandHome.getMeasuredHeight();
       // LogUtils.d("bandHome height:"+height);

        mViewPaper = (ViewPager)view.findViewById(R.id.vp);

        //显示的小点
        dots = new ArrayList<View>();
        dot_one = view.findViewById(R.id.dot_one);
        dot_two = view.findViewById(R.id.dot_two);
        dot_three = view.findViewById(R.id.dot_three);
        img_moren_bannar = (ImageView) view.findViewById(R.id.img_moren_bannar);
        img_moren_bannar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                dots.get(position).setBackgroundResource(R.mipmap.dot_black);
                dots.get(oldPosition).setBackgroundResource(R.mipmap.dot_white);

                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

        });
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
                new ViewPageTask(),
                6,
                6,
                TimeUnit.SECONDS);

        initView();

    }

    private void initView() {
        tv_city = (TextView) view.findViewById(R.id.tv_city);
        rl_search = (RelativeLayout) view.findViewById(R.id.rl_search);
        rl_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchProductActivity.class);
                startActivity(intent);
            }
        });

        ll_baike = (LinearLayout) view.findViewById(R.id.ll_baike);
        ll_baike.setOnClickListener(this);

        rl_menu1 = (RelativeLayout) view.findViewById(R.id.rl_menu1);
        rl_menu1.setOnClickListener(this);
        rl_menu2 = (RelativeLayout) view.findViewById(R.id.rl_menu2);
        rl_menu2.setOnClickListener(this);
        rl_menu3 = (RelativeLayout) view.findViewById(R.id.rl_menu3);
        rl_menu3.setOnClickListener(this);
        rl_menu4 = (RelativeLayout) view.findViewById(R.id.rl_menu4);
        rl_menu4.setOnClickListener(this);
        rl_menu5 = (RelativeLayout) view.findViewById(R.id.rl_menu5);
        rl_menu5.setOnClickListener(this);
        rl_menu6 = (RelativeLayout) view.findViewById(R.id.rl_menu6);
        rl_menu6.setOnClickListener(this);
        rl_menu7 = (RelativeLayout) view.findViewById(R.id.rl_menu7);
        rl_menu7.setOnClickListener(this);
        rl_menu8 = (RelativeLayout) view.findViewById(R.id.rl_menu8);
        rl_menu8.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        data = new ArrayList<>();
        productRvAdapter = new RecyclerviewProductAdapter(mContext,data);
        recyclerView.setAdapter(productRvAdapter);
        loadData();
    }

    private void loadData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageSize", 50 + "");
        params.put("pageNum", 1 + "");
        params.put("status", 1 + "");
        params.put("types", 1 + "");
        params.put("token", BaseApplication.getSpUtils().getString("token"));
        OkGo.post(Api.SERCH_RENT_OR_SALE).params(params).tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i("response:", s);
                        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(s);
                        if (jsonObject.getInteger("code") == 0) {
                            ProductBean productBean = com.alibaba.fastjson.JSONObject.parseObject(s, ProductBean.class);
                            List<ProductBean.DataBean.ContentBean> content = productBean.getData().getContent();
                                    data.addAll(content);
                                    productRvAdapter.setData(data);
                                    productRvAdapter.notifyDataSetChanged();
                                    updateListHeight();
                        } else {
                            Toast.makeText(mContext, jsonObject.getString("msg"),
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(mContext, "网络错误", Toast.LENGTH_LONG).show();

                    }
                });
    }

    public void initData(){
        tv_city.setText(Constant.nowCity);
    }
    private void updateListHeight(){
        int totalHeight = productRvAdapter.getItemCount()*450;
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = totalHeight;
        recyclerView.setLayoutParams(params);
       // recyclerView.scrollToPosition(productRvAdapter.getItemCount()-1);
    }

    private void initLunBo(List<AdModal> list) {
        LogUtils.d("initLunBo");

        if(list==null || list.size()==0){
            LogUtils.d("list.size()==0");
            Picasso.with(getContext())
                    .load(Constant.SERVER_FILE_HOST+"/files/images/moren/bannar.jpg")
                    .into(img_moren_bannar);
            return;
        }

        int k=0;
        //显示的图片
        images = new ArrayList<ImageView>();
        for(int i = 0; i < list.size(); i++){
            if(k==3)break;
            k++;
            final AdModal ad = list.get(i);
            LogUtils.d("显示图片："+ad.getImg_path());
            ImageView imageView = new ImageView(getActivity());
            //imageView.setBackgroundResource(imageIds[i]);
                Picasso.with(getContext())
                        .load(Constant.SERVER_FILE_HOST+ad.getImg_path())
                        .into(imageView);


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   addAdClickNum(ad);
                   // Intent i = new Intent(mContext,StoreHomeActivity.class);
                  //  i.putExtra("store_id",ad.getStore_id());
                  //  startActivity(i);

                }
            });
            images.add(imageView);
        }

        if(list.size()==2){
            dots.add(dot_one);
            dots.add(dot_two);
            dot_one.setVisibility(View.VISIBLE);
            dot_two.setVisibility(View.VISIBLE);
        }else if(list.size()>=3){
            dots.add(dot_one);
            dots.add(dot_two);
            dots.add(dot_three);
            dot_one.setVisibility(View.VISIBLE);
            dot_two.setVisibility(View.VISIBLE);
            dot_three.setVisibility(View.VISIBLE);
        }

        LogUtils.d("dots size:"+dots.size());
        if(adapter==null){
            adapter = new ViewPagerAdapter();
            mViewPaper.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }


    }


    private Handler mHandler = new Handler(){

        public void handleMessage(android.os.Message msg) {
            mViewPaper.setCurrentItem(currentItem);
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_baike:
                startActivity(new Intent(getActivity(), BaiKeActivity.class));
                break;
            case R.id.rl_menu1:
                Intent intent1 = new Intent(getActivity(), ProductListActivity.class);
                intent1.putExtra("product_type",Constant.MENU_CHANGFANG);
                intent1.putExtra("title","厂房");
                startActivity(intent1);
                break;
            case R.id.rl_menu2:
                Intent intent2 = new Intent(getActivity(), ProductListActivity.class);
                intent2.putExtra("product_type",Constant.MENU_CHANGKU);
                intent2.putExtra("title","仓库");
                startActivity(intent2);
                break;
            case R.id.rl_menu3:
                Intent intent3 = new Intent(getActivity(), ProductListActivity.class);
                intent3.putExtra("product_type",Constant.MENU_TUDI);
                intent3.putExtra("title","土地");
                startActivity(intent3);
                break;
            case R.id.rl_menu4:
                Intent intent4=new Intent(getActivity(), YuanQuZhaoShangActivity.class);
                intent4.putExtra("product_type",Constant.MENU_YUANQUZHAOSHANG);
                startActivity(intent4);
                break;
            case R.id.rl_menu5:
                Intent intent5=new Intent(getActivity(), QiuZuActivity.class);
                intent5.putExtra("product_type",Constant.MENU_QIUZUQIUGOU);
                startActivity(intent5);
                break;
            case R.id.rl_menu6:
                Intent intent6=new Intent(getActivity(), JingJiRenActivity.class);
                intent6.putExtra("product_type",Constant.MENU_JINGJIREN);
                startActivity(intent6);
                break;
            case  R.id.rl_menu7:
                Intent intent7=new Intent(getActivity(), UserZhaoFangActivity.class);
                intent7.putExtra("product_type",Constant.MENU_BANGWOXUANZHI);
                startActivity(intent7);
                break;
            case R.id.rl_menu8:
                Intent intent8=new Intent(getActivity(), XinWenActivity.class);
                intent8.putExtra("product_type",Constant.MENU_XINWEN);
                startActivity(intent8);
                break;
        }
    }

    /**
     * 图片轮播任务
     * @author liuyazhuang
     *
     */
    private class ViewPageTask implements Runnable {

        @Override
        public void run() {
            currentItem = (currentItem + 1) % imageIds.length;
            mHandler.sendEmptyMessage(0);
        }
    }

    /**
     * 自定义Adapter
     * @author liuyazhuang
     *
     */
    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            // TODO Auto-generated method stub
//          super.destroyItem(container, position, object);
//          view.removeView(view.getChildAt(position));
//          view.removeViewAt(position);
            view.removeView(images.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            // TODO Auto-generated method stub
            view.addView(images.get(position));
            return images.get(position);
        }

    }




    @Override
    public void onStart() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        super.onStart();
        LogUtils.d("首页onstart");
        recyclerView.scrollToPosition(0);

    }

    @Override
    public void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        super.onPause();
    }

    @Override
    public void onStop() {
        LogUtils.d("首页onStop");
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        LogUtils.d("首页onResume");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
        super.onDestroy();
    }

    public void updateCity(){
        if(Constant.nowLocationAddress==null)return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogUtils.d("修改首页地址:"+Constant.nowCity);
                tv_city.setText(Constant.nowCity);
            }
        });


    }


}
