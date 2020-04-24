package com.kufangdidi.www.activity.baike;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kufangdidi.www.R;
import com.kufangdidi.www.app.Api;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.fragment.FragmentMe;
import com.kufangdidi.www.utils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

public class BaiKeHuiDaActivity extends AppCompatActivity {
    private RelativeLayout common_top_back;
    private TextView common_top_title;
    private EditText my_answer;
    private Button answer_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.addDestoryActivity(this, "BaiKeHuiDaActivity");
        setContentView(R.layout.activity_baike_huida);
        initTitle();
        initView();
    }

    private void initView() {
        my_answer = findViewById(R.id.my_answer);
        answer_button = findViewById(R.id.answer_button);
        answer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (my_answer.getText().length() == 0) {
                    Toast.makeText(BaiKeHuiDaActivity.this, "请输入您的回答", Toast.LENGTH_SHORT).show();
                    return;
                }
                Integer elid = (Integer) getIntent().getSerializableExtra("elid");
                huiDa(elid);
            }
        });
    }

    private void huiDa(Integer elid) {
        LogUtils.d("createName:" + FragmentMe.tv_name.getText().toString());
        OkGo.post(Api.BASE_URL + "/encyclopediaAnswer/addEncyclopediaAnswer")
                .tag(this)
                .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .params("elid", elid)
                .params("content", my_answer.getText().toString())
                .params("createName", FragmentMe.tv_name.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("我的回答 返回结果s" + s);
                        JSONObject jsonObject = JSON.parseObject(s);
                        if (jsonObject.getInteger("code") == 0) {
                            Toast.makeText(BaiKeHuiDaActivity.this, jsonObject.getString(
                                    "msg"),
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(BaiKeHuiDaActivity.this,
                                    jsonObject.getString(
                                            "msg") + jsonObject.getInteger("code"),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(BaiKeHuiDaActivity.this, "请检查网络或遇到未知错误！"
                                , Toast.LENGTH_LONG).show();
                    }
                });
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
        common_top_title.setText("我要回答");
    }

}
