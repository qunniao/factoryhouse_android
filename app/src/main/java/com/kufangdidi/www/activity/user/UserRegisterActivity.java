package com.kufangdidi.www.activity.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.JsonObject;
import com.kufangdidi.www.R;
import com.kufangdidi.www.app.Api;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.utils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class UserRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPhone;
    private EditText etPwd;
    private EditText etCode;
    private Button btnGetCode;
    private int time = 0;
    private RadioGroup radioGroup;
    private int type;
    private String check_text;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (time > 0) {
                        btnGetCode.setClickable(false);
                        btnGetCode.setText(time + "s后重新获取");
                        handler.sendEmptyMessageDelayed(0, 1000);
                        time--;
                    } else {
                        btnGetCode.setClickable(true);
                        btnGetCode.setText("重新获取验证码");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        BaseApplication.addDestoryActivity(this, "UserRegisterActivity");
        initView();
    }

    private void initView() {
        etPhone = findViewById(R.id.et_phone);
        etPwd = findViewById(R.id.et_password);
        etCode = findViewById(R.id.edt_r_duan);
        btnGetCode = findViewById(R.id.btn_r_duan);
        radioGroup = findViewById(R.id.my_radiogroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            check_text = ((RadioButton) radioGroup.findViewById(checkedId)).getText().toString();
            LogUtils.d("check_text:" + check_text);
            if (check_text.equals("个人/企业")) {
                type = 1;
            }
            if (check_text .equals("中介") ) {
                type = 2;
            }
            if (check_text.equals("园区") ) {
                type = 3;
            }
//            type = ((RadioButton) radioGroup.findViewById(checkedId)).getId();
            LogUtils.d("type:" + type);
        });
    }


    @Override
    public void onClick(View v) {
        HashMap<String, String> params;
        switch (v.getId()) {
            case R.id.btn_r_duan:
                btnGetCode.setClickable(false);
                time = 80;
                handler.sendEmptyMessage(0);
                params = new HashMap<>();
                params.put("userPhone", etPhone.getText().toString().trim());
                params.put("type", "1");
                OkGo.post(Api.GET_CODE_URL).params(params)
                        .tag(this).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i("response:", s);
                        JsonObject jsonObject = GsonUtils.fromJson(s, JsonObject.class);
                        if (jsonObject.get("code").getAsInt() != 0) {
                            btnGetCode.setClickable(true);
                            btnGetCode.setText("重新获取验证码");
                            Toast.makeText(UserRegisterActivity.this,
                                    jsonObject.get("msg").getAsString(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        LogUtils.d("onError:" + e.getLocalizedMessage());
                        btnGetCode.setClickable(true);
                        btnGetCode.setText("重新获取验证码");
                        Toast.makeText(UserRegisterActivity.this, "请检查网络或遇到未知错误",
                                Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.btn_register:
                params = new HashMap<>();
                params.put("userPhone", etPhone.getText().toString().trim());
                params.put("code", etCode.getText().toString().trim());
                params.put("passWord", etPwd.getText().toString().trim());
                params.put("type", type + "");
                OkGo.post(Api.REGISTER_URL).params(params)
                        .tag(this).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i("response:", s);
                        JsonObject jsonObject = GsonUtils.fromJson(s, JsonObject.class);
                        Toast.makeText(UserRegisterActivity.this,
                                jsonObject.get("msg").getAsString(), Toast.LENGTH_LONG).show();
                        if (jsonObject.get("code").getAsInt() == 0) {
                            finish();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        //Log.i("response:", e.getMessage());
                        Toast.makeText(UserRegisterActivity.this, "请检查网络或遇到未知错误",
                                Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(0);
        handler = null;
        super.onDestroy();
    }
}
