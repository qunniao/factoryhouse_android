package com.kufangdidi.www.activity.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kufangdidi.www.R;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class UserZhaoFangActivity extends AppCompatActivity {
    private RelativeLayout common_top_back;
    private TextView common_top_title;
    private RadioGroup zf_radiogroup1, zf_radiogroup2;
    private EditText zf_name, zf_phone, zf_beizhu;
    private Button zf_tijiao;
    private Integer type = 0, status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_zhaofang);
        BaseApplication.addDestoryActivity(this, "UserZhaoFangActivity");
        initTitle();
        initView();
        initData();
    }

    private void initData() {
        zf_radiogroup1.setOnCheckedChangeListener((group, checkedId) -> {
            type = checkedId;
        });
        zf_radiogroup2.setOnCheckedChangeListener((group, checkedId) -> {
            status = checkedId - 3;
        });
        zf_tijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d("type:" + type + "status:" + status);
                if (type == 0) {
                    Toast.makeText(UserZhaoFangActivity.this, "请选择您想选择的类别", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (status == 0) {
                    Toast.makeText(UserZhaoFangActivity.this, "请选择您想选择的意向", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (zf_name.getText().length() == 0) {
                    Toast.makeText(UserZhaoFangActivity.this, "请输入您的姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (zf_phone.getText().length() == 0) {
                    Toast.makeText(UserZhaoFangActivity.this, "请输入您的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (zf_phone.getText().length() != 11) {
                    Toast.makeText(UserZhaoFangActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (zf_beizhu.getText().length() == 0) {
                    Toast.makeText(UserZhaoFangActivity.this, "请输入您的备注", Toast.LENGTH_SHORT).show();
                    return;
                }
                tiJiao();
            }
        });
    }

    private void tiJiao() {
        OkGo.post(Constant.SERVER_HOST + "/delegationInformation/addDelegationInformation")
                .tag(this)
                .params("uid", BaseApplication.getSpUtils().getInt("uid") + "")
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .params("type", type)
                .params("status", status)
                .params("contact", zf_name.getText().toString())
                .params("contactPhone", zf_phone.getText().toString())
                .params("content", zf_beizhu.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("帮我找房 返回结果s:" + s);
                        JSONObject dataJson = null;
                        try {
                            dataJson = new JSONObject(s);
                            int code = (int) dataJson.get("code");

                            if (code == 0) {
                                Toast.makeText(UserZhaoFangActivity.this, (String) dataJson.get(
                                        "msg"), Toast.LENGTH_LONG).show();
                                finish();

                            } else {
                                Toast.makeText(UserZhaoFangActivity.this, (String) dataJson.get(
                                        "msg"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        LogUtils.d("返回结果出错:" + e.getMessage());
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
        common_top_title.setText("帮我找房");
    }

    private void initView() {
        zf_radiogroup1 = findViewById(R.id.zf_radiogroup1);
        zf_radiogroup2 = findViewById(R.id.zf_radiogroup2);
        zf_name = findViewById(R.id.zf_name);
        zf_phone = findViewById(R.id.zf_phone);
        zf_beizhu = findViewById(R.id.zf_beizhu);
        zf_tijiao = findViewById(R.id.zf_tijiao);
    }

}
