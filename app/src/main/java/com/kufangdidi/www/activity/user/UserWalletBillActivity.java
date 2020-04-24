package com.kufangdidi.www.activity.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.kufangdidi.www.R;
import com.kufangdidi.www.adapter.WalletBillAdapter;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.bean.WalletBillBean;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.mob.MobSDK.getContext;

public class UserWalletBillActivity extends AppCompatActivity {
    private RelativeLayout common_top_back;
    private TextView common_top_title;
    private RecyclerView wallet_bill_recyler;
    private WalletBillAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wallet_bill);
        BaseApplication.addDestoryActivity(this,"UserWalletBillActivity");
        initTitle();
        walletBill();
    }

    private void initTitle() {
        common_top_back = findViewById(R.id.common_top_back);
        common_top_title = findViewById(R.id.common_top_title);
        wallet_bill_recyler = findViewById(R.id.wallet_bill_recyler);
        common_top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        common_top_title.setText("账单");
    }
    private void walletBill() {
        OkGo.post(Constant.SERVER_HOST + "/walletOrder/querySuccessWalletOrderByUid")
                .tag(this)
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("walletBill 返回结果s" + s);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        if (jsonObject.getInteger("code") != 0) return;
                        WalletBillBean walletBillBean = JSONObject.parseObject(s, WalletBillBean.class);
                        List<WalletBillBean.Data> m_data = walletBillBean.getData();
                        LogUtils.d("content2 size:" + m_data.size());
                        List<WalletBillBean.Data> arraylist = new ArrayList<>();
                        for (WalletBillBean.Data con : m_data) {
                            LogUtils.d(con.getSuccessTime() + "    " + con.getOrderMoney() + " " );
                            arraylist.add(con);
                        }
                        if (adapter == null) {
                            adapter = new WalletBillAdapter(getContext());
                            adapter.setdata(arraylist);
                            wallet_bill_recyler.setLayoutManager(new LinearLayoutManager(getContext()));
                            wallet_bill_recyler.setAdapter(adapter);
                        } else {
                            adapter.setdata(arraylist);
                            adapter.notifyDataSetChanged();
                            wallet_bill_recyler.setAdapter(adapter);
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(UserWalletBillActivity.this, "请检查网络或遇到未知错误！",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
