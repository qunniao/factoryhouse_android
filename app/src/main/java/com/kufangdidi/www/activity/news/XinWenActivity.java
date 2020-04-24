package com.kufangdidi.www.activity.news;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kufangdidi.www.R;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.fragment.PageFragment;

import java.util.ArrayList;
import java.util.List;

public class XinWenActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> listTitles;
    private List<Fragment> fragments;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xinwen);
        BaseApplication.addDestoryActivity(this,"XinWenActivity");
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        initTitle();
        initView();
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
        common_top_title.setText("新闻资讯");
    }

    private void initView() {

//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
//        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),
//                this);
//        viewPager.setAdapter(adapter);

        //TabLayout
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        tabLayout.setupWithViewPager(viewPager);


        listTitles = new ArrayList<>();
        fragments = new ArrayList<>();
        listTitles.add("安通有话说");
        listTitles.add("监测与分析");
        listTitles.add("项目招商");
        listTitles.add("园区动态");
        listTitles.add("指数分析");

        for (int i = 0; i < listTitles.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(listTitles.get(i)));
        }
//        @Override
//                public CharSequence getPageTitle(int position){
//            return;
//        }
        for (int i = 1; i <= listTitles.size(); i++){
            fragments.add(PageFragment.newInstance(i));
        }
        FragmentPagerAdapter adapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Log.i("web:",position+"");
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
            @Override
            public CharSequence getPageTitle(int position){
                Log.i("web:",position+"");
                return listTitles.get(position);
            }
        };
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);
    }

}
