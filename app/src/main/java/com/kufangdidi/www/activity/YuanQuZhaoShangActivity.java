package com.kufangdidi.www.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.kufangdidi.www.R;
import com.kufangdidi.www.adapter.YuanQuZhaoShangAdapter;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.bean.YuanQuZhaoShangBean;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.lzy.okgo.OkGo.getContext;

public class YuanQuZhaoShangActivity extends AppCompatActivity {
    private int pageNum = 1;
    private RecyclerView my_recyclerView;
    private YuanQuZhaoShangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuanquzhaoshang);
        BaseApplication.addDestoryActivity(this,"YuanQuZhaoShangActivity");
        my_recyclerView=findViewById(R.id.yqzs_recyclerview);
        initTitle();
        myYuanQuZhaoShangList();
    }

    private void initTitle() {
        RelativeLayout common_top_back = findViewById(R.id.common_top_back);
        TextView common_top_title = findViewById(R.id.common_top_title);
        common_top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        common_top_title.setText("园区招商");
    }

    private void myYuanQuZhaoShangList() {
        OkGo.post(Constant.SERVER_HOST + "/parkStore/pageParkStore")
                .tag(this)
                .params("pageNum",pageNum )
                .params("pageSize",50 )
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("myYuanQuZhaoShangList 返回结果s" + s);
                        com.alibaba.fastjson.JSONObject jsonObject =
                                com.alibaba.fastjson.JSONObject.parseObject(s);
                        if (jsonObject.getInteger("code") != 0) return;
                        YuanQuZhaoShangBean yuanQuBean = JSONObject.parseObject(s, YuanQuZhaoShangBean.class);
                        List<YuanQuZhaoShangBean.Data.Content> content = yuanQuBean.getData().getContent();
                        LogUtils.d("content2 size:" + content.size());
                        List<YuanQuZhaoShangBean.Data.Content> arraylist = new ArrayList<>();
                        for (YuanQuZhaoShangBean.Data.Content con : content) {
                            LogUtils.d(con.getCreateTime() + " " + con.getParkAddress() + " " );
                            arraylist.add(con);
                        }
                        if (adapter == null) {
                            adapter = new YuanQuZhaoShangAdapter(getContext());
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
