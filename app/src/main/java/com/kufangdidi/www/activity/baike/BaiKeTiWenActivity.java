package com.kufangdidi.www.activity.baike;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.kufangdidi.www.R;
import com.kufangdidi.www.app.Api;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.utils.LogUtils;

import okhttp3.Call;
import okhttp3.Response;

public class BaiKeTiWenActivity extends AppCompatActivity {
    private RelativeLayout common_top_back;
    private TextView common_top_title;

    private LinearLayout lin1, lin2;
    private int zhuTi = 1;
    private TextView text;
    private LinearLayout text1, text2, text3, text4, text5, text6;
    private boolean mBool1 = false, mBool2 = false, mBool3 = false, mBool4 = false, mBool5 = false;
    private int status = 0;
    private Button tiwen_fabu;
    private EditText tiwen_biaoti, tiwen_wenti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.addDestoryActivity(this, "BaiKeTiWenActivity");
        setContentView(R.layout.activity_baike_tiwen);
        initTitle();
        initView();
        initData();
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
        common_top_title.setText("提问");
    }

    private void zhuTi() {
        zhuTi += 1;
        if (zhuTi % 2 == 0) {
            lin2.setVisibility(View.VISIBLE);
        } else {
            lin2.setVisibility(View.INVISIBLE);
        }
    }

    private void textChang() {
        if (mBool1 == true) {
            text.setText(((TextView) text1.findViewById(R.id.textview1)).getText());
            ((TextView) text1.findViewById(R.id.textview1)).setTextColor(getResources().getColor(R.color.colorPink));
            (text1.findViewById(R.id.textview11)).setVisibility(View.VISIBLE);
        } else {
            ((TextView) text1.findViewById(R.id.textview1)).setTextColor(getResources().getColor(R.color.colorBlack));
            (text1.findViewById(R.id.textview11)).setVisibility(View.INVISIBLE);
        }
        if (mBool2 == true) {
            text.setText(((TextView) text2.findViewById(R.id.textview2)).getText());
            ((TextView) text2.findViewById(R.id.textview2)).setTextColor(getResources().getColor(R.color.colorPink));
            (text2.findViewById(R.id.textview22)).setVisibility(View.VISIBLE);
        } else {
            ((TextView) text2.findViewById(R.id.textview2)).setTextColor(getResources().getColor(R.color.colorBlack));
            (text2.findViewById(R.id.textview22)).setVisibility(View.INVISIBLE);
        }
        if (mBool3 == true) {
            text.setText(((TextView) text3.findViewById(R.id.textview3)).getText());
            ((TextView) text3.findViewById(R.id.textview3)).setTextColor(getResources().getColor(R.color.colorPink));
            (text3.findViewById(R.id.textview33)).setVisibility(View.VISIBLE);
        } else {
            ((TextView) text3.findViewById(R.id.textview3)).setTextColor(getResources().getColor(R.color.colorBlack));
            (text3.findViewById(R.id.textview33)).setVisibility(View.INVISIBLE);
        }
        if (mBool4 == true) {
            text.setText(((TextView) text4.findViewById(R.id.textview4)).getText());
            ((TextView) text4.findViewById(R.id.textview4)).setTextColor(getResources().getColor(R.color.colorPink));
            (text4.findViewById(R.id.textview44)).setVisibility(View.VISIBLE);
        } else {
            ((TextView) text4.findViewById(R.id.textview4)).setTextColor(getResources().getColor(R.color.colorBlack));
            (text4.findViewById(R.id.textview44)).setVisibility(View.INVISIBLE);
        }
        if (mBool5 == true) {
            text.setText(((TextView) text5.findViewById(R.id.textview5)).getText());
            ((TextView) text5.findViewById(R.id.textview5)).setTextColor(getResources().getColor(R.color.colorPink));
            (text5.findViewById(R.id.textview55)).setVisibility(View.VISIBLE);
        } else {
            ((TextView) text5.findViewById(R.id.textview5)).setTextColor(getResources().getColor(R.color.colorBlack));
            (text5.findViewById(R.id.textview55)).setVisibility(View.INVISIBLE);
        }
    }

    private void initView() {
        lin1 = findViewById(R.id.linerlayout1);
        lin2 = findViewById(R.id.linearlayout2);
        text = findViewById(R.id.mytextview);
        text1 = findViewById(R.id.textView1);
        text2 = findViewById(R.id.textView2);
        text3 = findViewById(R.id.textView3);
        text4 = findViewById(R.id.textView4);
        text5 = findViewById(R.id.textView5);
        text6 = findViewById(R.id.textView6);
        tiwen_biaoti = findViewById(R.id.tiwen_biaoti);
        tiwen_wenti = findViewById(R.id.tiwen_wenti);
        tiwen_fabu = findViewById(R.id.tiwen_fabu);
        lin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zhuTi();
            }
        });
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = 1;
                zhuTi();
                mBool1 = true;
                mBool2 = mBool3 = mBool4 = mBool5 = false;
                textChang();
            }
        });
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = 2;
                zhuTi();
                mBool2 = true;
                mBool1 = mBool3 = mBool4 = mBool5 = false;
                textChang();
            }
        });
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = 3;
                zhuTi();
                mBool3 = true;
                mBool1 = mBool2 = mBool4 = mBool5 = false;
                textChang();
            }
        });
        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = 4;
                zhuTi();
                mBool4 = true;
                mBool1 = mBool2 = mBool3 = mBool5 = false;
                textChang();
            }
        });
        text5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = 5;
                zhuTi();
                mBool5 = true;
                mBool1 = mBool2 = mBool3 = mBool4 = false;
                textChang();
                LogUtils.d("status:" + status);
            }
        });
        text6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zhuTi();
            }
        });
    }

    private void initData() {

        LogUtils.d("wentibiaoti");
        tiwen_fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d("status:" + status);
                if (status == 0) {
                    Toast.makeText(BaiKeTiWenActivity.this, "请选择分类！", Toast.LENGTH_LONG).show();
                    return;
                }
                String wentibiaoti = tiwen_biaoti.getText().toString();
                if (wentibiaoti.length() < 1) {
                    Toast.makeText(BaiKeTiWenActivity.this, "请输入您的问题！", Toast.LENGTH_LONG).show();
                    return;
                }
                LogUtils.d("    createName:" + BaseApplication.getSpUtils().getString("userName") + "        createId:" + BaseApplication.getSpUtils().getInt("getUid")
                        + "       title:" + wentibiaoti + "       status:" + status + "      " +
                        "content:" + tiwen_wenti.getText().toString() + "      token:" + BaseApplication.getSpUtils().getString("token"));
                OkGo.post(Api.BASE_URL + "/encyclopedia/addEncyclopedia")
                        .tag(this)
                        .params("createName", BaseApplication.getSpUtils().getString("userName"))
                        .params("createId", BaseApplication.getSpUtils().getInt("uid"))
                        .params("title", wentibiaoti)
                        .params("type", status)
                        .params("content", tiwen_wenti.getText().toString())
                        .params("token", BaseApplication.getSpUtils().getString("token"))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                LogUtils.d("myChuzuList 返回结果s" + s);
                                JSONObject jsonObject = JSON.parseObject(s);
                                if (jsonObject.getInteger("code") == 0) {
                                    Toast.makeText(BaiKeTiWenActivity.this, "提问成功！",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(BaiKeTiWenActivity.this, jsonObject.getString(
                                            "msg") + jsonObject.getInteger("code"),
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                Toast.makeText(BaiKeTiWenActivity.this, "请检查网络或遇到未知错误！",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}
