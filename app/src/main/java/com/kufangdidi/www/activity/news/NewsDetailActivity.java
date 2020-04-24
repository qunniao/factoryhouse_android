package com.kufangdidi.www.activity.news;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.kufangdidi.www.R;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.modal.NewsModal;

import cn.qzb.richeditor.RichEditor;

public class NewsDetailActivity extends AppCompatActivity {
    private ImageView common_top_back;
    private NewsModal modal;

    private TextView title;
    private TextView date;
    private RichEditor news_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        BaseApplication.addDestoryActivity(this,"NewsDetailActivity");
        modal = (NewsModal) getIntent().getSerializableExtra("modal");

        initTitle();
        initView();
        initData();
    }

    private void initTitle() {
        common_top_back = findViewById(R.id.common_top_back);
        common_top_back.setOnClickListener(v -> finish());
    }
    private void initView() {
        date = findViewById(R.id.date);
        title = findViewById(R.id.title);
        news_editor=findViewById(R.id.news_editor);
    }
    private void initData() {
       if(modal==null)return;
       title.setText(modal.getTitle());
        date.setText(modal.getCreateTime());
        news_editor.setHtml("<style> img  { width: 100%; }</style>"+modal.getContent());

    }
}
