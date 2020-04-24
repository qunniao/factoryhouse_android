package com.kufangdidi.www.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.kufangdidi.www.R;
import com.kufangdidi.www.app.Api;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.app.Constants;
import com.kufangdidi.www.modal.AllCityBean;
import com.kufangdidi.www.picker.AddressPickTask;
import com.kufangdidi.www.utils.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import okhttp3.Call;
import okhttp3.Response;

public class QiuzuQiugouActivity extends AppCompatActivity implements View.OnClickListener {
    private List<AllCityBean> provinceList;
    private List<List<String>> cityList;
    private List<List<List<String>>> areaList;
    private TextView tvAreaSelected;
    private TextView tvMianjiSelected;
    private TextView tvUseSelected;
    private TextView tvDanSelected;
    private TextView tvPeitaoSelected;

    private PopupWindow mianjiPopupWindow;
    private PopupWindow funPopupWindow;
    private PopupWindow danPopupWindow;
    private PopupWindow peitaoPopupWindow;
    private View mianjiPopupView;
    private View funPopupView;
    private View danPopupView;
    private View peitaoPopupView;
    private TranslateAnimation animation;
    private View v_mask;
    private int type;
    private int fabuType;

    //区域
    private String quyu;
    //区域字段
    private String province_name = "浙江省";
    private String city_name = "杭州市";
    private String county_name = "西湖区";

    //面积
    private String mianji_start,mianji_end;
    private String mianji_unit_str;
    //功能用途
    private String yongtu;
    //单层面积
    private String danceng_mianji="";
    //配套需求
    private String peitao_xuqiu="";

    private ImageView fanhui;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_buying);
        BaseApplication.addDestoryActivity(this,"QiuzuQiugouActivity");
        setFabuType();
        initView();
        parseJson();
    }

    private void initView() {
        tvAreaSelected = findViewById(R.id.area_selected);
        tvMianjiSelected = findViewById(R.id.mianji_selected);
        tvUseSelected = findViewById(R.id.tv_useSelected);
        tvDanSelected = findViewById(R.id.tv_danSelected);
        tvPeitaoSelected = findViewById(R.id.tv_peitaoSelected);
        v_mask = findViewById(R.id.v_mask);
        fanhui=findViewById(R.id.qzqg_fanhui);
    }

    private void setFabuType() {
        TextView tvType = findViewById(R.id.tv_type);
        type = getIntent().getIntExtra("type", 0);
        String s1 = "";
        String s2 = "";
        fabuType = getIntent().getIntExtra("fabu_type", 0);
        switch (type) {
            case Constants.FABU_TYPE_TUDI:
                s1 = "土地";
                break;
            case Constants.FABU_TYPE_CANGKU:
                s1 = "仓库";
                break;
            case Constants.FABU_TYPE_CHANGFANG:
                s1 = "厂房";
                break;
        }
        switch (fabuType) {
            case 1:
                s2 = "求租";
                break;
            case 2:
                s2 = "求购";
                break;
        }
        tvType.setText(String.format("%s%s", s1, s2));
    }

    private void selectArea() {
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, option2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            quyu = cityList.get(options1).get(option2)
                    + areaList.get(options1).get(option2).get(options3);
            tvAreaSelected.setText(quyu);

        }).build();

        pvOptions.setPicker(provinceList, cityList, areaList);
        pvOptions.show();
    }

    //区域选择
    public void onAddressPicker() {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                LogUtils.d("数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                LogUtils.d("选择城市："+province.getAreaName()+" "+city.getAreaName());
                province_name = province.getAreaName();
                city_name = city.getAreaName();
                county_name = county.getAreaName();
                quyu = province.getAreaName()+city.getAreaName()+county.getAreaName();
                tvAreaSelected.setText(quyu);
            }
        });
        task.execute(province_name, city_name,county_name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_area:
               // selectArea();
                onAddressPicker();
                break;
            case R.id.ll_mianji:
                showMianjiPop();
                break;
            case R.id.ll_fun:
                showFunPop();
                break;
            case R.id.ll_dancengmianji:
                showDanPop();
                break;
            case R.id.ll_peitao:
                showPeitaoPop();
                break;
            case R.id.btnSubmit:
                submit();
                break;
            case  R.id.qzqg_fanhui:
                finish();
                break;
        }
    }

    private void submit() {
        if(null == quyu){
            Toast.makeText(this,"请选择区域",Toast.LENGTH_SHORT).show();
            return;
        }
        if(null == mianji_start || null == mianji_end){
            Toast.makeText(this,"请选择面积",Toast.LENGTH_SHORT).show();
            return;
        }
        if(null == yongtu){
            Toast.makeText(this,"请选择功能用途",Toast.LENGTH_SHORT).show();
            return;
        }
        String title = ((EditText)findViewById(R.id.et_title)).getText().toString();
        if("".equals(title)){
            Toast.makeText(this,"请输入标题",Toast.LENGTH_SHORT).show();
            return;
        }
        String name = ((EditText)findViewById(R.id.et_contact)).getText().toString();
        if("".equals(name)){
            Toast.makeText(this,"请输入联系人",Toast.LENGTH_SHORT).show();
            return;
        }
        String phone = ((EditText)findViewById(R.id.et_contactPhone)).getText().toString();
        if("".equals(phone)){
            Toast.makeText(this,"请输入手机号码",Toast.LENGTH_SHORT).show();
            return;
        }

        String layerHeight = ((EditText)findViewById(R.id.et_layerHeight)).getText().toString();
        if("".equals(layerHeight)){
            Toast.makeText(this,"请输入楼层高度",Toast.LENGTH_SHORT).show();
            return;
        }

        String loadBearing = ((EditText) findViewById(R.id.et_loadBearing)).getText().toString();
        if("".equals(loadBearing)){
            Toast.makeText(this,"请输入承重",Toast.LENGTH_SHORT).show();
            return;
        }

        String minimumCharge = ((EditText) findViewById(R.id.et_minimumCharge)).getText().toString();
        if("".equals(minimumCharge)){
            Toast.makeText(this,"请输入最小用电量",Toast.LENGTH_SHORT).show();
            return;
        }

        LogUtils.d("uid:"+BaseApplication.getSpUtils().getInt("uid")+" token:"+BaseApplication.getSpUtils().getString("token"));
        LogUtils.d("region:"+quyu);
        LogUtils.d("type:"+type);
        LogUtils.d("status:"+fabuType);
        LogUtils.d("areaCap:"+mianji_end);
        LogUtils.d("areaLower:"+mianji_start);
        LogUtils.d("functionalUse:"+yongtu);
        LogUtils.d("singleLayerArea:"+danceng_mianji);
        LogUtils.d("supportingDemand:"+peitao_xuqiu);
        LogUtils.d("title:"+title);
        LogUtils.d("layerHeight:"+layerHeight);
        LogUtils.d("loadBearing:"+loadBearing);
        LogUtils.d("minimumCharge:"+minimumCharge);
        LogUtils.d("detailedDescription:"+((EditText) findViewById(R.id.et_detailedDescription)).getText().toString());
        LogUtils.d("contact:"+name);
        LogUtils.d("contactPhone:"+phone);
        HashMap<String, String> params = new HashMap<>();
        params.put("region", quyu);
        params.put("type", type + "");
        params.put("status", fabuType + "");
        params.put("areaCap", mianji_end);
        params.put("areaLower",mianji_start );
        params.put("functionalUse",yongtu);
        params.put("singleLayerArea", danceng_mianji);
        params.put("supportingDemand", peitao_xuqiu);
        params.put("title", title);
        params.put("layerHeight", layerHeight);
        params.put("loadBearing",loadBearing );
        params.put("minimumCharge", minimumCharge);
        params.put("detailedDescription", ((EditText) findViewById(R.id.et_detailedDescription)).getText().toString());
        params.put("contact", name);
        params.put("contactPhone", phone);
        params.put("token",  BaseApplication.getSpUtils().getString("token"));
        params.put("uid",BaseApplication.getSpUtils().getInt("uid")+"");
        OkGo.post(Api.ADD_BUYING).params(params).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                JSONObject jsonObject = JSON.parseObject(s);
                if (jsonObject.getInteger("code") == 0) {
                    Toast.makeText(QiuzuQiugouActivity.this, "发布成功！", Toast.LENGTH_LONG).show();
                    BaseApplication.destoryActivity("Fabu1Activity");
                    BaseApplication.destoryActivity("Fabu2Activity");
                    finish();
                } else {
                    Toast.makeText(QiuzuQiugouActivity.this, jsonObject.getString("msg") + jsonObject.getInteger("code"), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Toast.makeText(QiuzuQiugouActivity.this, "请检查网络或遇到未知错误！", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showPeitaoPop() {
        if (peitaoPopupWindow == null) {
            peitaoPopupView = View.inflate(this, R.layout.popupwindow_information_assort, null);
            // 参数2,3：指明popupwindow的宽度和高度
            peitaoPopupWindow = new PopupWindow(peitaoPopupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            peitaoPopupWindow.setOnDismissListener(() -> {
                //隐藏遮罩
                v_mask.setVisibility(View.GONE);
            });
            // 设置背景图片， 必须设置，不然动画没作用
            peitaoPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            peitaoPopupWindow.setFocusable(true);
            // 设置点击popupwindow外屏幕其它地方消失
            peitaoPopupWindow.setOutsideTouchable(true);
            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
        }
        if (animation == null) {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);
        }
        //设置按钮点击监听
        RadioGroup rg_peitao = peitaoPopupView.findViewById(R.id.rg_peitao);
        rg_peitao.setOnCheckedChangeListener((group, checkedId) ->{
             tvPeitaoSelected.setText(((RadioButton) group.findViewById(checkedId)).getText());
             peitao_xuqiu = tvPeitaoSelected.getText().toString();
        });

//         设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        peitaoPopupWindow.showAtLocation(findViewById(R.id.ll_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        peitaoPopupView.startAnimation(animation);
        //显示遮罩
        v_mask.setVisibility(View.VISIBLE);
    }

    private void showDanPop() {
        if (danPopupWindow == null) {
            danPopupView = View.inflate(this, R.layout.popupwindow_information_monolayer_area, null);
            // 参数2,3：指明popupwindow的宽度和高度
            danPopupWindow = new PopupWindow(danPopupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            danPopupWindow.setOnDismissListener(() -> {
                //隐藏遮罩
                v_mask.setVisibility(View.GONE);
            });
            // 设置背景图片， 必须设置，不然动画没作用
            danPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            danPopupWindow.setFocusable(true);
            // 设置点击popupwindow外屏幕其它地方消失
            danPopupWindow.setOutsideTouchable(true);
        }
        // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
        if (animation == null) {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);
        }
        //设置按钮点击监听
//        RadioGroup rgUse = mianjiPopupView.findViewById(R.id.rg_use);
//        rgUse.setOnCheckedChangeListener((group, checkedId) -> tvUseSelected.setText(((RadioButton) group.findViewById(checkedId)).getText()));
        EditText et = danPopupView.findViewById(R.id.et);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!et.getText().toString().equals("")) {
                    tvDanSelected.setText(et.getText().toString() + "㎡");
                    danceng_mianji = et.getText().toString();
                } else {
                    tvDanSelected.setText("请选择");
                }
            }
        });
//         设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        danPopupWindow.showAtLocation(findViewById(R.id.ll_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        danPopupView.startAnimation(animation);
        //显示遮罩
        v_mask.setVisibility(View.VISIBLE);
    }

    private void showMianjiPop() {
        if (mianjiPopupWindow == null) {
            mianjiPopupView = View.inflate(this, R.layout.popupwindow_information_needarea, null);
            // 参数2,3：指明popupwindow的宽度和高度
            mianjiPopupWindow = new PopupWindow(mianjiPopupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            mianjiPopupWindow.setOnDismissListener(() -> {
                //隐藏遮罩
                v_mask.setVisibility(View.GONE);
            });
            // 设置背景图片， 必须设置，不然动画没作用
            mianjiPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mianjiPopupWindow.setFocusable(true);
            // 设置点击popupwindow外屏幕其它地方消失
            mianjiPopupWindow.setOutsideTouchable(true);
        }
        // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
        if (animation == null) {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);
        }
        RadioButton area_unit1 = mianjiPopupView.findViewById(R.id.area_unit1);
        RadioButton area_unit2 = mianjiPopupView.findViewById(R.id.area_unit2);
        //默认面积单位
        area_unit1.setSelected(true);
        area_unit2.setSelected(false);
        mianji_unit_str = area_unit1.getText().toString();
        area_unit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                area_unit1.setSelected(true);
                area_unit2.setSelected(false);
                mianji_unit_str = area_unit1.getText().toString();
            }
        });
        area_unit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                area_unit2.setSelected(true);
                area_unit1.setSelected(false);
                mianji_unit_str = area_unit2.getText().toString();
            }
        });
        //设置按钮点击监听
        RadioGroup rgMianji = mianjiPopupView.findViewById(R.id.rg_need);
        EditText etMax = mianjiPopupView.findViewById(R.id.et_max);
        EditText etMin = mianjiPopupView.findViewById(R.id.et_min);
        rgMianji.setOnCheckedChangeListener((group, checkedId) -> {
            if (!etMax.getText().toString().equals("") && !etMin.getText().toString().equals("")) {
                tvMianjiSelected.setText(etMin.getText().toString() + "-" + etMax.getText().toString() + ((RadioButton) rgMianji.findViewById(checkedId)).getText());
            } else if (etMax.getText().toString().equals("") && etMin.getText().toString().equals("")) {
                tvMianjiSelected.setText("请选择");
            } else {
                tvMianjiSelected.setText(etMin.getText().toString() + etMax.getText().toString() + ((RadioButton) rgMianji.findViewById(checkedId)).getText());
            }
        });
        etMax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mianji_end = etMax.getText().toString();
                if (rgMianji.getCheckedRadioButtonId() != -1) {
                    if (!etMax.getText().toString().equals("") && !etMin.getText().toString().equals("")) {
                        tvMianjiSelected.setText(etMin.getText().toString() + "-" + etMax.getText().toString() + ((RadioButton) rgMianji.findViewById(rgMianji.getCheckedRadioButtonId())).getText());

                    } else if (etMax.getText().toString().equals("") && etMin.getText().toString().equals("")) {
                        tvMianjiSelected.setText("请选择");
                    } else {
                        tvMianjiSelected.setText(etMin.getText().toString() + etMax.getText().toString() + ((RadioButton) rgMianji.findViewById(rgMianji.getCheckedRadioButtonId())).getText());
                    }
                }
            }
        });
        etMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mianji_start = etMin.getText().toString();
                if (rgMianji.getCheckedRadioButtonId() != -1) {
                    if (!etMax.getText().toString().equals("") && !etMin.getText().toString().equals("")) {
                        tvMianjiSelected.setText(etMin.getText().toString() + "-" + etMax.getText().toString() + ((RadioButton) rgMianji.findViewById(rgMianji.getCheckedRadioButtonId())).getText());
                    } else if (etMax.getText().toString().equals("") && etMin.getText().toString().equals("")) {
                        tvMianjiSelected.setText("请选择");
                    } else {
                        tvMianjiSelected.setText(etMin.getText().toString() + etMax.getText().toString() + ((RadioButton) rgMianji.findViewById(rgMianji.getCheckedRadioButtonId())).getText());
                    }
                }
            }
        });
//         设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        mianjiPopupWindow.showAtLocation(findViewById(R.id.ll_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mianjiPopupView.startAnimation(animation);
        //显示遮罩
        v_mask.setVisibility(View.VISIBLE);
    }

    private void showFunPop() {
        if (funPopupWindow == null) {
            funPopupView = View.inflate(this, R.layout.popupwindow_information_use, null);
            // 参数2,3：指明popupwindow的宽度和高度
            funPopupWindow = new PopupWindow(funPopupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            funPopupWindow.setOnDismissListener(() -> {
                //隐藏遮罩
                v_mask.setVisibility(View.GONE);
            });
            // 设置背景图片， 必须设置，不然动画没作用
            funPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            funPopupWindow.setFocusable(true);
            // 设置点击popupwindow外屏幕其它地方消失
            funPopupWindow.setOutsideTouchable(true);
        }
        // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
        if (animation == null) {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);
        }
        //设置按钮点击监听
        RadioGroup rgUse = funPopupView.findViewById(R.id.rg_use);
        rgUse.setOnCheckedChangeListener((group, checkedId) ->{
            tvUseSelected.setText(((RadioButton) group.findViewById(checkedId)).getText());
            yongtu = tvUseSelected.getText().toString();
        } );
//        EditText etMax = mianjiPopupView.findViewById(R.id.et_max);
//        EditText etMin = mianjiPopupView.findViewById(R.id.et_min);

//         设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        funPopupWindow.showAtLocation(findViewById(R.id.ll_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        funPopupView.startAnimation(animation);
        //显示遮罩
        v_mask.setVisibility(View.VISIBLE);
    }

    private void parseJson() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = getAssets().open("province.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = stringBuilder.toString();
        provinceList = JSONArray.parseArray(s, AllCityBean.class);
        cityList = new ArrayList<>();
        areaList = new ArrayList<>();
        for (int i = 0; i < provinceList.size(); i++) {
            List<String> pCityLists = new ArrayList<>();
            List<List<String>> city_area = new ArrayList<>();
            List<AllCityBean.CityBean> city = provinceList.get(i).getCity();
            for (int j = 0; j < city.size(); j++) {
                pCityLists.add(city.get(j).getName());
                List<String> area = city.get(j).getArea();
                city_area.add(area);
            }
            cityList.add(pCityLists);
            areaList.add(city_area);
        }
    }
}
