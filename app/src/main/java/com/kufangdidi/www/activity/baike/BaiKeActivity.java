package com.kufangdidi.www.activity.baike;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.kufangdidi.www.R;
import com.kufangdidi.www.adapter.BaiKeAdapter;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.bean.BaiKeBean;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.lzy.okgo.OkGo.getContext;

public class BaiKeActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout common_top_back,bk_shousuo_button;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<String> mTitle;
    private ArrayList<Fragment> mFragment;

    private BaiKeAdapter adapter;
    private RecyclerView my_recyclerView;
    private TextView bk_jingying1,bk_rongzi1,bk_fayuan1,bk_zhuangxiu1,bk_jiaoyi1;
    private EditText bk_shousuo;

    private TextView tiwen;
    private int type=1,pageNum=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baike);
        BaseApplication.addDestoryActivity(this,"BaiKeActivity");
        initTitle();
        initView();
        xuanZhong();
        myBaiKeList();


        my_recyclerView=findViewById(R.id.bk_recycler);
/*
* 最新发布和精华发帖
* */
//        mTabLayout = (TabLayout)findViewById(R.id.mTabLayout);
//        mViewPager = (ViewPager)findViewById(R.id.mViewPager);
//        mTitle =new ArrayList<>();
//        mTitle.add("最新发布");
//        mTitle.add("精华热帖");
//        mFragment =new ArrayList<>();
//        mFragment.add(new FragmentBaiKeListView());
//        mFragment.add(new FragmentBaiKeListView());
//        mViewPager.setOffscreenPageLimit(mFragment.size());
//        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                return mFragment.get(position);
//            }
//
//            @Override
//            public int getCount() {
//                return mFragment.size();
//            }
//            @Override
//            public CharSequence getPageTitle(int position){
//                return mTitle.get(position);
//            }
//        });
//        mTabLayout.setupWithViewPager(mViewPager);
    }
    @Override
    protected void onStart(){
        super.onStart();
        xuanZhong();
        myBaiKeList();
    }

    private void initTitle() {
        common_top_back = findViewById(R.id.common_top_back);
        common_top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initView(){
        bk_jingying1 = findViewById(R.id.bk_jingying1);
        bk_rongzi1 = findViewById(R.id.bk_rongzi1);
        bk_fayuan1 = findViewById(R.id.bk_fayuan1);
        bk_zhuangxiu1 = findViewById(R.id.bk_zhuangxiu1);
        bk_jiaoyi1 = findViewById(R.id.bk_jiaoyi1);
        bk_shousuo = findViewById(R.id.bk_shousuo);
        bk_shousuo_button = findViewById(R.id.bk_shousuo_button);
        bk_shousuo_button.setOnClickListener(this);
        tiwen = findViewById(R.id.tiwen);
        tiwen.setOnClickListener(this);
    }

    private void xuanZhong(){
        if (type==1){
            bk_jingying1.setTextColor(getResources().getColor(R.color.colorPink));
        }else {
            bk_jingying1.setTextColor(getResources().getColor(R.color.colorBlack));
        }
        if (type==2){
            bk_rongzi1.setTextColor(getResources().getColor(R.color.colorPink));
        }else {
            bk_rongzi1.setTextColor(getResources().getColor(R.color.colorBlack));
        }
        if (type==3){
            bk_fayuan1.setTextColor(getResources().getColor(R.color.colorPink));
        }else {
            bk_fayuan1.setTextColor(getResources().getColor(R.color.colorBlack));
        }
        if (type==4){
            bk_zhuangxiu1.setTextColor(getResources().getColor(R.color.colorPink));
        }else {
            bk_zhuangxiu1.setTextColor(getResources().getColor(R.color.colorBlack));
        }
        if (type==5){
            bk_jiaoyi1.setTextColor(getResources().getColor(R.color.colorPink));
        }else {
            bk_jiaoyi1.setTextColor(getResources().getColor(R.color.colorBlack));
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tiwen:
                startActivity(new Intent(this,BaiKeTiWenActivity.class));
                break;
            case R.id.bk_jingying:
                type=1;
                break;
            case R.id.bk_rongzi:
                type=2;
                break;
            case R.id.bk_fayuan:
                type=3;
                break;
            case R.id.bk_zhuangxiu:
                type=4;
                break;
            case R.id.bk_jiaoyi:
                type=5;
                break;
            case R.id.bk_shousuo_button:
                break;
        }
        LogUtils.d("type:"+type);
        xuanZhong();
        myBaiKeList();
    }

    private void myBaiKeList() {
        OkGo.post(Constant.SERVER_HOST + "/encyclopedia/pageEncyclopedia")
                .tag(this)
                .params("pageNum",pageNum )
                .params("pageSize",50 )
                .params("type",type)
                .params("title",bk_shousuo.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("myBaiKeList 返回结果s" + s);
                        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(s);
                        if (jsonObject.getInteger("code") != 0) return;
                        BaiKeBean baiKeBean = JSONObject.parseObject(s, BaiKeBean.class);
                        List<BaiKeBean.Data.Content> content = baiKeBean.getData().getContent();
                        LogUtils.d("content size:" + content.size());
                        List<BaiKeBean.Data.Content> arraylist = new ArrayList<>();
                        for (BaiKeBean.Data.Content con : content) {
                            LogUtils.d(con.getCreateTime() + " " + con.getCreateName() + " " );
                            arraylist.add(con);
                        }
                        if (adapter == null) {
                            adapter = new BaiKeAdapter(getContext());
                            adapter.setdata(arraylist);
                            my_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            my_recyclerView.setAdapter(adapter);
                        } else {
                            adapter.setdata(arraylist);
                            adapter.notifyDataSetChanged();
                            my_recyclerView.setAdapter(adapter);
                        }
                    }
                });
    }
}
