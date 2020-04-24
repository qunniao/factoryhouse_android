package com.kufangdidi.www.activity.product;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kufangdidi.www.R;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.bean.HouseDetailBean;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.Call;
import okhttp3.Response;

public class ProductHomeActivity extends AppCompatActivity {
    private ImageView common_top_back;

    private TextView tv_title, tv_price, tv_area, tv_times, tv_type, tv_address, tv_zuqi,
            tv_can_fenzu, tv_suitable, tv_intro, tv_infrastructure, tv_peripheralPackage;
    private TextView tv_build_type, tv_building, tv_all_area, tv_user_area, tv_all_floor,
            tv_sale_floor, tv_diceng_height, tv_shangceng_height, tv_xingche, tv_xingche_zaizhong
            , tv_huoti, tv_huotizaizhong;
    private ImageView img_moren_bannar;
    private ImageView iv_call, iv_chat, my_collection,iv_share;

    private ViewPager mViewPaper;
    private List<ImageView> images;
    private int currentItem;
    private ViewPagerAdapter adapter;

    //记录上一次点的位置
    private int oldPosition = 0;
    private ScheduledExecutorService scheduledExecutorService;

    private String productId;
    private HouseDetailBean.DataBean modal;

    int screenWidth;
    int lunboHeight;
    private Boolean m_collection = false;

    private long lastClickTime;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_home);
        BaseApplication.addDestoryActivity(this, "ProductHomeActivity");
        productId = getIntent().getStringExtra("product_id");

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)

        initTitle();
        initView();
        loadData();

    }

    private void ifCollection() {
        OkGo.post(Constant.SERVER_HOST + "/collectionInformation" +
                "/queryCollectionInformationByUidPrimaryid").tag(this)
                .params("primaryid", modal.getFid())
                .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("是否收藏 返回结果s" + s);

                        JSONObject jsonObject = JSONObject.parseObject(s);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.size() == 0) return;
                        my_collection.setBackground(getResources().getDrawable(R.mipmap.aixin_pink));
                        m_collection = true;

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(ProductHomeActivity.this, "请检查网络或遇到未知错误！",
                                Toast.LENGTH_LONG).show();
                    }
                });


    }
    private void addHistory() {
        OkGo.post(Constant.SERVER_HOST + "/buyingPurchase/addVisitor").tag(this)
                .params("pid", modal.getFid())
                .params("productType",modal.getStatus())
                .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("添加人气 返回结果s" + s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(ProductHomeActivity.this, "请检查网络或遇到未知错误！",
                                Toast.LENGTH_LONG).show();
                    }
                });


    }

    private void loadData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("productId", productId);
        OkGo.post(Constant.SERVER_HOST + "/fplHouse/queryFplHousePOByProductId").params(params).tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i("response:", s);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        if (jsonObject.getInteger("code") == 0) {
                            HouseDetailBean productBean = JSONObject.parseObject(s,
                                    HouseDetailBean.class);
                            modal = productBean.getData();
                        } else {
                            Toast.makeText(ProductHomeActivity.this, jsonObject.getString("msg"),
                                    Toast.LENGTH_LONG).show();
                        }

                        initData();
                        ifCollection();
                        addHistory();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(ProductHomeActivity.this, "网络错误", Toast.LENGTH_LONG).show();

                    }
                });
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_price = findViewById(R.id.tv_price);
        tv_area = findViewById(R.id.tv_area);
        tv_times = findViewById(R.id.tv_times);
        tv_type = findViewById(R.id.tv_type);
        tv_address = findViewById(R.id.tv_address);
        tv_can_fenzu = findViewById(R.id.tv_can_fenzu);
        tv_zuqi = findViewById(R.id.tv_zuqi);
        tv_suitable = findViewById(R.id.tv_suitable);
        tv_intro = findViewById(R.id.tv_intro);
        tv_infrastructure = findViewById(R.id.tv_infrastructure);
        tv_peripheralPackage = findViewById(R.id.tv_peripheralPackage);

        tv_build_type = findViewById(R.id.tv_build_type);
        tv_building = findViewById(R.id.tv_building);
        tv_all_area = findViewById(R.id.tv_all_area);
        tv_user_area = findViewById(R.id.tv_user_area);
        tv_all_floor = findViewById(R.id.tv_all_floor);
        tv_sale_floor = findViewById(R.id.tv_sale_floor);
        tv_diceng_height = findViewById(R.id.tv_diceng_height);
        tv_shangceng_height = findViewById(R.id.tv_shangceng_height);
        tv_xingche_zaizhong = findViewById(R.id.tv_xingche_zaizhong);
        tv_huoti = findViewById(R.id.tv_huoti);
        tv_huotizaizhong = findViewById(R.id.tv_huotizaizhong);
        my_collection = findViewById(R.id.my_collection);
        my_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_collection) return;
                my_collection.setBackground(getResources().getDrawable(R.mipmap.aixin_pink));
//                List<HouseDetailBean.DataBean.PictureStorage> modal1=modal.getPictureStorage();
                LogUtils.d("uid : " + BaseApplication.getSpUtils().getInt("uid")
                        + "primaryid : " + modal.getFid() + "token : " + BaseApplication.getSpUtils().getString("token"));
                OkGo.post(Constant.SERVER_HOST + "/collectionInformation/addCollectionInformation"
                ).tag(this)
                        .params("primaryid", modal.getFid())
                        .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                        .params("token", BaseApplication.getSpUtils().getString("token"))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                LogUtils.d("收藏 返回结果s" + s);
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                Toast.makeText(ProductHomeActivity.this, "请检查网络或遇到未知错误！",
                                        Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });


        iv_call = findViewById(R.id.iv_call);
        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modal == null || null == modal.getContactPhone() || "".equals(modal.getContactPhone())) {
                    LogUtils.d("电话号码为空 phone");
                    return;
                }
                LogUtils.d("电话号码不为空 phone:" + modal.getContactPhone());

                lastClickTime = System.currentTimeMillis();
                ActivityCompat.requestPermissions(ProductHomeActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, 139);


                //检查有没有拨打电话的权限
                if (ActivityCompat.checkSelfPermission(ProductHomeActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[]
                    //   permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the
                    // documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                //开始拨打号码
                Intent intent = new Intent(Intent.ACTION_CALL,
                        Uri.parse("tel:" + modal.getContactPhone()));
                if (ActivityCompat.checkSelfPermission(ProductHomeActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[]
                    //   permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the
                    // documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });
        iv_chat = findViewById(R.id.iv_chat);
        iv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /*
               Intent intent = new Intent();
               String targetId = userInfo.getUserName();
               intent.putExtra(BaseApplication.TARGET_ID, targetId);
               intent.putExtra(BaseApplication.CONV_TITLE, userInfo.getNickname());
               intent.putExtra(BaseApplication.TARGET_APP_KEY, conv.getTargetAppKey());
               intent.putExtra(BaseApplication.DRAFT, adapter.getDraft(conv.getId()));

               intent.setClass(mContext, ChatActivity.class);
               mContext.startActivity(intent);
               */
            }
        });

        iv_share = findViewById(R.id.iv_share);
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopwindow();
            }
        });

        mViewPaper = (ViewPager) findViewById(R.id.vp);
        mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

        });


        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
                new ViewPageTask(),
                6,
                6,
                TimeUnit.SECONDS);

        img_moren_bannar = findViewById(R.id.img_moren_bannar);
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        img_moren_bannar.measure(w, h);
        lunboHeight = img_moren_bannar.getMeasuredHeight();
        int width = img_moren_bannar.getMeasuredWidth();

    }

    private void initTitle() {
        common_top_back = findViewById(R.id.common_top_back);
        common_top_back.setOnClickListener(v -> finish());
    }

    private void initData() {
        if (modal == null) return;
        tv_title.setText(modal.getTitle());
        tv_price.setText(modal.getRent() + modal.getRentUnit());
        tv_area.setText(modal.getArea() + modal.getAreaUnit());
        tv_times.setText(modal.getSubleaseType() + "次");//浏览多少次，有问题，需改
        initLunBo(modal.getPictureStorage());
        if (modal.getTypes() == 1) {
            tv_type.setText("厂房出租");
        } else if (modal.getTypes() == 2) {
            tv_type.setText("仓库出租");
        } else if (modal.getTypes() == 3) {
            tv_type.setText("土地出租");
        }
        tv_address.setText(modal.getRegion() + modal.getAddress());
        tv_zuqi.setText(modal.getLeasePeriod());
        tv_can_fenzu.setText("是");//有问题
        tv_suitable.setText(modal.getSuitableBusiness());
        tv_intro.setText(modal.getDetailedDescription());

        tv_infrastructure.setText(modal.getInfrastructure());
        tv_peripheralPackage.setText(modal.getPeripheralPackage());
    }

    private void initLunBo(List<HouseDetailBean.DataBean.PictureStorage> list) {
        LogUtils.d("initLunBo");
        images = new ArrayList<ImageView>();
        //第一张图
        ImageView imageView = new ImageView(this);
        //imageView.setBackgroundResource(imageIds[i]);
        Picasso.with(this)
                .load(Constant.SERVER_PIC_HOSTds + modal.getTitlePicture())
                .resize(screenWidth, lunboHeight)
                .into(imageView);
        LogUtils.d("path:" + Constant.SERVER_PIC_HOSTds + modal.getTitlePicture());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   addAdClickNum(ad);
                // Intent i = new Intent(mContext,StoreHomeActivity.class);
                //  i.putExtra("store_id",ad.getStore_id());
                //  startActivity(i);

            }
        });
        images.add(imageView);


        //显示的图片

        for (int i = 0; i < list.size(); i++) {
            final HouseDetailBean.DataBean.PictureStorage ad = list.get(i);
            LogUtils.d("显示图片：" + ad.getUrl());
            ImageView imageView2 = new ImageView(this);
            //imageView.setBackgroundResource(imageIds[i]);
            Picasso.with(this)
                    .load(Constant.SERVER_PIC_HOSTds + ad.getUrl())
                    .resize(screenWidth, lunboHeight)
                    .into(imageView2);


            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //   addAdClickNum(ad);
                    // Intent i = new Intent(mContext,StoreHomeActivity.class);
                    //  i.putExtra("store_id",ad.getStore_id());
                    //  startActivity(i);

                }
            });
            images.add(imageView2);
        }


        if (adapter == null) {
            adapter = new ViewPagerAdapter();
            mViewPaper.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }


    }

    private Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            mViewPaper.setCurrentItem(currentItem);
        }

    };

    /**
     * 图片轮播任务
     *
     * @author liuyazhuang
     */
    private class ViewPageTask implements Runnable {

        @Override
        public void run() {
            currentItem = (currentItem + 1) % images.size();
            mHandler.sendEmptyMessage(0);
        }
    }

    /**
     * 自定义Adapter
     *
     * @author liuyazhuang
     */
    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            // TODO Auto-generated method stub
//          super.destroyItem(container, position, object);
//          view.removeView(view.getChildAt(position));
//          view.removeViewAt(position);
            view.removeView(images.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            // TODO Auto-generated method stub
            view.addView(images.get(position));
            return images.get(position);
        }

    }

    @Override
    public void onStop() {
        LogUtils.d("首页onStop");
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
        super.onDestroy();
    }


    /**
     * 显示popupWindow
     */
    private void showPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_share, null);
        Button btn_close = popupView.findViewById(R.id.btn_close);


        RelativeLayout share_wechat = popupView.findViewById(R.id.share_wechat);
        RelativeLayout share_wechat_pengyouquan = popupView.findViewById(R.id.share_wechat_pengyouquan);
        RelativeLayout share_qq = popupView.findViewById(R.id.share_qq);
        RelativeLayout share_qqkongjian = popupView.findViewById(R.id.share_qqkongjian);

        mPopupWindow = new PopupWindow(popupView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(false);
        // 设置popWindow的显示和消失动画
        mPopupWindow.setAnimationStyle(R.style.popup_anim);

        // 在底部显示
        mPopupWindow.showAtLocation(findViewById(R.id.img_moren_bannar),
                Gravity.BOTTOM, 0, 0);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        //popWindow消失监听方法
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
            }
        });


        share_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(Wechat.NAME);
            }
        });
        share_wechat_pengyouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(WechatMoments.NAME);
            }
        });

        share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(QQ.NAME);
            }
        });
        share_qqkongjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(QZone.NAME);
            }
        });
    }


    private void showShare(String platform) {
        String url = "";
        if(modal.getTypes()==1){
            url = "http://kfddm.zhanchengwlkj.com/changfang-xq.html?productId="+productId;
        }else if(modal.getTypes()==2){
            url = "http://kfddm.zhanchengwlkj.com/cangku-xq.html?productId="+productId;
        }else if(modal.getTypes()==3){
            url = "http://kfddm.zhanchengwlkj.com/tudi-xq.html?productId="+productId;
        }
        LogUtils.d("imagepath:"+modal.getTitlePicture());
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("【推荐】厂房仓库土地出租出售");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(modal.getTitle());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(modal.getTitlePicture());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/kufangdidi/data/ic_launcher.png");//确保SDcard下面存在此张图片
        //oks.setImagePath(path);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我在这里找到了附近心仪的厂房，价格很实惠");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("库房滴滴");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(Constant.SERVER_DOWNLOAD_HOST+"download/index.html?name=kufangdidi");
        // 启动分享GUI
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                LogUtils.d("分享成功");

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                LogUtils.d("分享错误"+i+" platform:"+platform.getName());
                LogUtils.d(throwable.getLocalizedMessage());
                LogUtils.d(throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                LogUtils.d("分享取消");
            }
        });
        //启动分享
        oks.show(this);
    }

}
