package com.kufangdidi.www.activity.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.kufangdidi.www.R;
import com.kufangdidi.www.adapter.ChuZuAdapter;
import com.kufangdidi.www.adapter.QiuZuAdapter;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.bean.HouseBean;
import com.kufangdidi.www.bean.HouseQiuZuBean;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.lzy.okgo.OkGo.getContext;

public class UserFaBuActivity extends AppCompatActivity {
    private RelativeLayout common_top_back;
    private TextView common_top_title;
    private LinearLayout my_chuzu, my_qiuzu;
    private View my_chuzu_view, my_qiuzu_view;
    private RecyclerView my_recyclerView;
    private boolean bool1 = true, bool2 = false;
    private ChuZuAdapter adapter;
    private QiuZuAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fabu);
        BaseApplication.addDestoryActivity(this,"UserFaBuActivity");
        initTitle();
        initView();
//        initData();
        boolChange();
        onclick();
        myChuzuList();
    }

    private void initTitle() {
        common_top_back = findViewById(R.id.common_top_back);
        common_top_title = findViewById(R.id.common_top_title);
        common_top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        common_top_title.setText("我的发布");
    }

    private void initView() {
        my_chuzu = findViewById(R.id.my_chuzu);
        my_qiuzu = findViewById(R.id.my_qiuzu);
        my_chuzu_view = findViewById(R.id.my_chuzu_view);
        my_qiuzu_view = findViewById(R.id.my_qiuzu_view);
        my_recyclerView = findViewById(R.id.my_recycler);
    }

    private void boolChange() {
        if (bool1) {
            my_chuzu_view.setVisibility(View.VISIBLE);
        } else {
            my_chuzu_view.setVisibility(View.INVISIBLE);
        }
        if (bool2) {
            my_qiuzu_view.setVisibility(View.VISIBLE);
        } else {
            my_qiuzu_view.setVisibility(View.INVISIBLE);
        }
    }

    private void onclick() {
        my_chuzu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bool1 = true;
                bool2 = false;
                boolChange();
                myChuzuList();
            }
        });
        my_qiuzu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bool2 = true;
                bool1 = false;
                boolChange();
                myQiuzuList();
            }
        });
    }

    private void myChuzuList() {
        OkGo.post(Constant.SERVER_HOST + "/fplHouse/queryFplHouseByUid")
                .tag(this)
                .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("myChuzuList 返回结果s" + s);
                        JSONObject jsonObject =
                                JSONObject.parseObject(s);
                        if (jsonObject.getInteger("code") != 0) return;

                        HouseBean houseBean = JSONObject.parseObject(s, HouseBean.class);
                        List<HouseBean.DataBean> content = houseBean.getData();
                        LogUtils.d("content size:" + content.size());
                        List<HouseBean.DataBean> arraylist = new ArrayList<>();
                        for (HouseBean.DataBean con : content) {
                            LogUtils.d(con.getTitle() + " " + con.getProductId() + " " + con.getTypes());
                            arraylist.add(con);
                        }
                        if (adapter == null) {
                            adapter = new ChuZuAdapter(getContext());
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

    private void myQiuzuList() {
        OkGo.post(Constant.SERVER_HOST + "/buyingPurchase/queryBuyingPurchaseByUid")
                .tag(this)
                .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("myQiuzuList 返回结果s" + s);
                        JSONObject jsonObject2 =
                                JSONObject.parseObject(s);
                        if (jsonObject2.getInteger("code") != 0) return;
                        HouseQiuZuBean houseBean2 = JSONObject.parseObject(s, HouseQiuZuBean.class);
                        List<HouseQiuZuBean.DataBean> content2 = houseBean2.getData();
                        LogUtils.d("content2 size:" + content2.size());
                        List<HouseQiuZuBean.DataBean> arraylist2 = new ArrayList<>();
                        for (HouseQiuZuBean.DataBean con : content2) {
                            LogUtils.d(con.getTitle() + " " + con.getProductId() + " " + con.getType());
                            arraylist2.add(con);
                        }
                        if (adapter2 == null) {
                            adapter2 = new QiuZuAdapter(getContext());
                            adapter2.setdata(arraylist2);
                            my_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            my_recyclerView.setAdapter(adapter2);
                        } else {
                            adapter2.setdata(arraylist2);
                            adapter2.notifyDataSetChanged();
                            my_recyclerView.setAdapter(adapter2);
                        }
                    }
                });
    }
}
