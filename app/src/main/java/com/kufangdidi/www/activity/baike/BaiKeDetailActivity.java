package com.kufangdidi.www.activity.baike;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kufangdidi.www.R;
import com.kufangdidi.www.adapter.BaiKeDetailAdapter;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.bean.BaiKeBean;
import com.kufangdidi.www.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static com.lzy.okgo.OkGo.getContext;

public class BaiKeDetailActivity extends AppCompatActivity {
    private RelativeLayout common_top_back;
    private TextView common_top_title,bkxq_title,bqxq_name,bqxq_time,bkqx_yuedu;

    private Button button;
    private RecyclerView bqxq_neirong;
    private BaiKeBean.Data.Content bkdetail;
    private BaiKeDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baike_detail);
        BaseApplication.addDestoryActivity(this,"BaiKeDetailActivity");
        bkdetail=(BaiKeBean.Data.Content)getIntent().getSerializableExtra("bean");

        initView();
        initData();
        initTitle();
    }

    private void initTitle() {
        common_top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        common_top_title.setText("百科详情");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaiKeDetailActivity.this, BaiKeHuiDaActivity.class);
                intent.putExtra("elid",bkdetail.getElid());
                startActivity(intent);
            }
        });
    }

    private void initView() {
        button = findViewById(R.id.button);
        common_top_back = findViewById(R.id.common_top_back);
        common_top_title = findViewById(R.id.common_top_title);
        bkxq_title = findViewById(R.id.bkxq_title);
        bqxq_name = findViewById(R.id.bqxq_name);
        bqxq_time = findViewById(R.id.bqxq_time);
//        bkqx_yuedu = findViewById(R.id.bkqx_yuedu);
        bqxq_neirong=findViewById(R.id.bkxq_neirong);
    }

    private void initData() {
        bkxq_title.setText(bkdetail.getTitle());
        bqxq_name.setText(bkdetail.getCreateName());
        bqxq_time.setText(bkdetail.getCreateTime());
//        bqxq_neirong.setText(bkdetail.getEncyclopediaAnswer().get(0).getContent());
        List<BaiKeBean.Data.Content.EncyclopediaAnswer>list=bkdetail.getEncyclopediaAnswer();
        LogUtils.d("list.size:"+list.size()+"bkdetail:"+bkdetail.getEncyclopediaAnswer().size());
        List<BaiKeBean.Data.Content.EncyclopediaAnswer> arraylist=new ArrayList<>();
        for (BaiKeBean.Data.Content.EncyclopediaAnswer answer:list) {
            LogUtils.d(answer.getCreateName()+answer.getContent());
            arraylist.add(answer);
        }
        LogUtils.d("arraylist:"+arraylist.size());
        if (adapter == null) {
            adapter = new BaiKeDetailAdapter(getContext());
            adapter.setdata(arraylist);
            bqxq_neirong.setLayoutManager(new LinearLayoutManager(getContext()));
            bqxq_neirong.setAdapter(adapter);
        } else {
            adapter.setdata(arraylist);
            adapter.notifyDataSetChanged();
            bqxq_neirong.setAdapter(adapter);
        }
    }
}
