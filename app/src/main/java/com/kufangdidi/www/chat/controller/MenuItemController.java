package com.kufangdidi.www.chat.controller;

import android.content.Intent;
import android.view.View;

import com.kufangdidi.www.fragment.FragmentTwo;

/**
 * Created by ${chenyn} on 2017/4/9.
 */

public class MenuItemController implements View.OnClickListener {
    private FragmentTwo mFragment;

    public MenuItemController(FragmentTwo fragment) {
        this.mFragment = fragment;
    }

    //会话界面的加号
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            default:
                break;
        }

    }
}
