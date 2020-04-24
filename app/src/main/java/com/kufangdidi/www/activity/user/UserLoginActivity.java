package com.kufangdidi.www.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.kufangdidi.www.R;
import com.kufangdidi.www.activity.MainActivity;
import com.kufangdidi.www.app.Api;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.modal.LoginBean;
import com.kufangdidi.www.service.MainService;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout common_top_back;
    private TextView tv_reg;
    private EditText etPhone;
    private EditText etPassword;
    private InputMethodManager imm;

    private long clickTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        BaseApplication.addDestoryActivity(this,"UserLoginActivity");
        initTitle();
        initView();
    }

    private void initView() {
        tv_reg = findViewById(R.id.tv_reg);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        imm= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());//设置密码为不可见。
        tv_reg.setOnClickListener(this);
    }

    private void initTitle() {
        common_top_back = findViewById(R.id.common_top_back);
        common_top_back.setOnClickListener(v -> finish());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("huaxiu", "onStart方法");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("huaxiu", "onDestroy方法");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reg:
                startActivity(new Intent(this, UserRegisterActivity.class));
                break;
            case R.id.tv_forget_pwd:
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_login:
                HashMap<String, String> params = new HashMap<>();
                System.out.println(etPhone.getText().toString());
                System.out.println(etPassword.getText().toString());

                long now = System.currentTimeMillis();
                if(clickTime>0 && (now-clickTime)<(1000*2)){
                    Toast.makeText(UserLoginActivity.this,"操作太频繁",Toast.LENGTH_SHORT).show();
                    return;
                }

                if("".equals(etPhone.getText().toString())){
                    Toast.makeText(UserLoginActivity.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                clickTime = now;
                if("".equals(etPassword.getText().toString())){
                    Toast.makeText(UserLoginActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }


                params.put("userPhone", etPhone.getText().toString().trim());
                params.put("passWord", etPassword.getText().toString().trim());
                System.out.println();
                OkGo.post(Api.LOGIN_URL).params(params)
                        .tag(this).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("登录接口返回 s:"+s);
                        Log.i("headers:", response.headers().toString());
                        Log.i("message:", response.message());
                        Log.i("response:", response.toString());
                        Log.i("response:", s);
                        LoginBean loginBean = GsonUtils.fromJson(s, LoginBean.class);
                        if (loginBean.getCode() != 0) {
                            Toast.makeText(UserLoginActivity.this, loginBean.getMsg(), Toast.LENGTH_LONG).show();
                        } else {
                            /**
                             * uid : 0
                             * userName : null
                             * userPhone : 18702528557
                             * userSex : 0
                             * userPassword : null
                             * balance : 0.0
                             * createTime : null
                             * token : 9lnzgtft0zkCatmUlobRdQ==
                             */
                            BaseApplication.getSpUtils().put("uid", loginBean.getData().getUid());
                            BaseApplication.getSpUtils().put("userName", loginBean.getData().getUserName());
                            BaseApplication.getSpUtils().put("userPhone", loginBean.getData().getUserPhone());
                            BaseApplication.getSpUtils().put("userSex", loginBean.getData().getUserSex());
                            BaseApplication.getSpUtils().put("userPassword", etPassword.getText().toString().trim());
                            BaseApplication.getSpUtils().put("balance", loginBean.getData().getBalance() + "");
                            BaseApplication.getSpUtils().put("createTime", loginBean.getData().getCreateTime());
                            BaseApplication.getSpUtils().put("token", loginBean.getData().getToken());
                            BaseApplication.getSpUtils().put("jiguang_username", loginBean.getData().getJiguangUsername());
                            BaseApplication.getSpUtils().put("jiguang_password", loginBean.getData().getJiguangPassword());
                            BaseApplication.getSpUtils().put("avatarUrl", loginBean.getData().getAvatarUrl());
                            BaseApplication.getSpUtils().put("mainArea", loginBean.getData().getMainArea());
                            BaseApplication.getSpUtils().put("introduction", loginBean.getData().getIntroduction());
                            BaseApplication.getSpUtils().put("type", loginBean.getData().getType());

                            //登录成功
                            Intent intent = new Intent(UserLoginActivity.this, MainService.class);
                            intent.setAction("userAction");
                            intent.putExtra("method", MainService.loginSuccess);
                            startService(intent);

                            startActivity(new Intent(UserLoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(UserLoginActivity.this, "请检查网络或遇到未知错误", Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }
}