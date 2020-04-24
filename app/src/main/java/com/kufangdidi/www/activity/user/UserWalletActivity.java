package com.kufangdidi.www.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.kufangdidi.www.R;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.bean.WalletBean;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;

import okhttp3.Call;
import okhttp3.Response;

public class UserWalletActivity extends AppCompatActivity {
    private RelativeLayout common_top_back;
    private TextView wallet_balance;
    private RelativeLayout wallet_recharge, records_consumption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wallet);
        BaseApplication.addDestoryActivity(this, "UserWalletActivity");
        initTitle();
        balanceInquiry();
    }
    @Override
    protected void onStart(){
        super.onStart();
        balanceInquiry();
    }

    private void initTitle() {
        wallet_balance = findViewById(R.id.wallet_balance);
        wallet_recharge = findViewById(R.id.wallet_recharge);
        wallet_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserWalletActivity.this, UserWalletRechargeActivity.class);
                startActivity(intent);
            }
        });
        records_consumption = findViewById(R.id.records_consumption);
        records_consumption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserWalletActivity.this, UserWalletBillActivity.class);
                startActivity(intent);
            }
        });
        common_top_back = findViewById(R.id.common_top_back);
        common_top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void balanceInquiry() {
        LogUtils.d("uid:    " + BaseApplication.getSpUtils().getInt("uid") + "        token:    " + BaseApplication.getSpUtils().getString("token"));
        OkGo.post(Constant.SERVER_HOST + "/wallet/queryWalletByUid").tag(this)
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("balanceInquiry 返回结果s" + s);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        if (jsonObject.getInteger("code") != 0) return;
                        WalletBean walletBean = JSONObject.parseObject(s, WalletBean.class);
                        WalletBean.Data content = walletBean.getData();
                        LogUtils.d("content.getBalance:" + content.getBalance());
                        wallet_balance.setText(content.getBalance() + "");
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(UserWalletActivity.this, "请检查网络或遇到未知错误！",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

}
