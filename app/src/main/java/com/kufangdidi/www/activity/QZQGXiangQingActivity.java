package com.kufangdidi.www.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kufangdidi.www.R;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.modal.QiuZuQiuGouModal;
import com.kufangdidi.www.utils.LogUtils;

@SuppressLint("Registered")
public class QZQGXiangQingActivity extends AppCompatActivity {
    private QiuZuQiuGouModal modal;
    private TextView xq_title,xq_time,xq_productld,xq_functionalUse,xq_region,xq_detailedDescription,xq_area,xq_contact,xq_status,xq_layerHeight,xq_minimumCharge,xq_arealower;
    private String status;

    private LinearLayout ll_call;
    private long lastClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qzqg_xiangqing);
        BaseApplication.addDestoryActivity(this,"QZQGXiangQingActivity");
        modal = (QiuZuQiuGouModal) getIntent().getSerializableExtra("modal");
        initTitle();
        initView();
        initData();
    }

    private void initTitle() {
        RelativeLayout common_top_back = findViewById(R.id.common_top_back);
        TextView common_top_title = findViewById(R.id.common_top_title);
        common_top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        common_top_title.setText("详情");
    }

    private void initView() {
        xq_title = findViewById(R.id.xq_title);
        xq_time = findViewById(R.id.xq_time);
        xq_productld = findViewById(R.id.xq_productld);
        xq_functionalUse = findViewById(R.id.xq_functionalUse);
        xq_region = findViewById(R.id.xq_region);
        xq_detailedDescription = findViewById(R.id.xq_detailedDescription);
        xq_area=findViewById(R.id.xq_area);
        xq_contact=findViewById(R.id.xq_contact);
        xq_status=findViewById(R.id.xq_status);
        xq_layerHeight=findViewById(R.id.xq_layerHeight);
        xq_minimumCharge=findViewById(R.id.xq_minimumCharge);
        xq_arealower=findViewById(R.id.xq_arealower);

        ll_call = findViewById(R.id.ll_call);
        ll_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modal==null || null == modal.getContactPhone() || "".equals(modal.getContactPhone())){
                    LogUtils.d("电话号码为空 phone");
                    return;
                }
                LogUtils.d("电话号码不为空 phone:"+modal.getContactPhone());

                lastClickTime = System.currentTimeMillis();
                ActivityCompat.requestPermissions(QZQGXiangQingActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 139);


                //检查有没有拨打电话的权限
                if (ActivityCompat.checkSelfPermission(QZQGXiangQingActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                //开始拨打号码
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + modal.getContactPhone()));
                if (ActivityCompat.checkSelfPermission(QZQGXiangQingActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });
    }
    private void initData() {
        if(modal==null)return;
        xq_title.setText(modal.getTitle());
        xq_time.setText("发布："+modal.getCreateTime());
        xq_productld.setText(modal.getProductId());
        xq_functionalUse.setText(modal.getFunctionalUse());
        xq_region.setText(modal.getRegion());
        xq_detailedDescription.setText(modal.getDetailedDescription());
        xq_area.setText(modal.getAreaLower()+"-"+modal.getAreaCap()+"平方米");
        xq_contact.setText(modal.getContact());
        xq_layerHeight.setText(modal.getLayerHeight()+"米");
        xq_minimumCharge.setText(modal.getMinimumCharge()+"KAV");
        xq_arealower.setText(modal.getAreaLower()+"㎡");
        switch (modal.getStatus()){
            case 0:
                status="";
                break;
            case 1:
                status="求租";
                break;
            case 2:
                status="求购";
                break;
        }
        xq_status.setText(status);
    }
}
