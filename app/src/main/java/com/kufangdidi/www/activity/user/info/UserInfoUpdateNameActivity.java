package com.kufangdidi.www.activity.user.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.kufangdidi.www.R;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.service.MainService;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class UserInfoUpdateNameActivity extends AppCompatActivity {
    private RelativeLayout common_top_back;

    private EditText name;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_update_name);
        BaseApplication.addDestoryActivity(this,"UserInfoUpdateNameActivity");
        initTitle();

        initView();
    }

    private void initView() {
        name = findViewById(R.id.name);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = name.getText().toString();
                if(null == nick || "".equals(nick.trim())){
                    Toast.makeText(UserInfoUpdateNameActivity.this,"名字不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                if(nick.trim().equals(BaseApplication.getSpUtils().getString("userName"))){
                    Toast.makeText(UserInfoUpdateNameActivity.this,"名字没有变化，不需要保存",Toast.LENGTH_LONG).show();
                    return;
                }
                UpdateUserInfoByNick(nick);
            }
        });

        name.setText(BaseApplication.getSpUtils().getString("userName"));

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

    private void UpdateUserInfoByNick(String nick) {
        OkGo.post(Constant.SERVER_HOST+"/personal/updateUser")
                .tag(this)
                .params("uid", BaseApplication.getSpUtils().getInt("uid")+"")
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .params("userName",nick)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("findBannerListByServer 返回结果s:"+s);
                        JSONObject dataJson = null;
                        try {
                            dataJson = new JSONObject(s);
                            int code = (int) dataJson.get("code");

                            if(code ==0){
                                //修改成功
                                //登录成功
                                Intent intent = new Intent(UserInfoUpdateNameActivity.this, MainService.class);
                                intent.setAction("userAction");
                                intent.putExtra("method", MainService.initService);
                                startService(intent);
                                Toast.makeText(UserInfoUpdateNameActivity.this,(String) dataJson.get("msg"), Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                Toast.makeText(UserInfoUpdateNameActivity.this,(String) dataJson.get("msg"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        LogUtils.d("返回结果出错:"+e.getMessage());
                    }
                });
    }

}
