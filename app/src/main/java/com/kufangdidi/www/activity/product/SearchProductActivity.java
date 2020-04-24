package com.kufangdidi.www.activity.product;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kufangdidi.www.R;
import com.kufangdidi.www.adapter.RecyclerviewMoHuAdapter;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.bean.HouseBean;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.mingle.widget.LoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

import static com.lzy.okgo.OkGo.getContext;


public class SearchProductActivity extends Activity {

    private EditText search_key;
    InputMethodManager imm;
    private String keyStr = "";

    LoadingView loadingView;
    private RelativeLayout common_top_back;
    private RecyclerviewMoHuAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        BaseApplication.addDestoryActivity(this, "SearchProductActivity");
        loadingView = (LoadingView) findViewById(R.id.loadView);
        recyclerView = findViewById(R.id.rv_list);
        common_top_back = findViewById(R.id.common_top_back);
        common_top_back.setOnClickListener(v -> finish());

        RelativeLayout submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyStr = search_key.getText().toString();
                if ("".equals(keyStr)) return;
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                try {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken()
                            , InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (NullPointerException e) {
                }

                loadingView.setVisibility(View.VISIBLE);
                searchKey(keyStr);
            }
        });

        //初始化搜索框
        search_key = findViewById(R.id.search_key);
        search_key.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        //打开软键盘
        try {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e) {

        }

        search_key.setEnabled(true);
        search_key.setFocusable(true);
        search_key.setFocusableInTouchMode(true);
        search_key.requestFocus();


    }

    private void searchKey(String keyStr) {
        LogUtils.d("进入模糊搜索");
        OkGo.post(Constant.SERVER_HOST + "/fplHouse/queryFplHousePOByTitle\n")
                .tag(this)
                .params("title", keyStr)
//                .params("token", BaseApplication.getSpUtils().getString("token"))
//                .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                .params("pageSize", 50)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("模糊搜索 返回结果s:" + s);
                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(s);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (jsonObject == null) return;
                        HouseBean.DataBean dataBean = null;
                        List<HouseBean.DataBean> list = new ArrayList<>();
                        try {
                            JSONArray jsonData = jsonObject.getJSONArray("data");
                            LogUtils.d("data size:" + jsonData.length());
                            if (jsonData.length() == 0) {
                                TimerTask task = new TimerTask() {
                                    @Override
                                    public void run() {
                                        //要执行的操作

                                        loadingView.setVisibility(View.INVISIBLE);
                                    }
                                };
                                Timer timer = new Timer();
                                timer.schedule(task, 1000);
                                Toast.makeText(SearchProductActivity.this, "亲，没找到数据哦！",
                                        Toast.LENGTH_LONG).show();
                            }
                            for (int i = 0; i < jsonData.length(); i++) {
                                dataBean = new Gson().fromJson(jsonData.getString(i),
                                        HouseBean.DataBean.class);
                                LogUtils.d(" menu1 title:" + dataBean.getContact());
                                list.add(dataBean);
                                //  list.add(new Gson().fromJson(jsonArray.getString(i),AdModel
                                //  .class));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (adapter == null) {
                            adapter = new RecyclerviewMoHuAdapter(getContext(), list);
                            adapter.setData(list);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.setData(list);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
