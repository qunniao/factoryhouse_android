package com.kufangdidi.www.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kufangdidi.www.R;
import com.kufangdidi.www.activity.user.UserCollectionActivity;
import com.kufangdidi.www.activity.user.UserFaBuActivity;
import com.kufangdidi.www.activity.user.UserIDAuthenticationActivity;
import com.kufangdidi.www.activity.user.UserLoginActivity;
import com.kufangdidi.www.activity.user.UserSetUpActivity;
import com.kufangdidi.www.activity.user.UserShareActivity;
import com.kufangdidi.www.activity.user.UserVipActivity;
import com.kufangdidi.www.activity.user.UserWalletActivity;
import com.kufangdidi.www.activity.user.UserZhaoFangActivity;
import com.kufangdidi.www.activity.user.info.UserInfoActivity;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;
import com.squareup.picasso.Picasso;


/**
 * Created by admin on 2017/5/16.
 */

public class FragmentMe extends Fragment implements View.OnClickListener {
    private LinearLayout ll_wallet, ll_vip, ll_collection, ll_ID;
    private Context mContext;
    private View view;

    private RelativeLayout rl_user_fabu, rl_user_tuiguang, rl_user_zhaofang, rl_user_share, rl_set;

    public static TextView tv_name;
    private ImageView iv_touxiang;
    private RelativeLayout rl_user_info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab4, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d("FragmentTwo 启动");
        view = getView();
        initView();
    }

    private void initView() {
        ll_wallet = view.findViewById(R.id.ll_wallet);
        ll_wallet.setOnClickListener(this);
        ll_vip = view.findViewById(R.id.ll_vip);
        ll_vip.setOnClickListener(this);
        ll_collection = view.findViewById(R.id.ll_collection);
        ll_collection.setOnClickListener(this);
        ll_ID = view.findViewById(R.id.ll_id);
        ll_ID.setOnClickListener(this);

        rl_set = view.findViewById(R.id.rl_set);
        rl_set.setOnClickListener(this);
        rl_user_fabu = view.findViewById(R.id.rl_user_fabu);
        rl_user_fabu.setOnClickListener(this);
//        rl_user_tuiguang = view.findViewById(R.id.rl_user_tuiguang);
//        rl_user_tuiguang.setOnClickListener(this);
        rl_user_zhaofang = view.findViewById(R.id.rl_user_zhaofang);
        rl_user_zhaofang.setOnClickListener(this);
        rl_user_share = view.findViewById(R.id.rl_user_share);
        rl_user_share.setOnClickListener(this);

        tv_name = view.findViewById(R.id.tv_name);
        iv_touxiang = view.findViewById(R.id.iv_touxiang);
        rl_user_info = view.findViewById(R.id.rl_user_info);
        rl_user_info.setOnClickListener(this);
    }

    public void initData() {
        LogUtils.d("FragmentMe initData uid:" +BaseApplication.getSpUtils().getString("userName")+ BaseApplication.getSpUtils().getInt("uid")+BaseApplication.getSpUtils().getString("userPhone"));
        if (-1 == BaseApplication.getSpUtils().getInt("uid")) return;
        if (null == BaseApplication.getSpUtils().getString("userName")||BaseApplication.getSpUtils().getString("userName").length()==0) {
            tv_name.setText(BaseApplication.getSpUtils().getString("userPhone"));
        } else {
            tv_name.setText(BaseApplication.getSpUtils().getString("userName"));
        }
        LogUtils.d("touxiang" + BaseApplication.getSpUtils().getString("avatarUrl"));
        if (BaseApplication.getSpUtils().getString("avatarUrl") == null||BaseApplication.getSpUtils().getString("avatarUrl").length()==0) return;
            Picasso.with(getContext())
                    .load(Constant.SERVER_PIC_HOSTds + BaseApplication.getSpUtils().getString(
                            "avatarUrl"))
                    .into(iv_touxiang);

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_user_info:
                if (-1 == BaseApplication.getSpUtils().getInt("uid")) {
                    startActivity(new Intent(getActivity(), UserLoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), UserInfoActivity.class));
                }
                break;
            case R.id.ll_wallet:
                startActivity(new Intent(getActivity(), UserWalletActivity.class));
                break;
            case R.id.ll_vip:
                startActivity(new Intent(getActivity(), UserVipActivity.class));
                break;
            case R.id.ll_collection:
                startActivity(new Intent(getActivity(), UserCollectionActivity.class));
                break;
            case R.id.ll_id:
                startActivity(new Intent(getActivity(), UserIDAuthenticationActivity.class));
                break;
            case R.id.rl_user_fabu:
                startActivity(new Intent(getActivity(), UserFaBuActivity.class));
                break;
//            case R.id.rl_user_tuiguang:
//                startActivity(new Intent(getActivity(), UserTuiGuangActivity.class));
//                break;
            case R.id.rl_user_zhaofang:
                startActivity(new Intent(getActivity(), UserZhaoFangActivity.class));
                break;
            case R.id.rl_user_share:
                startActivity(new Intent(getActivity(), UserShareActivity.class));
                break;
            case R.id.rl_set:
                startActivity(new Intent(getActivity(), UserSetUpActivity.class));
                break;
        }
    }


}
