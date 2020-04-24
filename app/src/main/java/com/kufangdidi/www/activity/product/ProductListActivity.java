package com.kufangdidi.www.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kufangdidi.www.R;
import com.kufangdidi.www.adapter.ProductRvAdapter;
import com.kufangdidi.www.app.Api;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.bean.ProductBean;
import com.kufangdidi.www.bean.RegionChoosedBean;
import com.kufangdidi.www.picker.AddressPickTask;
import com.kufangdidi.www.picker.AddressPickerDialog;
import com.kufangdidi.www.utils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import okhttp3.Call;
import okhttp3.Response;

public class ProductListActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private RelativeLayout common_top_back;
    private TextView common_top_title;
    private ImageView iv_search;
    private String title;

    private RecyclerView recyclerView;
    private int product_type;
    private int types = 1;
    private int page = 1;
    private int pageSize = 10;

    //区域筛选栏
    private LinearLayout ll_quyu, ll_shaixuan;
    private Button rb_chuzu, rb_chushou;
    private TextView tv_city;

    //区域字段
    private String province_name = "浙江省";
    private String city_name = "杭州市";
    private String county_name = "全部";
    private AddressPickerDialog dialog = null;


    //筛选字段
    private String money_start = "0";//价格开始区间
    private String money_end = "1000000";//价格结束区间
    private String money_unit;//价格单位
    private String area_start = "0";//面积开始区间
    private String area_end = "1000000";//面积结束区间
    private String structure="其他";//结构

    private ProductRvAdapter productRvAdapter;
    private SwipeRefreshLayout swl;
    private List<ProductBean.DataBean.ContentBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        BaseApplication.addDestoryActivity(this, "ProductListActivity");
        title = getIntent().getStringExtra("title");
        if (title == null) title = "库房滴滴";
        initTitle();
        initView();
        types = 1;
        page = 1;
        getData(false);
    }

    private void initView() {
        tv_city = findViewById(R.id.tv_city);
        ll_quyu = findViewById(R.id.ll_quyu);
        ll_quyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddressDialog();
            }
        });

        ll_shaixuan = findViewById(R.id.ll_shaixuan);
        ll_shaixuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, ShaiXuanActivity.class);
                startActivityForResult(intent, 5);
            }
        });

        rb_chuzu = findViewById(R.id.rb_chuzu);
        rb_chuzu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_chuzu.setSelected(true);
                rb_chushou.setSelected(false);
                types = 1;
                page = 1;
                getData(false);
            }
        });
        rb_chushou = findViewById(R.id.rb_chushou);
        rb_chushou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_chuzu.setSelected(false);
                rb_chushou.setSelected(true);
                types = 2;
                page = 1;
                getData(false);
            }
        });

        swl = findViewById(R.id.swl);
        swl.setOnRefreshListener(this);

        recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        product_type = getIntent().getIntExtra("product_type", 0);
        data = new ArrayList<>();
        productRvAdapter = new ProductRvAdapter(data);
        productRvAdapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.setAdapter(productRvAdapter);
        productRvAdapter.setOnItemClickListener((adapter, view, position) -> {
            ProductBean.DataBean.ContentBean item =
                    (ProductBean.DataBean.ContentBean) adapter.getItem(position);
            Intent intent = new Intent(ProductListActivity.this, ProductHomeActivity.class);
            intent.putExtra("product_id", item.getProductId());
            startActivity(intent);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d("resultCode: " + resultCode);
        if (resultCode == 5) {
            //从筛选页面返回
            LogUtils.d("从筛选页面返回");
            money_start = data.getStringExtra("money_start");
            money_end = data.getStringExtra("money_end");
            money_unit = data.getStringExtra("money_unit");
            area_start = data.getStringExtra("area_start");
            area_end = data.getStringExtra("area_end");
            structure = data.getStringExtra("structure");
            page = 1;
            getData(true);

        }
    }

    //区域选择
    public void onAddressPicker() {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                LogUtils.d("数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                LogUtils.d("选择城市：" + province.getAreaName() + " " + city.getAreaName());
                if (province.getAreaName().equals(province_name) && city.getAreaName().equals(city_name) && county.getAreaName().equals(county_name)) {
                    LogUtils.d("区域没有变化");
                    return;
                }
                province_name = province.getAreaName();
                city_name = city.getAreaName();
                county_name = county.getAreaName();

            }
        });
        task.execute(province_name, city_name, county_name);
    }

    public void onAddressDialog() {
        dialog = new AddressPickerDialog(ProductListActivity.this, "弹出框标题", province_name,
                city_name, county_name, new AddressPickerDialog.onAreaPickerDialogClickListener() {
            @Override
            public void onChooseClick(RegionChoosedBean bean) {
                //bean 为点击完成返回的数据对象
                if (bean.isEmpty()) {
                    LogUtils.d("未选择数据");
                } else {
                    LogUtils.d(bean.getpName() + "\t" + bean.getcName() + "\t" + bean.getaName());
                    province_name = bean.getpName();
                    city_name = bean.getcName();
                    county_name = bean.getaName();
                    tv_city.setText(city_name);
                    page = 1;
                    getData(true);

                }
                dialog.cancel();
            }
        });

        dialog.show();
    }

    //二类(0:初始,1:出租,2:出售)
    private void getData(boolean isLoadMore) {
        String region = "区";
        if (!county_name.equals("全部")) {
            region = county_name;
        }
        LogUtils.d("region:" + region);
        HashMap<String, String> params = new HashMap<>();
        params.put("pageSize", pageSize + "");
        params.put("pageNum", page + "");
        params.put("status", types + "");
        params.put("types", product_type + "");
        params.put("token", BaseApplication.getSpUtils().getString("token"));
        params.put("region", region);//  地区
        params.put("upArea", area_start);// 查询面积上限（反）
        params.put("downArea", area_end);// 查询面积下限（反）
        params.put("upRent", money_start);// 查询价格上限（反）
        params.put("downRent", money_end);// 查询价格下限（反）
        if (null != money_unit && !money_unit.equals("")) {
            params.put("rentUnit", money_unit);// 查询价格单位
        }
//        params.put("buildingStructure", structure);// 查询建筑结构
        OkGo.post(Api.SERCH_RENT_OR_SALE).params(params).tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        LogUtils.d("筛选结果" + s);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        if (jsonObject.getInteger("code") == 0) {
                            ProductBean productBean = JSONObject.parseObject(s,
                                    ProductBean.class);
                            List<ProductBean.DataBean.ContentBean> content =
                                    productBean.getData().getContent();
                            if (isLoadMore && page != 1) {

                                productRvAdapter.loadMoreComplete();
                                if (content.size() == 0) {
                                    productRvAdapter.loadMoreEnd();
                                } else {
                                    data.addAll(content);
                                    productRvAdapter.notifyDataSetChanged();
                                }
                            } else {
                                data = content;
                                productRvAdapter.setNewData(data);
                            }
                        } else {
                            Toast.makeText(ProductListActivity.this, jsonObject.getString(
                                    "msg"),
                                    Toast.LENGTH_LONG).show();
                        }
                        if (isLoadMore) {
                            swl.setEnabled(true);
                        } else {
                            productRvAdapter.setEnableLoadMore(true);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(ProductListActivity.this, "网络错误", Toast.LENGTH_LONG).show();
                        if (isLoadMore) {
                            swl.setEnabled(true);
                        } else {
                            productRvAdapter.setEnableLoadMore(true);
                        }
                    }
                });
    }

    private void initTitle() {
        common_top_back = findViewById(R.id.common_top_back);
        common_top_back.setOnClickListener(v -> finish());
        common_top_title = findViewById(R.id.common_top_title);
        common_top_title.setText(title);
        iv_search = findViewById(R.id.iv_search);
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this,
                        SearchProductActivity.class);
                startActivity(intent);
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
