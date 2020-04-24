package com.kufangdidi.www.chat.activity.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;

import com.kufangdidi.www.chat.utils.FileHelper;
import com.kufangdidi.www.chat.utils.SharePreferenceManager;
//import com.zhanchengwlkj.www.service.MainService;
import com.kufangdidi.www.utils.LogUtils;

import java.io.File;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by ${chenyn} on 2017/2/20.
 */

public class BaseFragment extends Fragment {
    private Dialog dialog;

    private UserInfo myInfo;
    protected float mDensity;
    protected int mDensityDpi;
    protected int mWidth;
    protected int mHeight;
    protected float mRatio;
    protected int mAvatarSize;
    private Context mContext;
    public Activity mActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
        //订阅接收消息,子类只要重写onEvent就能收到消息
        JMessageClient.registerEventReceiver(this);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mDensity = dm.density;
        mDensityDpi = dm.densityDpi;
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;
        mRatio = Math.min((float) mWidth / 720, (float) mHeight / 1280);
        mAvatarSize = (int) (50 * mDensity);

    }
    public void onEventMainThread(LoginStateChangeEvent event) {
        LogUtils.d("消息接收");

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

                /*
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.jmui_cancel_btn:
                                getActivity().finish();
                                break;
                            case R.id.jmui_commit_btn:
                                JMessageClient.login(SharePreferenceManager.getCachedUsername(), SharePreferenceManager.getCachedPsw(), new BasicCallback() {
                                    @Override
                                    public void gotResult(int responseCode, String responseMessage) {
                                        if (responseCode == 0) {
                                            Intent in = new Intent(mContext,MainService.class);
                                            in.setAction("userAction");
                                            in.putExtra("method",MainService.loginAgain);
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                getActivity().startForegroundService(in);
                                            } else {
                                                // Pre-O behavior.
                                                getActivity().startService(in);
                                            }
                                            dialog.dismiss();
                                        }
                                    }
                                });
                                break;
                        }
                    }
                };


                Intent in = new Intent(mContext,MainService.class);
                in.setAction("userAction");
                in.putExtra("method",MainService.loginOut);


                mContext.startService(in);
                dialog = DialogCreator.createLogoutStatusDialog(mContext, "您的账号在其他设备上登陆", listener);
                dialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.show();
                */
                break;
            case user_password_change:
              //  Intent intent = new Intent(mContext, LoginActivity.class);
               // startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        //注销消息接收
        JMessageClient.unRegisterEventReceiver(this);
        if (dialog != null) {
            dialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mActivity = context;
    }
}
