package com.kufangdidi.www.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kufangdidi.www.R;
import com.kufangdidi.www.adapter.ViewPagerAdapter;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.bean.YuanQuZhaoShangBean;
import com.kufangdidi.www.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class YQZSDetailActivity extends AppCompatActivity {
    private ImageView common_top_back;
    private YuanQuZhaoShangBean.Data.Content bean;
    private ViewPager mViewPager;
    private LinearLayout linearLayoutDots;
    private TextView yqxq_title, yqxq_price, yqxq_area, yqxq_times, yqxq_jiaofangshijian,
            yqxq_zongdongshi, yqxq_jianzhujiegou, yqxq_xiaofangdengji, yqxq_chanzheng,
            yqxq_address, yqxq_zhengce, yqxq_peitao, yqxq_shiheqiye, yqxq_jieshao;


    private List<ImageView> mImageList;//轮播的图片集合
    private int previousPosition = 0;//前一个被选中的position
    private List<View> mDots;//小点
    private boolean isStop = false;//线程是否停止
    private static int PAGER_TIOME = 5000;//间隔时间
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yqzs_xiangqing);
        BaseApplication.addDestoryActivity(this,"YQZSDetailActivity");
        context = getApplicationContext();
        bean = (YuanQuZhaoShangBean.Data.Content) getIntent().getSerializableExtra("bean");
        LogUtils.d("size:"+bean.getPictureStorage().size());
        initTitle();
        initView();
        initData();
        init();
    }

    private void initTitle() {
        common_top_back = findViewById(R.id.common_top_back);
        common_top_back.setOnClickListener(v -> finish());
    }

    private void initView() {
        yqxq_title = findViewById(R.id.yqxq_title);
        yqxq_price = findViewById(R.id.yqxq_price);
        yqxq_area = findViewById(R.id.yqxq_area);
        yqxq_times = findViewById(R.id.yqxq_times);
        yqxq_jiaofangshijian = findViewById(R.id.yqxq_jiaofangshijian);
        yqxq_zongdongshi = findViewById(R.id.yqxq_zongdongshi);
        yqxq_jianzhujiegou = findViewById(R.id.yqxq_jianzhujiegou);
        yqxq_xiaofangdengji = findViewById(R.id.yqxq_xiaofangdengji);
        yqxq_chanzheng = findViewById(R.id.yqxq_chanzheng);
        yqxq_address = findViewById(R.id.yqxq_address);
        yqxq_zhengce = findViewById(R.id.yqxq_zhengce);
        yqxq_peitao = findViewById(R.id.yqxq_peitao);
        yqxq_shiheqiye = findViewById(R.id.yqxq_shiheqiye);
        yqxq_jieshao = findViewById(R.id.yqxq_jieshao);
    }

    private void initData() {
        if (bean == null) return;
        yqxq_title.setText(null);
        yqxq_price.setText(bean.getParkprice() + "元/㎡/月");
        yqxq_area.setText(bean.getPriceArea() + "平方");
        yqxq_times.setText("-");
        yqxq_jiaofangshijian.setText(bean.getDeliveryTime());
        yqxq_zongdongshi.setText(bean.getAllBuildingsNum() + "栋");
        yqxq_jianzhujiegou.setText(bean.getAhtStructure());
        yqxq_xiaofangdengji.setText(bean.getFireRating());
        yqxq_chanzheng.setText(bean.getProductionCertificate());
        yqxq_address.setText(bean.getParkAddress());
        yqxq_zhengce.setText(bean.getPolicy());
        yqxq_peitao.setText(bean.getMatching());
        yqxq_shiheqiye.setText(bean.getSuitableBusiness());
        yqxq_jieshao.setText(bean.getIntroduce());
    }


    public void init() {
        mViewPager = findViewById(R.id.yqxq_vp);
        linearLayoutDots = findViewById(R.id.layout_dot);
        if(null == bean.getTitlePicture() || "".equals(bean.getTitlePicture()))return;
        /*
        if (bean.getPictureStorage()==null||bean.getPictureStorage().size() == 0) {
            mViewPager.setBackground(getResources().getDrawable(R.mipmap.home_list_demo));
            linearLayoutDots.setVisibility(View.INVISIBLE);
            return;
        }


        if (bean.getPictureStorage().size()==1){
            ImageView imageView1=new ImageView(this);
            RequestOptions options = new RequestOptions()
                    .error(R.mipmap.home_list_demo)
                    .placeholder(R.mipmap.home_list_demo);
            Glide.with(context).load(bean.getPictureStorage().get(0).getUrl()).apply(options).into(imageView1);
            LogUtils.d("url:"+bean.getPictureStorage().get(0).getUrl());
            mViewPager.addView(imageView1);
            linearLayoutDots.setVisibility(View.INVISIBLE);
            return;
        }
         */

        initData2();//初始化数据
        initView2();//初始化View，设置适配器
        autoPlayView();//开启线程，自动播放
    }

    /**
     * 第二步、初始化数据（图片、标题、点击事件）
     */
    public void initData2() {
        //初始化标题列表和图片
//        mImageTitles = new String[]{"这是一个好看的标题1","这是一个优美的标题2","这是一个快乐的标题3","这是一个开心的标题4"};
//        int[] imageRess = new int[]{R.drawable.ncvt_wifi_head,R.drawable.img1,R.drawable.img2,R
// .drawable.img3};

        //添加图片到图片列表里
        mImageList = new ArrayList<>();
//        ImageView iv;
//        for (int i = 0; i < mImageList.size(); i++) {
//            iv = new ImageView(this);
//            iv.setBackgroundResource(imageRess[i]);//设置图片
//            iv.setId(imgae_ids[i]);//顺便给图片设置id
//            iv.setOnClickListener(new pagerImageOnClick());//设置图片点击事件
//            mImageList.add(iv);
//        }
        ImageView iv;
        LogUtils.d("size:" + bean.getPictureStorage().size());

        iv = new ImageView(this);
        RequestOptions options = new RequestOptions()
                .error(R.mipmap.home_list_demo)
                .placeholder(R.mipmap.home_list_demo);
        Glide.with(context).load(bean.getTitlePicture()).apply(options).into(iv);
        LogUtils.d("url:" + bean.getTitlePicture());
        mImageList.add(iv);

        for (int i = 0; i < bean.getPictureStorage().size(); i++) {
            iv = new ImageView(this);
            /*
            Picasso.with(context)
                    .load(Constant.SERVER_PIC_HOSTds + bean.getPictureStorage().get(i).getUrl())
                    .into(iv);
                    */
            options = new RequestOptions()
                    .error(R.mipmap.home_list_demo)
                    .placeholder(R.mipmap.home_list_demo);
            Glide.with(context).load(bean.getPictureStorage().get(i).getUrl()).apply(options).into(iv);
            LogUtils.d("url:" + bean.getPictureStorage().get(i).getUrl());
            mImageList.add(iv);
            LogUtils.d("iv的值" + iv);
//            }
        }

        //添加轮播点
        mDots = addDots(linearLayoutDots, fromResToDrawable(this, R.drawable.lunboxiaoyuandian),
                mImageList.size());//其中fromResToDrawable()方法是我自定义的，目的是将资源文件转成Drawable


    }

    //图片点击事件
//    private class pagerImageOnClick implements View.OnClickListener{
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.pager_image1:
//                    Toast.makeText(MainActivity.this, "图片1被点击", Toast.LENGTH_SHORT).show();
//                    break;
//                case R.id.pager_image2:
//                    Toast.makeText(MainActivity.this, "图片2被点击", Toast.LENGTH_SHORT).show();
//                    break;
//                case R.id.pager_image3:
//                    Toast.makeText(MainActivity.this, "图片3被点击", Toast.LENGTH_SHORT).show();
//                    break;
//                case R.id.pager_image4:
//                    Toast.makeText(MainActivity.this, "图片4被点击", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    }

    /**
     * 第三步、给PagerViw设置适配器，并实现自动轮播功能
     */
    public void initView2() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mImageList, mViewPager);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //伪无限循环，滑到最后一张图片又从新进入第一张图片
                int newPosition = position % mImageList.size();
                // 把当前选中的点给切换了, 还有描述信息也切换
//                mTvPagerTitle.setText(mImageTitles[newPosition]);//图片下面设置显示文本
                //设置轮播点
                LinearLayout.LayoutParams newDotParams =
                        (LinearLayout.LayoutParams) mDots.get(newPosition).getLayoutParams();
                newDotParams.width = 24;
                newDotParams.height = 24;

                LinearLayout.LayoutParams oldDotParams =
                        (LinearLayout.LayoutParams) mDots.get(previousPosition).getLayoutParams();
                oldDotParams.width = 16;
                oldDotParams.height = 16;

                // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
                previousPosition = newPosition;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setFirstLocation();
    }

    /**
     * 第四步：设置刚打开app时显示的图片和文字
     */
    private void setFirstLocation() {
//        mTvPagerTitle.setText(mImageTitles[previousPosition]);
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % mImageList.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        mViewPager.setCurrentItem(currentPosition);
    }

    /**
     * 第五步: 设置自动播放,每隔PAGER_TIOME秒换一张图片
     */
    private void autoPlayView() {
        //自动播放图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            if (mImageList.size()<=1)return;
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                        }
                    });
                    SystemClock.sleep(PAGER_TIOME);
                }
            }
        }).start();
    }


    /**
     * 资源图片转Drawable
     *
     * @param context
     * @param resId
     * @return
     */
    public Drawable fromResToDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }


    /**
     * 动态添加一个点
     *
     * @param linearLayout 添加到LinearLayout布局
     * @param backgount    设置
     * @return
     */
    public int addDot(final LinearLayout linearLayout, Drawable backgount) {
        final View dot = new View(this);
        LinearLayout.LayoutParams dotParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        dotParams.width = 16;
        dotParams.height = 16;
        dotParams.setMargins(4, 0, 4, 0);
        dot.setLayoutParams(dotParams);
        dot.setBackground(backgount);
        dot.setId(View.generateViewId());
        linearLayout.addView(dot);
        return dot.getId();
    }

    /**
     * 添加多个轮播小点到横向线性布局
     *
     * @param linearLayout
     * @param backgount
     * @param number
     * @return
     */
    public List<View> addDots(final LinearLayout linearLayout, Drawable backgount, int number) {
        List<View> dots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int dotId = addDot(linearLayout, backgount);
            dots.add(findViewById(dotId));
        }
        return dots;
    }
}
