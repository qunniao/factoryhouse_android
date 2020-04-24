package com.kufangdidi.www.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.kufangdidi.www.R;
import com.kufangdidi.www.activity.user.UserLoginActivity;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.app.Constants;
import com.kufangdidi.www.chat.utils.DialogCreator;
import com.kufangdidi.www.chat.utils.FileHelper;
import com.kufangdidi.www.chat.utils.SharePreferenceManager;
import com.kufangdidi.www.chat.utils.ThreadUtil;
import com.kufangdidi.www.fragment.FragmentChat;
import com.kufangdidi.www.fragment.FragmentMe;
import com.kufangdidi.www.fragment.FragmentOne;
import com.kufangdidi.www.fragment.FragmentTwo;
import com.kufangdidi.www.modal.AppVersionModal;
import com.kufangdidi.www.service.MainService;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;
import com.kufangdidi.www.utils.MessageEvent;
import com.kufangdidi.www.utils.common;
import com.kufangdidi.www.view.DialogConfirm;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Fragment[] mFragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RelativeLayout rl_main_menu_1, rl_main_menu_2, rl_main_menu_3, rl_main_menu_4, main_fabu;
    private TextView main_menu_text_1, main_menu_text_2, main_menu_text_3, main_menu_text_4;
    private ImageView main_menu_img_1, main_menu_img_2, main_menu_img_3, main_menu_img_4;

    IntentFilter filter;

    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;
    private FragmentChat fragmentChat;
    private FragmentMe fragmentMe;
    private int mHeight, mWidth;
    private MyReceiver myReceiver;

    //消息未读数量
    private TextView mAllUnReadMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseApplication.addDestoryActivity(this,"MainActivity");
        EventBus.getDefault().register(this);
        LogUtils.d("首页启动");
        initActivity();
        setStatusBar();
        requestPermissions();
        //注册极光sdk的event用于接收各种event事件
        JMessageClient.registerEventReceiver(this);

        myReceiver = new MyReceiver();
        filter = new IntentFilter();
        filter.addAction("loginSuccess");
        filter.addAction("jiguang_login");
        filter.addAction("setUnReadMsg");
        filter.addAction("JPushReceive");
        registerReceiver(myReceiver, filter);

        Intent intent = new Intent(MainActivity.this, MainService.class);
        intent.setAction("userAction");
        intent.putExtra("method", MainService.initService);
        startService(intent);
        /*
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(intent);
        } else {
            // Pre-O behavior.
            startService(intent);
        }
        */
        findServerAppCode();
    }

    /**
     * android6.0以上申请动态权限
     */
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT>=23){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    ||ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED
                    ||ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    !=PackageManager.PERMISSION_GRANTED) {

                LogUtils.d("没有权限,请手动开启位置权限");
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                //builder.create().show();
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},510);

            }
        }
    }

    //检测最新版本
    private void findServerAppCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtils.d("调用服务端方法："+ Constant.SERVER_DOWNLOAD_HOST+"download/findAppNewVersion.html?name=kufangdidi");
                OkGo.get(Constant.SERVER_DOWNLOAD_HOST+"download/findAppNewVersion.html?name=kufangdidi")
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, okhttp3.Call call, okhttp3.Response response) {
                                if(s==null){return;}
                                LogUtils.d("新版本回调 s:"+s);
                                JSONObject jsonObject = null;
                                String nowV = common.getVersionCode(getApplicationContext());
                                AppVersionModal appVersion = null;
                                try {

                                    jsonObject = new JSONObject(s);
                                    appVersion = new Gson().fromJson(jsonObject.get("data").toString(),AppVersionModal.class);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(appVersion!=null && !appVersion.getAndroid_version().equals(nowV)){ //如果服务器的app版本名称和app版本名称不相同
                                    dialogConfirmByDownloadApp(appVersion);
                                }

                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                            }
                        });
            }
        }).start();

    }

    /**
     * 安装最新版本提示框
     * @param appVersion
     */
    private void dialogConfirmByDownloadApp(AppVersionModal appVersion) {
        DialogConfirm.Builder builder = new DialogConfirm.Builder(MainActivity.this);
        builder.setTitle("检测到新版本"+appVersion.getAndroid_version()+", 是否下载安装？");
        builder.setPositiveButton("安装", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //去MainService调用接口
                Intent intent = new Intent(MainActivity.this, MainService.class);
                intent.setAction("userAction");
                intent.putExtra("method", MainService.DOWNLOAD_APP);
                intent.putExtra("url",appVersion.getAndroid_uri());
                startService(intent);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("下次",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }


    //设置状态栏透明度
    public void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {            //Android 4.4 statusBar半透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {            //Android 5.0以上 statusBar背景全透明
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initActivity() {
        mAllUnReadMsg = (TextView) findViewById(R.id.all_unread_number);
        //注册广播接收器
        mFragments = new Fragment[4];
        fragmentManager = getSupportFragmentManager();

        fragmentOne = (FragmentOne) fragmentManager.findFragmentById(R.id.tab1);
        mFragments[0] = fragmentOne;

        fragmentTwo = (FragmentTwo) fragmentManager.findFragmentById(R.id.tab2);
        mFragments[1] = fragmentTwo;

        fragmentChat = (FragmentChat) fragmentManager.findFragmentById(R.id.tab3);
        mFragments[2] = fragmentChat;
        fragmentMe = (FragmentMe) fragmentManager.findFragmentById(R.id.tab4);
        mFragments[3] = fragmentMe;

        main_fabu = (RelativeLayout) findViewById(R.id.main_fabu);
        main_fabu.setOnClickListener(this);

        rl_main_menu_1 = (RelativeLayout) findViewById(R.id.main_menu_1);
        rl_main_menu_2 = (RelativeLayout) findViewById(R.id.main_menu_2);
        rl_main_menu_3 = (RelativeLayout) findViewById(R.id.main_menu_3);
        rl_main_menu_4 = (RelativeLayout) findViewById(R.id.main_menu_4);

        rl_main_menu_1.setOnClickListener(this);
        rl_main_menu_2.setOnClickListener(this);
        rl_main_menu_3.setOnClickListener(this);
        rl_main_menu_4.setOnClickListener(this);

        main_menu_text_1 = (TextView) findViewById(R.id.main_menu_text_1);
        main_menu_text_2 = (TextView) findViewById(R.id.main_menu_text_2);
        main_menu_text_3 = (TextView) findViewById(R.id.main_menu_text_3);
        main_menu_text_4 = (TextView) findViewById(R.id.main_menu_text_4);

        main_menu_img_1 = (ImageView) findViewById(R.id.main_menu_img_1);
        main_menu_img_2 = (ImageView) findViewById(R.id.main_menu_img_2);
        main_menu_img_3 = (ImageView) findViewById(R.id.main_menu_img_3);
        main_menu_img_4 = (ImageView) findViewById(R.id.main_menu_img_4);

        setFragmentIndicator();
        fragmentTransaction.show(mFragments[0]).commit();
        main_menu_text_1.setSelected(true);
        main_menu_img_1.setSelected(true);
    }

    private void setFragmentIndicator() {
        fragmentTransaction = fragmentManager.beginTransaction()
                .hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3]);
        main_menu_text_1.setSelected(false);
        main_menu_text_2.setSelected(false);
        main_menu_text_3.setSelected(false);
        main_menu_text_4.setSelected(false);

        main_menu_img_1.setSelected(false);
        main_menu_img_2.setSelected(false);
        main_menu_img_3.setSelected(false);
        main_menu_img_4.setSelected(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_menu_1:
                setFragmentIndicator();
                main_menu_text_1.setSelected(true);
                main_menu_img_1.setSelected(true);
                fragmentTransaction.show(mFragments[0]).commit();
                break;
            case R.id.main_menu_2:
                setFragmentIndicator();
                main_menu_text_2.setSelected(true);
                main_menu_img_2.setSelected(true);
                fragmentTransaction.show(mFragments[1]).commit();
                break;
            case R.id.main_menu_3:
                if (BaseApplication.getSpUtils().getString("token", null) == null) {
//                if(true){
                    ActivityUtils.startActivity(new Intent(this, UserLoginActivity.class));
                    return;
                }
                setFragmentIndicator();
                main_menu_text_3.setSelected(true);
                main_menu_img_3.setSelected(true);
                fragmentTransaction.show(mFragments[2]).commit();
                break;
            case R.id.main_menu_4:
                if (BaseApplication.getSpUtils().getString("token", null) == null) {
                    ActivityUtils.startActivity(new Intent(this, UserLoginActivity.class));
                    return;
                }
                //消息页面
                setFragmentIndicator();
                main_menu_text_4.setSelected(true);
                main_menu_img_4.setSelected(true);
                fragmentTransaction.show(mFragments[3]).commit();
                break;
            case R.id.main_fabu:
                //我的页面
                LogUtils.d("uid:"+BaseApplication.getSpUtils().getInt("uid"));
                if (-1 == BaseApplication.getSpUtils().getInt("uid")) {
                    ActivityUtils.startActivity(new Intent(this, UserLoginActivity.class));
                    return;
                }
                startActivity(new Intent(this, Fabu1Activity.class));
                break;

        }
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String act = intent.getAction();
            if ("jiguang_login".equals(act)) {
                fragmentChat.initListView();
                setUnReadMsg(JMessageClient.getAllUnReadMsgCount());
            } else if ("setUnReadMsg".equals(act)) {
                //设置未读消息
                setUnReadMsg(JMessageClient.getAllUnReadMsgCount());
            } else if ("JPushReceive".equals(act)) {
                //Toast.makeText(MainActivity.this,"收到推送",Toast.LENGTH_SHORT).show();
                //  Bundle bundle = intent.getExtras();
                //  String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                //  String msg = bundle.getString(JPushInterface.EXTRA_ALERT);
                // LogUtils.d("推送标题："+title+"  推送内容："+msg);
                //  notiPush(title,msg);
            }
            else if ("loginSuccess".equals(act)) {
                LogUtils.d("MyReceiver loginSuccess");
               fragmentMe.initData();
            }
        }
    }

    public void setUnReadMsg(final int count) {

        ThreadUtil.runInUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAllUnReadMsg != null) {
                    if (count > 0) {
                        mAllUnReadMsg.setVisibility(View.VISIBLE);
                        if (count < 100) {
                            mAllUnReadMsg.setText(count + "");
                        } else {
                            mAllUnReadMsg.setText("99+");
                        }

                    } else {
                        mAllUnReadMsg.setVisibility(View.GONE);
                    }
                    fragmentChat.refreshAdapter();
                }
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }


    public void onEventMainThread(LoginStateChangeEvent event) {
        final LoginStateChangeEvent.Reason reason = event.getReason();
        UserInfo myInfo = event.getMyInfo();
        if (myInfo != null) {
            String path;
            File avatar = myInfo.getAvatarFile();
            if (avatar != null && avatar.exists()) {
                path = avatar.getAbsolutePath();
            } else {
                path = FileHelper.getUserAvatarPath(myInfo.getUserName());
            }
            SharePreferenceManager.setCachedUsername(myInfo.getUserName());
            SharePreferenceManager.setCachedAvatarPath(path);
            JMessageClient.logout();
        }
        switch (reason) {
            case user_logout:
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.jmui_cancel_btn:
                                Intent intent = new Intent(MainActivity.this, UserLoginActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.jmui_commit_btn:
                                JMessageClient.login(SharePreferenceManager.getCachedUsername(), SharePreferenceManager.getCachedPsw(), new BasicCallback() {
                                    @Override
                                    public void gotResult(int responseCode, String responseMessage) {
                                        if (responseCode == 0) {
                                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });
                                break;
                        }
                    }
                };
                Dialog dialog = DialogCreator.createLogoutStatusDialog(MainActivity.this, "您的账号在其他设备上登陆", listener);
                dialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;
            case user_password_change:
                Intent intent = new Intent(MainActivity.this, UserLoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        //注销消息接收
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
        try{
            unregisterReceiver(myReceiver);
        }catch(Exception e){
            e.printStackTrace();
        }
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getN()) {
            case Constants.FRAGMENTONE_UPDATE_CITY:
                LogUtils.d("onMessageEvent 2" );
                fragmentOne.updateCity();
                break;
        }
    }
}
