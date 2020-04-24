package com.kufangdidi.www.activity;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.kufangdidi.www.R;
import com.kufangdidi.www.adapter.RecyclerviewMoHuAdapter;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.bean.HouseBean;
import com.kufangdidi.www.modal.JingJiRenModal;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

import static com.mob.MobSDK.getContext;

@SuppressLint("Registered")
public class DianPuActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout layout1, layout2, layout3, layout4;
    private boolean bool1 = false;
    private boolean bool2 = false;
    private boolean bool3 = false;
    private boolean bool4 = false;
    private JingJiRenModal modal;
    private TextView jjr_name, jjr_pinglun, fangyuan, renqi, quyu;
    private RelativeLayout common_top_back;
    private CircleImageView touxiang;
    private int type;
    private RecyclerviewMoHuAdapter adapter;
    private RecyclerView my_recycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dianpu);
        BaseApplication.addDestoryActivity(this, "DianPuActivity");
        modal = (JingJiRenModal) getIntent().getSerializableExtra("modal");
        initTitle();
        initView();
        initData();
    }

    private void initTitle() {
        common_top_back = findViewById(R.id.common_top_back);
        common_top_back.setOnClickListener(this);
        TextView common_top_title = findViewById(R.id.common_top_title);
        common_top_title.setText("经纪人店铺");
    }

    private void myClick() {
//        if (bool1) {
//            ((TextView) findViewById(R.id.text1)).setTypeface(Typeface.defaultFromStyle
//            (Typeface.BOLD));
//            ((View) findViewById(R.id.view1)).setVisibility(View.VISIBLE);
//        } else {
//            ((TextView) findViewById(R.id.text1)).setTypeface(Typeface.defaultFromStyle
//            (Typeface.NORMAL));
//            ((View) findViewById(R.id.view1)).setVisibility(View.INVISIBLE);
//        }
        if (bool2) {
            ((TextView) findViewById(R.id.text2)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            ((View) findViewById(R.id.view2)).setVisibility(View.VISIBLE);
        } else {
            ((TextView) findViewById(R.id.text2)).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            ((View) findViewById(R.id.view2)).setVisibility(View.INVISIBLE);
        }
        if (bool3) {
            ((TextView) findViewById(R.id.text3)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            ((View) findViewById(R.id.view3)).setVisibility(View.VISIBLE);
        } else {
            ((TextView) findViewById(R.id.text3)).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            ((View) findViewById(R.id.view3)).setVisibility(View.INVISIBLE);
        }
        if (bool4) {
            ((TextView) findViewById(R.id.text4)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            ((View) findViewById(R.id.view4)).setVisibility(View.VISIBLE);
        } else {
            ((TextView) findViewById(R.id.text4)).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            ((View) findViewById(R.id.view4)).setVisibility(View.INVISIBLE);
        }

    }

    private void initView() {
        jjr_name = findViewById(R.id.jjr_name);
        jjr_pinglun = findViewById(R.id.jjr_pinglun);
//        layout1 = findViewById(R.id.layout1);
//        layout1.setOnClickListener(this);
        layout2 = findViewById(R.id.layout2);
        layout2.setOnClickListener(this);
        layout3 = findViewById(R.id.layout3);
        layout3.setOnClickListener(this);
        layout4 = findViewById(R.id.layout4);
        layout4.setOnClickListener(this);
        fangyuan = findViewById(R.id.fangyuan);
        renqi = findViewById(R.id.renqi);
        quyu = findViewById(R.id.quyu);
        touxiang = findViewById(R.id.touxiang);
        my_recycler = findViewById(R.id.my_recycler);
    }

    private void initData() {
        if (modal == null) return;
        if (modal.getAvatarUrl() != null && modal.getAvatarUrl().length() != 0) {
            Picasso.with(getContext())
                    .load(Constant.SERVER_PIC_HOSTds + modal.getAvatarUrl())
                    .into(touxiang);
        }
        if (modal.getUserName() == null || modal.getUserName().length() == 0) {
            jjr_name.setText(modal.getUserPhone());
        } else {
            jjr_name.setText(modal.getUserName());
        }
        if (modal.getIntroduction() != null && modal.getIntroduction().length() != 0) {
            jjr_pinglun.setText(modal.getIntroduction());
        }
        fangyuan.setText(modal.getFplHouse() + "");
        renqi.setText(modal.getPopularValue() + "");
        quyu.setText(modal.getMainArea() + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.layout1:
//                bool1 = true;
//                bool2 = bool3 = bool4 = false;
//                myClick();
//                break;
            case R.id.layout2:
                bool2 = true;
                bool1 = bool3 = bool4 = false;
                myClick();
                type = 1;
                myChuzuList();
                break;
            case R.id.layout3:
                bool3 = true;
                bool1 = bool2 = bool4 = false;
                myClick();
                type = 2;
                myChuzuList();
                break;
            case R.id.layout4:
                bool4 = true;
                bool1 = bool2 = bool3 = false;
                myClick();
                type = 3;
                myChuzuList();
                break;
            case R.id.common_top_back:
                finish();
                break;
        }
    }

    private void myChuzuList() {
        LogUtils.d("uid:"+modal.getUid()+"types"+type);
        OkGo.post(Constant.SERVER_HOST + "/fplHouse/queryFplHouseByUidAndTypes")
                .tag(this)
                .params("uid", modal.getUid())
                .params("types", type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("myChuzuList 返回结果s" + s);
                        com.alibaba.fastjson.JSONObject jsonObject =
                                com.alibaba.fastjson.JSONObject.parseObject(s);
                        if (jsonObject.getInteger("code") != 0) return;

                        HouseBean houseBean = JSONObject.parseObject(s, HouseBean.class);
                        List<HouseBean.DataBean> content = houseBean.getData();
                        LogUtils.d("content size:" + content.size());
                        List<HouseBean.DataBean> arraylist = new ArrayList<>();
                        for (HouseBean.DataBean con : content) {
                            LogUtils.d(con.getTitle() + " " + con.getProductId() + " " + con.getTypes());
                            arraylist.add(con);
                        }
                        if (adapter == null) {
                            adapter = new RecyclerviewMoHuAdapter(getContext(),arraylist);
                            adapter.setData(arraylist);
                            my_recycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                            my_recycler.setAdapter(adapter);
                            updateListHeight();
                        } else {
                            adapter.setData(arraylist);
                            adapter.notifyDataSetChanged();
                            my_recycler.setAdapter(adapter);
                            updateListHeight();
                        }
                    }
                });
    }
    private void updateListHeight(){
        int totalHeight = adapter.getItemCount()*450;
        ViewGroup.LayoutParams params = my_recycler.getLayoutParams();
        params.height = totalHeight;
        my_recycler.setLayoutParams(params);
        // recyclerView.scrollToPosition(productRvAdapter.getItemCount()-1);
    }
}
