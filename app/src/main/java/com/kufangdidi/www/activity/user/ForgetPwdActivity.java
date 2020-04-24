package com.kufangdidi.www.activity.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.JsonObject;
import com.kufangdidi.www.R;
import com.kufangdidi.www.app.Api;
import com.kufangdidi.www.app.BaseApplication;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class ForgetPwdActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText etPhone;
    private EditText etVerifyCode;
    private EditText etPassword;
    private EditText etPasswordCheck;
    private Button btnGetVerifyCode;
    private long clickTime = 0;
    private int time = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        BaseApplication.addDestoryActivity(this, "ForgetPwdActivity");
        initView();
    }

    private void initView() {
        etPhone = findViewById(R.id.et_phone);
        etVerifyCode = findViewById(R.id.et_verify_code);
        etPassword = findViewById(R.id.et_password);
        etPasswordCheck = findViewById(R.id.et_password_check);
        btnGetVerifyCode = findViewById(R.id.btn_get_verify_code);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (time > 0) {
                        btnGetVerifyCode.setClickable(false);
                        btnGetVerifyCode.setText(time + "s后重新获取");
                        handler.sendEmptyMessageDelayed(0, 1000);
                        time--;
                    } else {
                        btnGetVerifyCode.setClickable(true);
                        btnGetVerifyCode.setText("重新获取验证码");
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        HashMap<String, String> params;
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_confirm:
                long now = System.currentTimeMillis();
                if (clickTime > 0 && (now - clickTime) < (1000 * 2)) {
                    Toast.makeText(ForgetPwdActivity.this, "操作太频繁", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(etPassword.getText().toString())) {
                    Toast.makeText(ForgetPwdActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etPassword.getText().toString().equals(etPasswordCheck.getText().toString())) {
                    params = new HashMap<>();
                    params.put("userPhone", etPhone.getText().toString().trim());
                    params.put("code", etVerifyCode.getText().toString().trim());
                    params.put("passWord", etPassword.getText().toString().trim());
                    OkGo.post(Api.FORGET_PASSWORD).params(params)
                            .tag(this).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.i("response:", s);
                            JsonObject jsonObject = GsonUtils.fromJson(s, JsonObject.class);
                            Toast.makeText(ForgetPwdActivity.this,
                                    jsonObject.get("msg").getAsString(), Toast.LENGTH_LONG).show();
                            if (jsonObject.get("code").getAsInt() == 0) {
                                finish();
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            //Log.i("response:", e.getMessage());
                            Toast.makeText(ForgetPwdActivity.this, "请检查网络或遇到未知错误",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "两次输入的密码不相同", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_get_verify_code:
                if ("".equals(etPhone.getText().toString())) {
                    Toast.makeText(ForgetPwdActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                btnGetVerifyCode.setClickable(false);
                time = 60;
                handler.sendEmptyMessage(0);
                params = new HashMap<>();
                params.put("userPhone", etPhone.getText().toString().trim());
                params.put("type", "3");
                OkGo.post(Api.GET_CODE_URL).params(params)
                        .tag(this).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i("response:", s);
                        JsonObject jsonObject = GsonUtils.fromJson(s, JsonObject.class);
                        if (jsonObject.get("code").getAsInt() != 0) {
                            btnGetVerifyCode.setClickable(true);
                            btnGetVerifyCode.setText("重新获取验证码");
                            Toast.makeText(ForgetPwdActivity.this,
                                    jsonObject.get("msg").getAsString(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        LogUtils.d("onError:" + e.getLocalizedMessage());
                        btnGetVerifyCode.setClickable(true);
                        btnGetVerifyCode.setText("重新获取验证码");
                        Toast.makeText(ForgetPwdActivity.this, "请检查网络或遇到未知错误",
                                Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }
}
