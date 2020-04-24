package com.kufangdidi.www.fragment;


import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kufangdidi.www.R;


/**
 * Created by admin on 2017/5/16.
 */

public class FragmentCenter extends Fragment implements View.OnClickListener {
    private Context mContext;

;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab1, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // mContext = getContext();

    }


    @Override
    public void onClick(View v) {

    }



    @Override
    public void onStart() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        super.onStart();

    }

    @Override
    public void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        super.onPause();
    }

    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()

        super.onResume();
    }

    @Override
    public void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()

        super.onDestroy();
    }

}
