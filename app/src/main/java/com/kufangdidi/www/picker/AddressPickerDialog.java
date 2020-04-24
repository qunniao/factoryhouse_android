package com.kufangdidi.www.picker;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.google.gson.Gson;
import com.kufangdidi.www.R;
import com.kufangdidi.www.bean.RegionAreaBean;
import com.kufangdidi.www.bean.RegionChoosedBean;
import com.kufangdidi.www.bean.RegionCityBean;
import com.kufangdidi.www.bean.RegionProvinceBean;
import com.kufangdidi.www.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AddressPickerDialog extends Dialog implements View.OnClickListener {

    private WheelView wvProvince, wvCity, wvArea;
    //弹出框的标题
    private String title;

    private Context mContext;

    private List<RegionProvinceBean> provinceBeans = null;

    //区域字段
    private String province_name;
    private String city_name;
    private String county_name;

    public AddressPickerDialog(Context context, String title, String province_name,String city_name,String county_name,AddressPickerDialog.onAreaPickerDialogClickListener listener) {
        // 在构造方法里, 传入主题
        super(context, R.style.Dialog);
        mContext = context;
        this.province_name = province_name;
        this.city_name = city_name;
        this.county_name = county_name;
        // 拿到Dialog的Window, 修改Window的属性

        Window window = getWindow();
        assert window != null;
        window.getDecorView().setPadding(0, 0, 0, 0);

        //获取当前屏幕的宽高
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();

        // 获取Window的LayoutParams
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        //attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.height = dm.heightPixels / 3;//默认显示高度为屏幕的三分之一
       // attributes.height = dm.heightPixels;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
       // attributes.verticalMargin = 0.1f;
        attributes.dimAmount = 0.3f;

        // 一定要重新设置, 才能生效
        window.setAttributes(attributes);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        this.title = title;
        this.listener = listener;

    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wheelview_address);

        initView();
        initData();
    }

    private void initView() {
       // TextView tvAddressTitle = findViewById(R.id.tv_addresspicker_title);
        wvProvince = findViewById(R.id.wheelv_province);
        wvCity = findViewById(R.id.wheelv_city);
        wvArea = findViewById(R.id.wheelv_area);

        findViewById(R.id.tv_addresspicker_confirm).setOnClickListener(this);
       // findViewById(R.id.tv_addresspicker_exit).setOnClickListener(this);

       // if (!TextUtils.isEmpty(title)) tvAddressTitle.setText(title);
    }

    private RegionProvinceBean curPBean;
    private RegionCityBean curCBean;
    private RegionAreaBean curABean;

    private void initData() {
        String regionStr = getLocalJson();
        try {
            JSONObject jsonObject = new JSONObject(regionStr);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            if(provinceBeans==null)provinceBeans = new ArrayList<>();
            provinceBeans.clear();
            Gson gson = new Gson();
            RegionProvinceBean modal_province;
            RegionCityBean modal_city;
            for(int i=0;i<jsonArray.length();i++){
                modal_province = gson.fromJson(jsonArray.get(i).toString(), RegionProvinceBean.class);
                JSONObject jsonObject_province = new JSONObject(jsonArray.get(i).toString());

                //城市列表
                JSONArray jsonArray_city = jsonObject_province.getJSONArray("subarea");
                List<RegionCityBean> list_city = new ArrayList<>();
                for(int c=0;c<jsonArray_city.length();c++){
                    modal_city = gson.fromJson(jsonArray_city.get(c).toString(), RegionCityBean.class);
                    JSONObject jsonObject_city = new JSONObject(jsonArray_city.get(c).toString());

                    //区域列表
                    JSONArray jsonArray_area = jsonObject_city.getJSONArray("subarea");
                    List<RegionAreaBean> list_area = new ArrayList<>();
                    RegionAreaBean quanbu = new RegionAreaBean();
                    quanbu.setAreacode("0");
                    quanbu.setAreaname("全部");
                    list_area.add(quanbu);
                    for(int a=0;a<jsonArray_area.length();a++){
                        list_area.add(gson.fromJson(jsonArray_area.get(a).toString(), RegionAreaBean.class));
                    }
                    modal_city.setSubarea(list_area);
                    list_city.add(modal_city);
                }
                modal_province.setSubarea(list_city);
                provinceBeans.add(modal_province);
                if(modal_province.getAreaname().equals(province_name)){
                    curPBean = modal_province;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.d("json error");
        }

       // LogUtils.d("regionStr:"+regionStr);

        //curPBean = provinceBeans.get(0);
        wvProvince.setCyclic(false);
        wvCity.setCyclic(false);
        wvArea.setCyclic(false);

        ArrayList<String> pList = new ArrayList<>();
        final ArrayList<String> cList = new ArrayList<>();
        final ArrayList<String> aList = new ArrayList<>();


        //curPBean = provinceBeans.get(0);
        for (RegionProvinceBean p : provinceBeans) {
            pList.add(p.getAreaname());
        }
        //初始化当前省位置
        int province_position = 0;
        for(int i=0;i<pList.size();i++){
            if(province_name.equals(pList.get(i))){
                province_position = i;
            }
        }
        wvProvince.setAdapter(new ArrayWheelAdapter<>(pList));
        wvProvince.setCurrentItem(province_position);

        int city_position = 0;
        for(int i=0;i<curPBean.getSubarea().size();i++){
            if(city_name.equals(curPBean.getSubarea().get(i).getAreaname())){
                curCBean = curPBean.getSubarea().get(i);
                city_position = i;
            }
        }
       // curCBean = (RegionCityBean) curPBean.getSubarea().get(0);
        for (RegionCityBean c : curPBean.getSubarea()) {
            cList.add(c.getAreaname());
        }

        wvCity.setAdapter(new ArrayWheelAdapter<>(cList));
        wvCity.setCurrentItem(city_position);

        int area_position = 0;
        for(int i=0;i<curCBean.getSubarea().size();i++){
            if(county_name.equals(curCBean.getSubarea().get(i).getAreaname())){
                curABean = curCBean.getSubarea().get(i);
                area_position = i;
            }
        }
       // curABean = (RegionAreaBean) curCBean.getSubarea().get(0);

        for (RegionAreaBean a : curCBean.getSubarea()) {
            aList.add(a.getAreaname());
        }
        wvArea.setAdapter(new ArrayWheelAdapter<>(aList));
        wvArea.setCurrentItem(area_position);

        wvProvince.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                curPBean = provinceBeans.get(index);

                //联动第二级
                cList.clear();
                List<RegionCityBean> cBeans = curPBean.getSubarea();
                curCBean = cBeans.get(0);
                for (RegionCityBean c : cBeans) {
                    cList.add(c.getAreaname());
                }
                wvCity.setAdapter(new ArrayWheelAdapter<>(cList));
                wvCity.setCurrentItem(0);
                //联动第三级
                aList.clear();
                List<RegionAreaBean> aBeans = curCBean.getSubarea();
                curABean = aBeans.get(0);
                for (RegionAreaBean a : aBeans) {
                    aList.add(a.getAreaname());
                }
                wvArea.setAdapter(new ArrayWheelAdapter<>(aList));
                wvArea.setCurrentItem(0);
            }
        });
        wvCity.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                curCBean = (RegionCityBean) curPBean.getSubarea().get(index);

                //联动第三级
                aList.clear();
                List<RegionAreaBean> aBeans = curCBean.getSubarea();
                curABean = aBeans.get(0);
                for (RegionAreaBean a : aBeans) {
                    aList.add(a.getAreaname());
                }
                wvArea.setAdapter(new ArrayWheelAdapter<>(aList));
                wvArea.setCurrentItem(0);
            }
        });
        wvArea.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                curABean = (RegionAreaBean) curCBean.getSubarea().get(index);
            }
        });
    }

    @Override
    public void onClick(View v) {
        RegionChoosedBean bean = new RegionChoosedBean();
        switch (v.getId()) {
            case R.id.tv_addresspicker_confirm:
                bean.setEmpty(false);
                bean.setpCode(curPBean.getAreacode());
                bean.setpName(curPBean.getAreaname());
                bean.setcCode(curCBean.getAreacode());
                bean.setcName(curCBean.getAreaname());
                bean.setaCode(curABean.getAreacode());
                bean.setaName(curABean.getAreaname());
                listener.onChooseClick(bean);
                break;
                /*
            case R.id.tv_addresspicker_exit:
                bean.setEmpty(true);
                listener.onChooseClick(bean);
                break;
                */
        }
    }

    private AddressPickerDialog.onAreaPickerDialogClickListener listener;

    public interface onAreaPickerDialogClickListener {
        void onChooseClick(RegionChoosedBean bean);
    }

    /**
     * 获取本地json文件
     *
     * @return 本地json内容的字符串
     */
    private String getLocalJson() {
        StringBuilder builder = new StringBuilder();
        try {
            AssetManager assetManager = mContext.getAssets(); // 获得assets资源管理器(assets中的文件无法直接访问,可以使用AssetManager访问)
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open("city.json"), "UTF-8");
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
