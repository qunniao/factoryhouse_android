package com.kufangdidi.www.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.kufangdidi.www.R;
import com.kufangdidi.www.adapter.RecyclerviewNewsAdapter;
import com.kufangdidi.www.modal.NewsModal;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/*
 * Effect:
 * Author: jww
 * Date: 2019/03/16
 */
public class PageFragment extends Fragment {

    private int menu_id;

    private List<NewsModal> data;
    private RecyclerviewNewsAdapter adapter;
    private RecyclerView recyclerView;

    private Context mContext;

    private int pageNum = 1;

    public static PageFragment newInstance(int menu_id) {
        Bundle args = new Bundle();
        args.putInt("menu_id", menu_id);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menu_id = getArguments().getInt("menu_id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_news_fragment_page, container, false);
        recyclerView = view.findViewById(R.id.recycle_news);
        findNewsListByAll();
//        initData();
        return view;
    }
//
//    private void initData(){
//        List<NewsModal> list2 = new ArrayList<>();
//        for(int i=0;i<5;i++){
//            NewsModal modal =  new NewsModal();
//            modal.setTitle("标题"+i+" 菜单："+menu_id);
//            list2.add(modal);
//        }
//
//        if(adapter==null){
//            adapter = new RecyclerviewNewsAdapter(getContext());
//            adapter.setdata(list2);
//            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//            recyclerView.setAdapter(adapter);
//        }else{
//            adapter.setdata(list2);
//            adapter.notifyDataSetChanged();
//        }
//    }

    private void findNewsListByAll() {
        LogUtils.d("进入findNewsListByAll");
        OkGo.post(Constant.SERVER_HOST + "/newsController/pageNews")
                .tag(this)
                .params("pageNum", pageNum)
                .params("pageSize", 50)
                .params("type", menu_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("findNewsListByAll 返回结果s:" + s);
                        JSONObject jsonObject = null;
                        JSONObject jsonData = null;
                        try {
                            jsonObject = new JSONObject(s);
                            jsonData = jsonObject.getJSONObject("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (jsonObject == null) return;
                        NewsModal modal = null;
                        List<NewsModal> list2 = new ArrayList<>();
                        try {
                            JSONArray jsonArray = jsonData.getJSONArray("content");
                            LogUtils.d("data size:" + jsonArray.length());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                modal = new Gson().fromJson(jsonArray.getString(i),
                                        NewsModal.class);
                                LogUtils.d(" menu1 title:" + modal.getTitle());
                                list2.add(modal);
                                //  list.add(new Gson().fromJson(jsonArray.getString(i),AdModel
                                // .class));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (adapter == null) {
                            adapter = new RecyclerviewNewsAdapter(getContext());
                            adapter.setdata(list2);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.setdata(list2);
                            adapter.notifyDataSetChanged();
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        LogUtils.d("返回结果出错:" + e.getMessage());
                    }
                });
    }


}
