package com.kufangdidi.www.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.kufangdidi.www.R;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.app.Constants;

public class Fabu1Activity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu1);
        BaseApplication.addDestoryActivity(this,"Fabu1Activity");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_tudi:
                Intent intent = new Intent(this, Fabu2Activity.class);
                intent.putExtra("type", Constants.FABU_TYPE_TUDI);
                ActivityUtils.startActivity(intent);
                break;
            case R.id.ll_cangku:
                intent = new Intent(this, Fabu2Activity.class);
                intent.putExtra("type", Constants.FABU_TYPE_CANGKU);
                ActivityUtils.startActivity(intent);
                break;
            case R.id.ll_changfang:
                intent = new Intent(this, Fabu2Activity.class);
                intent.putExtra("type", Constants.FABU_TYPE_CHANGFANG);
                ActivityUtils.startActivity(intent);
                break;
            case R.id.btn_close:
                finish();
                break;
        }
    }
}
