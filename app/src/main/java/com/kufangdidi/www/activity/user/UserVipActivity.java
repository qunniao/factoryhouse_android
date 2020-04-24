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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.squareup.picasso.Picasso;
import com.kufangdidi.www.R;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.bean.IsVipBean;
import com.kufangdidi.www.bean.VipBean;
import com.kufangdidi.www.fragment.FragmentMe;
import com.kufangdidi.www.modal.PayResult;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

public class UserVipActivity extends AppCompatActivity {
    private RelativeLayout common_top_back;
    private RelativeLayout oneyear_layout;
    private RelativeLayout twoyear_layout;
    private RelativeLayout threeyear_layout;
    private TextView tv_money1, tv_money2, tv_money3, one_year_vip, two_year_vip, three_year_vip,
            theme, if_vip, huiyuan;
    private ImageView uv_vip;
    private CircleImageView vip_touxiang;
    private boolean click_one = true;
    private boolean click_two = false;
    private boolean click_three = false;

    private Button submit;

    private PopupWindow mPopupWindow;
    private String payWay;
    private double total_money;
    private long clickTime = 0;

    private int pageNum = 1, type = 2;
    private List<VipBean.Data.Content> arraylist;
    private VipBean.Data.Content modal;

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
                        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                        if_vip.setText("您已是VIP会员");
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
                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_vip);
        BaseApplication.addDestoryActivity(this, "UserVipActivity");
        mContext = getApplicationContext();
        initTitle();
        mOnClick();
        mClickBoolean();
        initView();
        myVip();
        isVip();
    }

    private void initView() {
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopwindow();
            }
        });
    }

    private void initTitle() {
        common_top_back = findViewById(R.id.common_top_back);
        common_top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void mOnClick() {
        tv_money1 = findViewById(R.id.tv_money1);
        tv_money2 = findViewById(R.id.tv_money2);
        tv_money3 = findViewById(R.id.tv_money3);
        one_year_vip = findViewById(R.id.one_year_vip);
        two_year_vip = findViewById(R.id.two_year_vip);
        three_year_vip = findViewById(R.id.three_year_vip);

        uv_vip = findViewById(R.id.uv_vip);

        oneyear_layout = findViewById(R.id.oneyear_layout);
        twoyear_layout = findViewById(R.id.twoyear_layout);
        threeyear_layout = findViewById(R.id.threeyear_layout);
        theme = findViewById(R.id.theme);
        if_vip = findViewById(R.id.if_vip);
        huiyuan = findViewById(R.id.huiyuan);
        vip_touxiang = findViewById(R.id.vip_touxiang);
        theme.setText(FragmentMe.tv_name.getText());
        if(null == BaseApplication.getSpUtils().getString("avatarUrl") || "".equals(BaseApplication.getSpUtils().getString(
                "avatarUrl")))return;
        Picasso.with(this)
                .load(Constant.SERVER_PIC_HOSTds + BaseApplication.getSpUtils().getString(
                        "avatarUrl"))
                .into(vip_touxiang);

    }

    private void mClickBoolean() {
        if (click_one == true) {
            oneyear_layout.setBackground(getResources().getDrawable(R.drawable.yollow_view));
            (oneyear_layout.findViewById(R.id.clickone)).setVisibility(View.VISIBLE);
        } else {
            oneyear_layout.setBackground(null);
            (oneyear_layout.findViewById(R.id.clickone)).setVisibility(View.INVISIBLE);
        }
        if (click_two == true) {
            twoyear_layout.setBackground(getResources().getDrawable(R.drawable.yollow_view));
            (twoyear_layout.findViewById(R.id.clicktwo)).setVisibility(View.VISIBLE);
        } else {
            twoyear_layout.setBackground(null);
            (twoyear_layout.findViewById(R.id.clicktwo)).setVisibility(View.INVISIBLE);
        }
        if (click_three == true) {
            threeyear_layout.setBackground(getResources().getDrawable(R.drawable.yollow_view));
            (threeyear_layout.findViewById(R.id.clickthree)).setVisibility(View.VISIBLE);
        } else {
            threeyear_layout.setBackground(null);
            (threeyear_layout.findViewById(R.id.clickthree)).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 支付宝支付业务
     *
     * @param
     */
    public void payV2(final String orderInfo) {
        LogUtils.d("payV2 orderInfo:" + orderInfo);
        /*
        if (TextUtils.isEmpty(Constant.APPID) || (TextUtils.isEmpty(Constant.RSA2_PRIVATE) &&
        TextUtils.isEmpty(Constant.RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }
        */

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(UserVipActivity.this);
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
        mPopupWindow.showAtLocation(findViewById(R.id.submit),
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
                Toast.makeText(mContext, "微信支付将在下一版本开放，请谅解", Toast.LENGTH_LONG).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                if (clickTime > 0 && (now - clickTime) < (1000 * 2)) {
                    //Toast.makeText(mContext,"已提交订单，正在支付...",Toast.LENGTH_LONG).show();
                    return;
                }
                clickTime = now;

                if (payWay.equals("alipay")) {
                    vipAlipay();
//                    Toast.makeText(mContext, "支付宝支付将在下一版本开放，请谅解", Toast.LENGTH_LONG).show();
                    return;
                }

                if (payWay.equals("wechat")) {
                    Toast.makeText(mContext, "微信支付将在下一版本开放，请谅解", Toast.LENGTH_LONG).show();
                    return;
                }
                mPopupWindow.dismiss();
                // addOrder();
            }
        });
    }

    private void vipAlipay() {
        LogUtils.d("type:" + type + "token:" + BaseApplication.getSpUtils().getString("token") +
                "uid" + BaseApplication.getSpUtils().getInt("uid"));
        OkGo.post("https://back.zhanchengwlkj.com/factoryhouse" + "/order/alipayAPP").tag(this)
                .params("plid", modal.getPlid())
                .params("type", type)
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("vipAlipay 返回结果s" + s);
                        payV2(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(UserVipActivity.this, "请检查网络或遇到未知错误！", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void myVip() {
        OkGo.post(Constant.SERVER_HOST + "/productList/pageProductList").tag(this)
                .params("pageNum", pageNum)
                .params("pageSize", 50)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("myVip 返回结果s" + s);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        if (jsonObject.getInteger("code") != 0) return;
                        VipBean vipBean = JSONObject.parseObject(s, VipBean.class);
                        List<VipBean.Data.Content> content = vipBean.getData().getContent();
                        LogUtils.d("content size:" + content.size());
                        arraylist = new ArrayList<>();
                        for (VipBean.Data.Content con : content) {
                            LogUtils.d(con.getBalance() + " " + con.getContent() + " ");
                            arraylist.add(con);
                        }
                        if (arraylist.size() == 0) return;

                        sortList();
                        initViewData();
                        //默认第一个已选
                        click_one = true;
                        click_two = false;
                        click_three = false;
                        mClickBoolean();
                        modal = arraylist.get(0);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(UserVipActivity.this, "请检查网络或遇到未知错误！", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void isVip() {
        OkGo.post(Constant.SERVER_HOST + "/personal/queryVipByUid").tag(this)
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("isVip 返回结果s" + s);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        if (jsonObject.getInteger("code") != 0) return;
                        IsVipBean vipBean = JSONObject.parseObject(s, IsVipBean.class);
                        IsVipBean.Data data = vipBean.getData();
                        LogUtils.d("content size:" + data);
                        if (data == null) return;
                        if_vip.setText("会员到期时间：" + data.getExpiryDate());
                        submit.setText("立即续费");
                        huiyuan.setTextColor(getResources().getColor(R.color.coloryellow));
                        theme.setTextColor(getResources().getColor(R.color.coloryellow));
                        uv_vip.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(UserVipActivity.this, "请检查网络或遇到未知错误！", Toast.LENGTH_LONG).show();
                    }
                });
    }

    void sortList() {
        List<VipBean.Data.Content> list = new ArrayList<>();
        VipBean.Data.Content modal;
        for (int i = 0; i < arraylist.size(); i++) {
            for (int k = i + 1; k < arraylist.size(); k++) {
                if (arraylist.get(i).getBalance() > arraylist.get(k).getBalance()) {
                    modal = arraylist.get(i);
                    arraylist.set(i, arraylist.get(k));
                    arraylist.set(k, modal);
                }
            }
        }
        for (int i = 0; i < arraylist.size(); i++) {
            LogUtils.d("id:" + arraylist.get(i).getPlid());
            LogUtils.d("balance:" + arraylist.get(i).getBalance());
            LogUtils.d("content:" + arraylist.get(i).getContent());
        }

    }

    private void initViewData() {
        if (arraylist.size() == 0) return;
        one_year_vip.setText(arraylist.get(0).getContent());
        tv_money1.setText("￥" + arraylist.get(0).getBalance());
        two_year_vip.setText(arraylist.get(1).getContent());
        tv_money2.setText("￥" + arraylist.get(1).getBalance());
        three_year_vip.setText(arraylist.get(2).getContent());
        tv_money3.setText("￥" + arraylist.get(2).getBalance());
        oneyear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_one = true;
                click_two = false;
                click_three = false;
                mClickBoolean();
                modal = arraylist.get(0);
            }
        });


        twoyear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_two = true;
                click_one = false;
                click_three = false;
                mClickBoolean();
                modal = arraylist.get(1);
            }
        });

        threeyear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_three = true;
                click_one = false;
                click_two = false;
                mClickBoolean();
                modal = arraylist.get(2);
            }
        });
    }
}
