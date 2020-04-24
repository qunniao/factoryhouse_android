package com.kufangdidi.www.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.kufangdidi.www.R;
import com.kufangdidi.www.adapter.JingJiRenAdapter;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.bean.RegionChoosedBean;
import com.kufangdidi.www.modal.JingJiRenModal;
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

public class JingJiRenActivity extends AppCompatActivity {
    private int mQuYu = 0;
    private int mPaiXu = 0;
    private int pageNum = 1;
    private JingJiRenAdapter adapter;
    private RecyclerView recyclerView;

    private LinearLayout ll_quyu;
    private TextView tv_city;

    //区域字段
    private String province_name = "浙江省";
    private String city_name = "杭州市";
    private String county_name = "全部";
    private AddressPickerDialog dialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jingjiren);
        BaseApplication.addDestoryActivity(this, "JingJiRenActivity");
        recyclerView = findViewById(R.id.recycle_jingjiren);
        initTitle();
        theme();
        findJingJiRenList();
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
        common_top_title.setText("经纪人");
    }

    public void onAddressDialog() {
        dialog = new AddressPickerDialog(JingJiRenActivity.this, "弹出框标题", province_name,
                city_name, county_name, new AddressPickerDialog.onAreaPickerDialogClickListener() {
            @Override
            public void onChooseClick(RegionChoosedBean bean) {
                clickQuYu();
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
                findJingJiRenList();
            }
        });

        dialog.show();
    }


    private void theme() {
        tv_city = findViewById(R.id.tv_city);
        LinearLayout quYu = (LinearLayout) findViewById(R.id.quyu);
        LinearLayout paiXu = (LinearLayout) findViewById(R.id.paixu);
        quYu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickQuYu();
                if (mPaiXu % 2 == 1) {
                    clickPaiXu();
                }
                onAddressDialog();
            }
        });
        paiXu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPaiXu();
                myClick();
                if (mQuYu % 2 == 1) {
                    clickQuYu();
                }
            }
        });
    }

    private void clickQuYu() {
        mQuYu += 1;
        if (mQuYu % 2 == 1) {
            ((TextView) findViewById(R.id.tv_city)).setTextColor(getResources().getColor(R.color.colorPink));
            (findViewById(R.id.quyu2)).setBackground(getResources().getDrawable(R.mipmap.icn_xiajiantou_pink));
        } else {
            ((TextView) findViewById(R.id.tv_city)).setTextColor(getResources().getColor(R.color.colorBlack));
            (findViewById(R.id.quyu2)).setBackground(getResources().getDrawable(R.mipmap.icn_xiajiantou));
        }
    }

    private void clickPaiXu() {
        mPaiXu += 1;
        if (mPaiXu % 2 == 1) {
            ((TextView) findViewById(R.id.paixu1)).setTextColor(getResources().getColor(R.color.colorPink));
            (findViewById(R.id.paixu2)).setBackground(getResources().getDrawable(R.mipmap.icn_xiajiantou_pink));
            (findViewById(R.id.paixu_layout)).setVisibility(View.VISIBLE);
        } else {
            ((TextView) findViewById(R.id.paixu1)).setTextColor(getResources().getColor(R.color.colorBlack));
            (findViewById(R.id.paixu2)).setBackground(getResources().getDrawable(R.mipmap.icn_xiajiantou));
            (findViewById(R.id.paixu_layout)).setVisibility(View.INVISIBLE);
        }
    }

    private void myClick() {
        LinearLayout shiJian = findViewById(R.id.shijian);
        LinearLayout renQi = findViewById(R.id.renqi);
        TextView kongbai = findViewById(R.id.kongbai);
        shiJian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) findViewById(R.id.shijian1)).setTextColor(getResources().getColor(R.color.colorPink));
                (findViewById(R.id.shijian2)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.renqi1)).setTextColor(getResources().getColor(R.color.colorShenGray));
                (findViewById(R.id.renqi2)).setVisibility(View.INVISIBLE);
                clickPaiXu();
            }
        });
        renQi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) findViewById(R.id.renqi1)).setTextColor(getResources().getColor(R.color.colorPink));
                (findViewById(R.id.renqi2)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.shijian1)).setTextColor(getResources().getColor(R.color.colorShenGray));
                (findViewById(R.id.shijian2)).setVisibility(View.INVISIBLE);
                clickPaiXu();
            }
        });
        kongbai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPaiXu();
            }
        });
    }

    private void findJingJiRenList() {
        LogUtils.d("进入findJingJiRenList");
        String mainArea = null;
        if (!county_name.equals("全部")) {
            mainArea = county_name ;
        }
        LogUtils.d("mainArea"+mainArea);
        OkGo.post(Constant.SERVER_HOST + "/personal/brokerList")
                .tag(this)
                .params("pageNum", pageNum)
                .params("pageSize", 50)
                .params("type", 2)
                .params("mainArea", mainArea)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("findJingJiRenList 返回结果s:" + s);
                        JSONObject jsonObject = null;
                        JSONObject jsonData = null;
                        try {
                            jsonObject = new JSONObject(s);
                            jsonData = jsonObject.getJSONObject("data");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (jsonObject == null) return;
                        JingJiRenModal modal = null;
                        List<JingJiRenModal> list2 = new ArrayList<>();
                        try {
                            JSONArray jsonArray = jsonData.getJSONArray("content");
                            LogUtils.d("data size:" + jsonArray.length());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                modal = new Gson().fromJson(jsonArray.getString(i),
                                        JingJiRenModal.class);
                                LogUtils.d("getUserName:" + modal.getUserName());
                                list2.add(modal);
                                //  list.add(new Gson().fromJson(jsonArray.getString(i),AdModel
                                //  .class));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (adapter == null) {
                            adapter = new JingJiRenAdapter(getContext());
                            adapter.setdata(list2);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.setdata(list2);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        LogUtils.d("返回结果出错:" + e.getMessage());
                    }
                });
    }
}
