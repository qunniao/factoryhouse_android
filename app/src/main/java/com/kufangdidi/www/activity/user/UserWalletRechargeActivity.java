package com.kufangdidi.www.activity.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.kufangdidi.www.R;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.modal.PayResult;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class UserWalletRechargeActivity extends AppCompatActivity {
    private TextView common_top_back;
    private EditText wr_money;
    private Button mr_button;
    private double money;
    private PopupWindow mPopupWindow;
    private String payWay;
    private long clickTime = 0;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.ALIPAY_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(UserWalletRechargeActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        /*
                        Intent intent = new Intent(UserVipActivity.this, MainService.class);
                        intent.setAction("userAction");
                        intent.putExtra("method", MainService.paySuccess);
                        intent.putExtra("order_id",order_id);
                        startService(intent);
                        */
                        // finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(UserWalletRechargeActivity.this, "支付失败",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wallet_recharge);
        BaseApplication.addDestoryActivity(this, "UserWalletRechargeActivity");
        initTitle();
    }

    private void initTitle() {
        wr_money = findViewById(R.id.wr_money);
        mr_button = findViewById(R.id.mr_button);
        mr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wr_money.getText().length() == 0) {
                    Toast.makeText(UserWalletRechargeActivity.this, "请输入您要充值的金额！",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                money = Double.parseDouble(wr_money.getText().toString());
                if (money < 0.01) {
                    Toast.makeText(UserWalletRechargeActivity.this, "请输入正确的金额！",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                showPopwindow();
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

    private void walletRecharge() {
        OkGo.post(Constant.SERVER_HOST + "/order/alipayAPP").tag(this)
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                .params("money", money)
                .params("type", 1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("walletRecharge 返回结果s" + s);
                        payV2(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(UserWalletRechargeActivity.this, "请检查网络或遇到未知错误！",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * 支付宝支付业务
     *
     * @param
     */
    public void payV2(final String orderInfo) {
        LogUtils.d("payV2 orderInfo:" + orderInfo);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(UserWalletRechargeActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("web", result.toString());

                Message msg = new Message();
                msg.what = Constant.ALIPAY_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 显示popupWindow
     */
    private void showPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pay_way_select, null);
        ImageView img_close = popupView.findViewById(R.id.img_close);

        final ImageView img_alipay_danxuan = popupView.findViewById(R.id.img_alipay_danxuan);
        final ImageView img_wechat_danxuan = popupView.findViewById(R.id.img_wechat_danxuan);
        RelativeLayout ll_alipay = popupView.findViewById(R.id.rl_alipay);
        RelativeLayout ll_wechat = popupView.findViewById(R.id.rl_wechat);
        //  TextView pay_money = popupView.findViewById(R.id.pay_money);
        Button submit = (Button) popupView.findViewById(R.id.btn_submit);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        mPopupWindow = new PopupWindow(popupView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(false);
        // 设置popWindow的显示和消失动画
        mPopupWindow.setAnimationStyle(R.style.popup_anim);

        // 在底部显示
        mPopupWindow.showAtLocation(findViewById(R.id.mr_button),
                Gravity.BOTTOM, 0, 0);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        //popWindow消失监听方法
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
            }
        });

        payWay = "alipay";
        img_alipay_danxuan.setImageResource(R.mipmap.icn_danxuan2);
        img_wechat_danxuan.setImageResource(R.mipmap.icn_danxuan1);


        ll_alipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_alipay_danxuan.setImageResource(R.mipmap.icn_danxuan2);
                img_wechat_danxuan.setImageResource(R.mipmap.icn_danxuan1);
                payWay = "alipay";
            }
        });
        ll_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_alipay_danxuan.setImageResource(R.mipmap.icn_danxuan1);
                img_wechat_danxuan.setImageResource(R.mipmap.icn_danxuan2);
                payWay = "wechat";
                Toast.makeText(UserWalletRechargeActivity.this, "微信支付将在下一版本开放，请谅解",
                        Toast.LENGTH_LONG).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                if (clickTime > 0 && (now - clickTime) < (1000 * 2)) {
                    //Toast.makeText(UserWalletRechargeActivity.this,"已提交订单，正在支付...",Toast
                    // .LENGTH_LONG).show();
                    return;
                }
                clickTime = now;

                if (payWay.equals("alipay")) {
                    walletRecharge();
//                    Toast.makeText(UserWalletRechargeActivity.this, "支付宝支付将在下一版本开放，请谅解", Toast
//                    .LENGTH_LONG).show();
                    return;
                }

                if (payWay.equals("wechat")) {
                    Toast.makeText(UserWalletRechargeActivity.this, "微信支付将在下一版本开放，请谅解",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                mPopupWindow.dismiss();
                // addOrder();
            }
        });
    }

}
