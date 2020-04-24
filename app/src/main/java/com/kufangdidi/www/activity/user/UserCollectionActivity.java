package com.kufangdidi.www.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.kufangdidi.www.R;
import com.kufangdidi.www.activity.product.ProductHomeActivity;
import com.kufangdidi.www.adapter.ProductRvAdapter;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.bean.ProductBean;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class UserCollectionActivity extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private RelativeLayout common_top_back;
    private TextView common_top_title;
    private int page = 1;
    private int pageSize = 10;

    private List<ProductBean.DataBean.ContentBean> data;
    private ProductRvAdapter productRvAdapter;
    private SwipeRefreshLayout swl;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_collection);
        BaseApplication.addDestoryActivity(this, "UserCollectionActivity");
        initTitle();

        getData(false);
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
        common_top_title.setText("我的收藏");
        swl = findViewById(R.id.swl);
        swl.setOnRefreshListener(this);

        recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        productRvAdapter = new ProductRvAdapter(data);
        productRvAdapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.setAdapter(productRvAdapter);
        productRvAdapter.setOnItemClickListener((adapter, view, position) -> {
            ProductBean.DataBean.ContentBean item =
                    (ProductBean.DataBean.ContentBean) adapter.getItem(position);
            Intent intent = new Intent(UserCollectionActivity.this, ProductHomeActivity.class);
            intent.putExtra("product_id", item.getProductId());
            startActivity(intent);
        });
    }

    private void getData( boolean isLoadMore) {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageSize", pageSize + "");
        params.put("pageNum", page + "");
        params.put("token", BaseApplication.getSpUtils().getString("token"));
        params.put("uid", BaseApplication.getSpUtils().getInt("uid") + "");
        OkGo.post(Constant.SERVER_HOST + "/collectionInformation/pageCollectionInformation").params(params).tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("收藏分页列表" + s);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        JSONArray jsonArray  = jsonData.getJSONArray("content");
                        List<ProductBean.DataBean.ContentBean> list = new ArrayList<>();
                        for(int i=0;i<jsonArray.size();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("fplHouseList");
                            for(int a=0;a<jsonArray1.size();a++){
                                ProductBean.DataBean.ContentBean modal = new Gson().fromJson(jsonArray1.get(a).toString(), ProductBean.DataBean.ContentBean.class);
                                list.add(modal);
                            }
                        }
                        LogUtils.d("data size:"+list.size());
                        if (isLoadMore) {
                            productRvAdapter.loadMoreComplete();
                            if (list.size() == 0) {
                                productRvAdapter.loadMoreEnd();
                            } else {
                                data.addAll(list);
                                productRvAdapter.notifyDataSetChanged();
                            }
                        } else {
                            data = list;
                            productRvAdapter.setNewData(data);
                        }

                        if (isLoadMore) {
                            swl.setEnabled(true);
                        } else {
                            productRvAdapter.setEnableLoadMore(true);
                        }


//                        if (jsonObject.getInteger("code") == 0) {
//                            ProductBean productBean = JSONObject.parseObject(s, ProductBean.class);
//                            List<ProductBean.DataBean.ContentBean> content =
//                                    productBean.getData().getContent();
//                            if (isLoadMore) {
//                                productRvAdapter.loadMoreComplete();
//                                if (content.size() == 0) {
//                                    productRvAdapter.loadMoreEnd();
//                                } else {
//                                    data.addAll(content);
//                                    productRvAdapter.notifyDataSetChanged();
//                                }
//                            } else {
//                                data = content;
//                                productRvAdapter.setNewData(data);
//                            }
//                        } else {
//                            Toast.makeText(UserCollectionActivity.this, jsonObject.getString("msg"),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                        if (isLoadMore) {
//                            swl.setEnabled(true);
//                        } else {
//                            productRvAdapter.setEnableLoadMore(true);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Call call, Response response, Exception e) {
//                        super.onError(call, response, e);
//                        Toast.makeText(UserCollectionActivity.this, "网络错误", Toast.LENGTH_LONG).show();
//                        if (isLoadMore) {
//                            swl.setEnabled(true);
//                        } else {
//                            productRvAdapter.setEnableLoadMore(true);
//                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        page = 1;
        productRvAdapter.setEnableLoadMore(false);
        getData(false);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        swl.setEnabled(false);
        getData(true);
    }
}
