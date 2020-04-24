package com.kufangdidi.www.activity.user.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class UserInfoUpdateGenderActivity extends AppCompatActivity {
    private RelativeLayout common_top_back;

    private RelativeLayout rl_nan,rl_nv;
    private ImageView image_select_nan,image_select_nv;
    private int gender;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_update_gender);
        BaseApplication.addDestoryActivity(this,"UserInfoUpdateNameActivity");
        initTitle();
        initView();
    }

    private void initView() {
        rl_nan = findViewById(R.id.rl_nan);
        rl_nv = findViewById(R.id.rl_nv);
        image_select_nan = findViewById(R.id.image_select_nan);
        image_select_nv = findViewById(R.id.image_select_nv);

        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int gend = new Integer(BaseApplication.getSpUtils().getInt("userSex"));
                if(gender == gend){
                    //没有做任何修改，直接返回
                    finish();
                }
                UpdateUserInfoByGender(gender);
            }
        });

        rl_nan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              gender = 0;
              image_select_nan.setVisibility(View.VISIBLE);
              image_select_nv.setVisibility(View.INVISIBLE);
            }
        });
        rl_nv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = 1;
                image_select_nan.setVisibility(View.INVISIBLE);
                image_select_nv.setVisibility(View.VISIBLE);
            }
        });

        gender = new Integer(BaseApplication.getSpUtils().getInt("userSex"));
        if(gender==0){
            image_select_nan.setVisibility(View.VISIBLE);
            image_select_nv.setVisibility(View.INVISIBLE);
        }else if(gender==1){
            image_select_nan.setVisibility(View.INVISIBLE);
            image_select_nv.setVisibility(View.VISIBLE);
        }
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

    private void UpdateUserInfoByGender(int gender) {
        OkGo.post(Constant.SERVER_HOST+"/personal/updateUser")
                .tag(this)
                .params("uid", BaseApplication.getSpUtils().getInt("uid")+"")
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .params("userSex",gender)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("UpdateUserInfoByGender 返回结果s:"+s);
                        JSONObject dataJson = null;
                        try {
                            dataJson = new JSONObject(s);
                            int code = (int) dataJson.get("code");

                            if(code ==0){
                                //修改成功
                                Intent intent = new Intent(UserInfoUpdateGenderActivity.this, MainService.class);
                                intent.setAction("userAction");
                                intent.putExtra("method", MainService.initService);
                                startService(intent);
                                Toast.makeText(UserInfoUpdateGenderActivity.this,(String) dataJson.get("msg"), Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                Toast.makeText(UserInfoUpdateGenderActivity.this,(String) dataJson.get("msg"), Toast.LENGTH_LONG).show();
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
