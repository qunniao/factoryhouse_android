package com.kufangdidi.www.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.kufangdidi.www.R;
import com.kufangdidi.www.adapter.QiuZuQiuGouAdapter;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.bean.RegionChoosedBean;
import com.kufangdidi.www.modal.QiuZuQiuGouModal;
import com.kufangdidi.www.picker.AddressPickerDialog;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.lzy.okgo.OkGo.getContext;

public class QiuZuActivity extends AppCompatActivity {
    private RelativeLayout common_top_back;
    private TextView common_top_title;
    private LinearLayout quYu,leiXing,qiTa,qz_lx_buXian,qz_lx_qiuZu,qz_lx_qiuGou,qz_lx_jieKe,qz_lx_kongBai;
    private int mQuYu=0,mLeiXing=0,mQiTa=0,pageNum = 1;
    private boolean buXian,qiuZu,qiuGou,jieKe;
    private QiuZuQiuGouAdapter adapter;
    private RecyclerView recyclerView;

    //区域筛选栏
    private LinearLayout ll_quyu,ll_shaixuan;
    private TextView tv_city;

    //区域字段
    private String province_name = "浙江省";
    private String city_name = "杭州市";
    private String county_name = "西湖区";
    private AddressPickerDialog dialog = null;
    //筛选字段
    private String type;//分类


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qiuzu);
        BaseApplication.addDestoryActivity(this,"QiuZuActivity");
        recyclerView = findViewById(R.id.recycle_qiuzuqiugou);
        initTitle();
        theme();
        findQiuZuQiuGouList();
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
        common_top_title.setText("求租求购");
    }

    private void theme(){
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
                Intent intent = new Intent(QiuZuActivity.this, QiuZuShaiXuanActivity.class);
                startActivityForResult(intent, 5);
            }
        });
        leiXing=findViewById(R.id.qz_leixing);
        leiXing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLeiXing();
                myClick();
            }
        });

    }

    public void onAddressDialog(){
        dialog = new AddressPickerDialog(QiuZuActivity.this,"弹出框标题",province_name,city_name,county_name, new AddressPickerDialog.onAreaPickerDialogClickListener() {
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
                }
                dialog.cancel();
            }
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d("resultCode: "+resultCode);
        if(resultCode == 5){
            //从筛选页面返回
            LogUtils.d("从筛选页面返回");
            boolean submit = data.getBooleanExtra("submit",false);
            if(submit){
                type = data.getStringExtra("type");

            }

        }
    }


    private void clickLeiXing(){
        mLeiXing+=1;
        if(mLeiXing%2==1){
            ((TextView)findViewById(R.id.qz_leixing1)).setTextColor(getResources().getColor(R.color.colorPink));
            ((ImageView)findViewById(R.id.qz_leixing2)).setBackground(getResources().getDrawable(R.mipmap.icn_xiajiantou_pink));
            ((LinearLayout)findViewById(R.id.qz_leixing_layut)).setVisibility(View.VISIBLE);
        }
        else {
            ((TextView)findViewById(R.id.qz_leixing1)).setTextColor(getResources().getColor(R.color.colorBlack));
            ((ImageView)findViewById(R.id.qz_leixing2)).setBackground(getResources().getDrawable(R.mipmap.icn_xiajiantou));
            ((LinearLayout)findViewById(R.id.qz_leixing_layut)).setVisibility(View.INVISIBLE);
        }
    }


    private void myClick(){
        qz_lx_buXian =findViewById(R.id.qz_lx_buxian);
        qz_lx_qiuZu =findViewById(R.id.qz_lx_qiuzu);
        qz_lx_qiuGou =findViewById(R.id.qz_lx_qiugou);
        qz_lx_jieKe =findViewById(R.id.qz_lx_jieke);
        qz_lx_kongBai =findViewById(R.id.qz_lx_kongbai);
        qz_lx_buXian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLeiXing();
                buXian=true;
                qiuZu=qiuGou=jieKe=false;
                leiXing_menu();
            }
        });
        qz_lx_qiuZu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLeiXing();
                qiuZu=true;
                buXian=qiuGou=jieKe=false;
                leiXing_menu();
            }
        });
        qz_lx_qiuGou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLeiXing();
                qiuGou=true;
                buXian=qiuZu=jieKe=false;
                leiXing_menu();
            }
        });
        qz_lx_jieKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLeiXing();
                jieKe=true;
                buXian=qiuZu=qiuGou=false;
                leiXing_menu();
            }
        });
        qz_lx_kongBai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLeiXing();
            }
        });
    }

    protected void leiXing_menu() {
        if (buXian==true){
            ((TextView)findViewById(R.id.qz_lx_buxian1)).setTextColor(getResources().getColor(R.color.colorPink));
            (findViewById(R.id.qz_lx_buxian2)).setVisibility(View.VISIBLE);
        }
        else {
            ((TextView)findViewById(R.id.qz_lx_buxian1)).setTextColor(getResources().getColor(R.color.colorBlack));
            (findViewById(R.id.qz_lx_buxian2)).setVisibility(View.INVISIBLE);
        }
        if (qiuZu==true){
            ((TextView)findViewById(R.id.qz_lx_qiuzu1)).setTextColor(getResources().getColor(R.color.colorPink));
            (findViewById(R.id.qz_lx_qiuzu2)).setVisibility(View.VISIBLE);
        }
        else {
            ((TextView)findViewById(R.id.qz_lx_qiuzu1)).setTextColor(getResources().getColor(R.color.colorBlack));
            (findViewById(R.id.qz_lx_qiuzu2)).setVisibility(View.INVISIBLE);
        }
        if (qiuGou==true){
            ((TextView)findViewById(R.id.qz_lx_qiugou1)).setTextColor(getResources().getColor(R.color.colorPink));
            (findViewById(R.id.qz_lx_qiugou2)).setVisibility(View.VISIBLE);
        }
        else {
            ((TextView)findViewById(R.id.qz_lx_qiugou1)).setTextColor(getResources().getColor(R.color.colorBlack));
            (findViewById(R.id.qz_lx_qiugou2)).setVisibility(View.INVISIBLE);
        }
        if (jieKe==true){
            ((TextView)findViewById(R.id.qz_lx_jieke1)).setTextColor(getResources().getColor(R.color.colorPink));
            (findViewById(R.id.qz_lx_jieke2)).setVisibility(View.VISIBLE);
        }
        else {
            ((TextView)findViewById(R.id.qz_lx_jieke1)).setTextColor(getResources().getColor(R.color.colorBlack));
            (findViewById(R.id.qz_lx_jieke2)).setVisibility(View.INVISIBLE);
        }
    }

    private void findQiuZuQiuGouList() {
        LogUtils.d("进入QiuZuQiuGouList");
        OkGo.post(Constant.SERVER_HOST+"/buyingPurchase/pageBuyingPurchase")
                .tag(this)
                .params("pageNum", pageNum)
                .params("pageSize",  50)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("findQiuZuQiuGouList 返回结果s:"+s);
                        JSONObject jsonObject=null;
                        JSONObject jsonData = null;
                        try {
                            jsonObject = new JSONObject(s);
                            jsonData = jsonObject.getJSONObject("data");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(jsonObject==null)return;
                        if(jsonData==null)return;
                        QiuZuQiuGouModal modal = null;
                        List<QiuZuQiuGouModal> list = new ArrayList<>();
                        try {
                            JSONArray jsonArray = jsonData.getJSONArray("content");
                            LogUtils.d("data size:"+jsonArray.length());

                            for(int i=0;i<jsonArray.length();i++){
                                modal = new Gson().fromJson(jsonArray.getString(i), QiuZuQiuGouModal.class);
                                LogUtils.d(" menu1 title:"+modal.getContact());
                                list.add(modal);
                                //  list.add(new Gson().fromJson(jsonArray.getString(i),AdModel.class));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(adapter==null){
                            adapter = new QiuZuQiuGouAdapter(getContext());
                            adapter.setdata(list);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(adapter);
                        }else{
                            adapter.setdata(list);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        LogUtils.d("返回结果出错:"+e.getMessage());
                    }
                });
    }
}
