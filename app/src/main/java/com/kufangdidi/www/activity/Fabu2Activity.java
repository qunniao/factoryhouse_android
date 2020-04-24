package com.kufangdidi.www.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.kufangdidi.www.R;
import com.kufangdidi.www.app.BaseApplication;

public class Fabu2Activity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout common_top_back;
    private TextView common_top_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu2);
        BaseApplication.addDestoryActivity(this,"Fabu2Activity");
        initTitle();
    }

    private void initTitle() {
        common_top_back = findViewById(R.id.common_top_back);
        common_top_title = findViewById(R.id.common_top_title);
        common_top_back.setOnClickListener(v -> finish());
        common_top_title.setText("选择发布类别");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_chuzu:
                Intent intent = new Intent(this, ChuzuChushouActivity.class);
                intent.putExtra("type", getIntent().getIntExtra("type", -1));
                intent.putExtra("fabu_type", 1);
                ActivityUtils.startActivity(intent);

                break;
            case R.id.rl_chushou:
                intent = new Intent(this, ChuzuChushouActivity.class);
                intent.putExtra("type", getIntent().getIntExtra("type", -1));
                intent.putExtra("fabu_type", 2);
                ActivityUtils.startActivity(intent);
                break;
            case R.id.rl_qiuzu:
                intent = new Intent(this, QiuzuQiugouActivity.class);
                intent.putExtra("type", getIntent().getIntExtra("type",-1));
                intent.putExtra("fabu_type", 1);
                ActivityUtils.startActivity(intent);

                break;
            case R.id.rl_qiugou:
                intent = new Intent(this, QiuzuQiugouActivity.class);
                intent.putExtra("type", getIntent().getIntExtra("type", -1));
                intent.putExtra("fabu_type", 2);
                ActivityUtils.startActivity(intent);
                break;
        }
    }
}
