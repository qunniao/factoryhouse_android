package com.kufangdidi.www.activity.user;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.kufangdidi.www.R;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.FileUtils;
import com.kufangdidi.www.utils.LogUtils;

import java.io.File;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * Created by hqyl on 2017/6/2.
 */

public class UserShareActivity extends Activity {

    private RelativeLayout goBack;
    private Button btn_submit;
    private View weiboView,qqView,qqFriendView,wechatView,wechatFriendView;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_share);
        BaseApplication.addDestoryActivity(this,"UserShareActivity");
        FileUtils fileUtils = new FileUtils();
        path = fileUtils.getSDPATH()+Constant.CACHE_DATA+"ic_launcher.png";
        File file = new File(path);
        if(!file.exists()){
            LogUtils.d("文件不存在");
            fileUtils.copyFilesFassets(UserShareActivity.this,"ic_launcher.png",path);
        }

        goBack = (RelativeLayout) findViewById(R.id.rl_goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        weiboView = findViewById(R.id.weiboView);
        weiboView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(SinaWeibo.NAME);
            }
        });

        qqView = findViewById(R.id.qqView);
        qqView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(QQ.NAME);
            }
        });

        qqFriendView = findViewById(R.id.qqFriendView);
        qqFriendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(QZone.NAME);
            }
        });

        wechatView = findViewById(R.id.wechatView);
        wechatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(Wechat.NAME);
            }
        });

        wechatFriendView = findViewById(R.id.wechatFriendView);
        wechatFriendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(WechatMoments.NAME);
            }
        });
        //透明状态栏
        setStatusBar();
    }

    //设置状态栏透明度
    public void setStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {            //Android 4.4 statusBar半透明
            getWindow ().addFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {            //Android 5.0以上 statusBar背景全透明
            Window window = getWindow ();
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView ().setSystemUiVisibility (
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (Color.TRANSPARENT);
        }
    }



    private void showShare(String platform) {
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("库房滴滴");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(Constant.SERVER_DOWNLOAD_HOST+"download/index.html?name=kufangdidi");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("厂房、仓库、土地出租出售");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        //oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/kufangdidi/data/ic_launcher.png");//确保SDcard下面存在此张图片
        oks.setImagePath(path);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(Constant.SERVER_DOWNLOAD_HOST+"download/index.html?name=kufangdidi");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我在这里找到了附近心仪的厂房，价格很实惠");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
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
                LogUtils.d("分享错误");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                LogUtils.d("分享取消");
            }
        });
        //启动分享
        oks.show(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }



}
