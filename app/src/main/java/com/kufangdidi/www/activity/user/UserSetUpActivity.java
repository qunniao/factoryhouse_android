package com.kufangdidi.www.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.kufangdidi.www.R;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.service.MainService;
import com.kufangdidi.www.utils.common;


public class UserSetUpActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView now_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        BaseApplication.addDestoryActivity(this,"UserSetUpActivity");
        initTitle();
        now_version = findViewById(R.id.now_version);
        now_version.setText(common.getVersionCode(this));
    }

    private void initTitle() {
        TextView common_top_title = findViewById(R.id.common_top_title);
        common_top_title.setText("设置");
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.common_top_back:
                finish();
                break;
            case R.id.about_us:
                startActivity(new Intent(this, UserAboutUsActivity.class));
                break;
            case R.id.quit:
                Intent intent = new Intent(this, MainService.class);
                intent.setAction("userAction");
                intent.putExtra("method", MainService.loginOut);
                startService(intent);
                break;
        }
    }
}