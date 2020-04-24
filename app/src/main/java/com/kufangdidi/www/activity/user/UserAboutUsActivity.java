package com.kufangdidi.www.activity.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kufangdidi.www.R;
import com.kufangdidi.www.app.BaseApplication;

public class UserAboutUsActivity extends AppCompatActivity {
    private RelativeLayout common_top_back;
    private TextView common_top_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        BaseApplication.addDestoryActivity(this,"UserAboutUsActivity");
        initTitle();
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
        common_top_title.setText("关于我们");
    }
}
