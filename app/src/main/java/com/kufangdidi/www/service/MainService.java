package com.kufangdidi.www.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.kufangdidi.www.BuildConfig;
import com.kufangdidi.www.activity.user.UserLoginActivity;
import com.kufangdidi.www.app.Api;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.app.Constants;
import com.kufangdidi.www.chat.database.UserEntry;
import com.kufangdidi.www.chat.utils.SharePreferenceManager;
import com.kufangdidi.www.database.DBManager;
import com.kufangdidi.www.modal.LoginBean;
import com.kufangdidi.www.modal.UserModal;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.FileUtils;
import com.kufangdidi.www.utils.LogUtils;
import com.kufangdidi.www.utils.common;

import java.io.File;
import java.util.HashMap;
import java.util.Timer;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class MainService extends Service {
    DBManager db;
    private Timer timer;
    Gson gson;
    //通知栏进度条
    private NotificationManager mNotificationManager=null;
    private Notification mNotification;
    // 通知渠道的id
    private String NotificationChannelId = "1";
    private NotificationChannel channel;

    public static int storeUpdateLocationOpen = -1;


    //用户相关的方法常量
    public final static int initService = 20;
    public final static int registerSuccess = 21;
    public final static int loginSuccess = 22;
    public final static int loginOut = 24;

    public final static int DOWNLOAD_APP = 25;

    //用户地址相关常量
    public final static int UserAddressDeleteById = 30;
    public final static int UserAddressAdd = 31;
    public final static int UserAddressUpdateById = 32;

    public final static int OrderCancelById = 212;
    public final static int downloadApp = 216;
    public final static int userUpdateInfo = 217;

    //极光
    private UserInfo userInfo;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 定义一个Handler，用于处理下载线程与UI间通讯
            switch (msg.what) {
                case 1:

                    Bundle bundle = msg.getData();
                   // notificationInit("华修新版下载进度","已下载 "+bundle.getInt("progress")+"%");

                    break;
                case 2:
                    break;
                case -1:
                    break;
                default:
                    break;
            }
        }
    };
    private int sequence;


    public MainService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d("MainService服务 onCreate");

        /*
        if (Build.VERSION.SDK_INT >= 26) {
            //android 8.0+ 前台服务
            channel = new NotificationChannel(NotificationChannelId,"huaxiu_service",
                    NotificationManager.IMPORTANCE_HIGH);

            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(channel);

            mNotification = new Notification.Builder(getApplicationContext(),"1").build();

            MainService.this.startForeground(1, mNotification);
        }
*/
        db = new DBManager(this);

        gson = new Gson();

        //极光聊天测试
       // loginJiGuang("kf_123456","kf_123456");
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent==null) {
            return super.onStartCommand(intent, flags, startId);
        }
            String act = intent.getAction();
            Bundle bundle = intent.getExtras();

            userActionMethod(bundle);


        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 用户相关的方法
     * @param bundle
     */
    private void userActionMethod(Bundle bundle) {
        switch (bundle.getInt("method")){
            case initService:
                initService();
                break;
            case loginSuccess:
                UserModal userModal = new UserModal();
                userModal.setUserPhone(BaseApplication.getSpUtils().getString("userPhone"));
                userModal.setUserPassword(BaseApplication.getSpUtils().getString("userPassword"));
                userModal.setJiguang_username(BaseApplication.getSpUtils().getString("jiguang_username"));
                userModal.setJiguang_password(BaseApplication.getSpUtils().getString("jiguang_password"));
                try{
                    db.delete();
                }catch(Exception e){
                    e.printStackTrace();
                }
                db.add(userModal);

                Intent intent1 = new Intent();
                intent1.setAction("loginSuccess");
                intent1.putExtra("broadcast",loginSuccess);
                sendBroadcast(intent1);
                LogUtils.d("登录成功");

                loginJiGuang(BaseApplication.getSpUtils().getString("jiguang_username"),BaseApplication.getSpUtils().getString("jiguang_password"));
                break;
            case loginOut:
                db.delete();
                BaseApplication.destoryActivityByAll();
                Intent intent = new Intent(this, UserLoginActivity.class);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case DOWNLOAD_APP:
                downloadApp(bundle.getString("url"));
                break;
            default:
                break;

        }
    }

    private void downloadApp(String url) {
        //设置你的操作事项
        FileUtils fileUtils = new FileUtils();
        fileUtils.deleteDirectory(Constant.CACHE_DATA_APK);
        fileUtils.creatSDDir(Constant.CACHE_DATA_APK);
        String sd = fileUtils.getSDPATH();
        String path = Constant.CACHE_DATA_APK+ common.splitName(url);
        LogUtils.d("****MainService**** downloadApp path:"+path);
        File file = new File(sd+path);
        if(file.exists()){
            install(file);
        }else{
            LogUtils.d("****MainService**** Positive url:"+url+" path:"+sd+path);
            createDownloadTask(url,sd+path).start();
        }
    }

    private void install(File file){

        if (Build.VERSION.SDK_INT >= 26) {
            LogUtils.d("Build.VERSION.SDK_INT >= 26");
            //来判断应用是否有权限安装apk
            boolean installAllowed= getPackageManager().canRequestPackageInstalls();
            //有权限
            if (installAllowed) {
                LogUtils.d("有权限");
                //安装apk
                installApk(file);
            } else {
                LogUtils.d("无权限");
                Toast.makeText(MainService.this,"安装失败，请允许安装未知应用",Toast.LENGTH_LONG).show();
            }
        } else {
            installApk(file);
        }


    }

    private void installApk(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //7.0以上通过FileProvider
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LogUtils.d("AndroidN以及更高的版本");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);

        //  mNotificationManager.cancelAll();
    }

    //创建下载任务
    private BaseDownloadTask createDownloadTask(final String url, final String path) {

        return FileDownloader.getImpl().create(url)
                .setPath(path, false)
                .setCallbackProgressTimes(1000)
                .setMinIntervalUpdateSpeed(1000)
                .setListener(new FileDownloadSampleListener() {

                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.pending(task, soFarBytes, totalBytes);
                        LogUtils.d("下载 pending:"+soFarBytes);
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);
                       // sendMsg(1,(int)(((double)soFarBytes/totalBytes)*100),soFarBytes,totalBytes);// 更新进度条
                        LogUtils.d("下载 progress："+(int)(((double)soFarBytes/totalBytes)*100)+"%");
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        LogUtils.d("下载 error:"+e.getLocalizedMessage());
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                        LogUtils.d("下载 connected:");
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.paused(task, soFarBytes, totalBytes);
                        LogUtils.d("下载 paused:");
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        LogUtils.d("下载 completed:");
                        install(new File(path));
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        super.warn(task);
                    }
                });
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        LogUtils.d("MainService服务 onDestroy");
       // List<Conversation> list = JMessageClient.getConversationList();

    }

    private void loginJiGuang(final String name ,final String password){
        LogUtils.d("loginJiGuang:"+name);
        if(name==null || password==null)return;
        try{
            userInfo = JMessageClient.getMyInfo();
            if(userInfo!=null){
                LogUtils.d("极光信息不为空，无需登录");
                Intent intent1 = new Intent();
                intent1.setAction("jiguang_login");
                intent1.putExtra("broadcast", Constants.REFRESH_CHAT);
                sendBroadcast(intent1);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        JMessageClient.login(name, password, new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {
                if (responseCode == 0) {
                    SharePreferenceManager.setCachedPsw(password);
                    userInfo = JMessageClient.getMyInfo();
                    File avatarFile = userInfo.getAvatarFile();
                    //登陆成功,如果用户有头像就把头像存起来,没有就设置null
                    if (avatarFile != null) {
                        SharePreferenceManager.setCachedAvatarPath(avatarFile.getAbsolutePath());
                    } else {
                        SharePreferenceManager.setCachedAvatarPath(null);
                    }
                    String username = userInfo.getUserName();
                    String appKey = userInfo.getAppKey();
                    UserEntry userEntry = UserEntry.getUser(username, appKey);
                    if (null == userEntry) {
                        userEntry = new UserEntry(username, appKey);
                        userEntry.save();
                    }

                    LogUtils.d("极光登录成功"+appKey+"  id:"+userInfo.getUserID());
                    Intent intent1 = new Intent();
                    intent1.setAction("jiguang_login");
                    sendBroadcast(intent1);

                    if(BaseApplication.getSpUtils().getString("userName")!=null && !"".equals(BaseApplication.getSpUtils().getString("userName"))){
                        updateJiGuangInfo(BaseApplication.getSpUtils().getString("userName"));
                    }else {
                        updateJiGuangInfo(BaseApplication.getSpUtils().getString("userPhone"));
                    }

                } else {
                    LogUtils.d("极光登陆失败code:"+responseCode+" 用户名："+name+" 密码："+password+" msg:"+responseMessage);
                    registerJiGuang(name ,password);
                }
            }
        });

    }

    private void registerJiGuang(final String name,final String password){
        JMessageClient.register(name, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    LogUtils.d("极光注册成功");
                    SharePreferenceManager.setRegisterName(name);
                    SharePreferenceManager.setRegistePass(password);
                    loginJiGuang(name ,password); //注册成功后登录极光
                } else {
                    LogUtils.d("极光注册失败 "+s);
                    //HandleResponseCode.onHandle(mContext, i, false);
                }
            }
        });
    }

    private void logoutJiGuang(String name,String password){
        JMessageClient.logout();
    }

    private void updateJiGuangInfo(String nick){
        userInfo.setNickname(nick);
        JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {
                if (responseCode == 0) {
                    LogUtils.d("极光信息更新");
                } else {
                    LogUtils.d("极光信息更新失败");
                }
            }
        });
    }

    private void initService() {
        loginUser();

    }

    private void loginUser() {
        UserModal userModal = db.queryUser();
        if(null == userModal.getUserPhone() || "".equals(userModal.getUserPhone()))return;

        HashMap<String, String> params = new HashMap<>();
        LogUtils.d("登录手机号："+userModal.getUserPhone());
        LogUtils.d("登录密码"+userModal.getUserPassword());
        params.put("userPhone", userModal.getUserPhone());
        params.put("passWord", userModal.getUserPassword());
        OkGo.post(Api.LOGIN_URL).params(params)
                .tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                com.blankj.utilcode.util.LogUtils.d("登录接口返回 s:"+s);
                Log.i("headers:", response.headers().toString());
                Log.i("message:", response.message());
                Log.i("response:", response.toString());
                Log.i("response:", s);
                LoginBean loginBean = GsonUtils.fromJson(s, LoginBean.class);
                if (loginBean.getCode() != 0) {
                    LogUtils.d("登录结果："+loginBean.getMsg());
                } else {
                    /**
                     * uid : 0
                     * userName : null
                     * userPhone : 18702528557
                     * userSex : 0
                     * userPassword : null
                     * balance : 0.0
                     * createTime : null
                     * token : 9lnzgtft0zkCatmUlobRdQ==
                     */
                    BaseApplication.getSpUtils().put("uid", loginBean.getData().getUid());
                    BaseApplication.getSpUtils().put("userName", loginBean.getData().getUserName());
                    BaseApplication.getSpUtils().put("userPhone", loginBean.getData().getUserPhone());
                    BaseApplication.getSpUtils().put("userSex", loginBean.getData().getUserSex());
                    BaseApplication.getSpUtils().put("userPassword", userModal.getUserPassword());
                    BaseApplication.getSpUtils().put("balance", loginBean.getData().getBalance() + "");
                    BaseApplication.getSpUtils().put("createTime", loginBean.getData().getCreateTime());
                    BaseApplication.getSpUtils().put("token", loginBean.getData().getToken());
                    BaseApplication.getSpUtils().put("jiguang_username", loginBean.getData().getJiguangUsername());
                    BaseApplication.getSpUtils().put("jiguang_password", loginBean.getData().getJiguangPassword());
                    BaseApplication.getSpUtils().put("avatarUrl", loginBean.getData().getAvatarUrl());
                    BaseApplication.getSpUtils().put("mainArea", loginBean.getData().getMainArea());
                    BaseApplication.getSpUtils().put("introduction", loginBean.getData().getIntroduction());
                    BaseApplication.getSpUtils().put("type", loginBean.getData().getType());

                    userModal.setUserPhone(BaseApplication.getSpUtils().getString("userPhone"));
                    userModal.setUserPassword(BaseApplication.getSpUtils().getString("userPassword"));
                    userModal.setJiguang_username(BaseApplication.getSpUtils().getString("jiguang_username"));
                    userModal.setJiguang_password(BaseApplication.getSpUtils().getString("jiguang_password"));
                    try{
                        db.delete();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    db.add(userModal);

                    //登录成功

                    Intent intent1 = new Intent();
                    intent1.setAction("loginSuccess");
                    intent1.putExtra("broadcast",loginSuccess);
                    sendBroadcast(intent1);
                    LogUtils.d("初始化MainService,用户已经登录过，id:"+BaseApplication.getSpUtils().getInt("uid")+" token:"+BaseApplication.getSpUtils().getString("token"));

                    loginJiGuang(BaseApplication.getSpUtils().getString("jiguang_username"),BaseApplication.getSpUtils().getString("jiguang_password"));
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                LogUtils.d( "初始化登录遇到错误");
            }
        });
    }

}
