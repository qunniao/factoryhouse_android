package com.kufangdidi.www.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kufangdidi.www.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBaiKeListView extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_baike_listview, container, false);
    }

}
