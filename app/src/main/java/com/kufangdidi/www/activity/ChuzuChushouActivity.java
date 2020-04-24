package com.kufangdidi.www.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.kufangdidi.www.R;
import com.kufangdidi.www.adapter.GridImageAdapter;
import com.kufangdidi.www.app.Api;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.app.Constants;
import com.kufangdidi.www.modal.AllCityBean;
import com.kufangdidi.www.picker.AddressPickTask;
import com.kufangdidi.www.utils.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Response;


public class ChuzuChushouActivity extends AppCompatActivity implements View.OnClickListener, GridImageAdapter.onAddPicClickListener, BDLocationListener {

    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private RxPermissions rxPermissions;
    private List<LocalMedia> selectList;
    private List<AllCityBean> provinceList;
    private List<List<String>> cityList;
    private List<List<List<String>>> areaList;
    private PopupWindow fenzuPopupWindow;
    private PopupWindow zujinPopupWindow;
    private PopupWindow mianjiPopupWindow;
    private PopupWindow zuqiPopupWindow;
    private View fenzuPopupView;
    private View zujinPopupView;
    private View mianjiPopupView;
    private View zuqiPopupView;
    private TranslateAnimation animation;
    private View v_mask;
    private LinearLayout fenzu;
    private LinearLayout zuqi;
    private TextView tvChuType;
    private TextView tvAreaSelected;
    //区域
    private String quyu;
    //区域字段
    private String province_name = "浙江省";
    private String city_name = "杭州市";
    private String county_name = "西湖区";
    //分租
    private TextView tvFenzuSelected;



    private TextView tvZujinSelected;
    //租金
    private String zujin;
    private String zujin_unit = "元";

    private TextView tvMianjiSelected;
    //面积
    private String mianji;
    private String mianji_unit="平方米";

    private TextView tvZuqiSelected;
    //租期
    private String zuqi_str;

    private int fabuType;
    private int type;
    private LocationClient locationClient;
    private double latitude;
    private double longitude;
    private TextView et_latitude;
    private boolean canFenzu =true;

    private RadioGroup rgMianji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuzuchushou);
        BaseApplication.addDestoryActivity(this,"ChuzuChushouActivity");
        initView();
    }

    private void initView() {
        v_mask = findViewById(R.id.v_mask);
        fenzu = findViewById(R.id.ll_fenzu);
        zuqi = findViewById(R.id.ll_zuqi);
        tvChuType = findViewById(R.id.tv_chu_type);
        tvAreaSelected = findViewById(R.id.area_selected);
        tvFenzuSelected = findViewById(R.id.fenzu_selected);
        tvZujinSelected = findViewById(R.id.zujin_selected);
        tvMianjiSelected = findViewById(R.id.mianji_selected);
        tvZuqiSelected = findViewById(R.id.zuqi_selected);
        et_latitude = findViewById(R.id.et_latitude);

        rxPermissions = new RxPermissions(this);
        TextView common_top_title = findViewById(R.id.common_top_title);
        common_top_title.setText("填写信息");
        RelativeLayout common_top_back = findViewById(R.id.common_top_back);
        common_top_back.setOnClickListener(this);
        recyclerView = findViewById(R.id.rv_img);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, this);
        recyclerView.setAdapter(adapter);
        setFabuType();
        parseJson();
        initLocation();
    }

    private void initLocation() {
        locationClient = new LocationClient(this);
        locationClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认GCJ02
//GCJ02：国测局坐标；
//BD09ll：百度经纬度坐标；
//BD09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(1000);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
//可选，V7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
//        option.setIsNeedAddress(true);

//        option.setIsNeedLocationPoiList(true);

        locationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明


//mLocationClient为第二步初始化过的LocationClient对象
//调用LocationClient的start()方法，便可发起定位请求
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
                s2 = "出租";
                break;
            case 2:
                s2 = "出售";
                tvChuType.setText(s2);
                fenzu.setVisibility(View.GONE);
                zuqi.setVisibility(View.GONE);
                break;
        }
        tvType.setText(String.format("%s%s", s1, s2));
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

    private void getPictures() {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(10)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(3)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                //.previewVideo()// 是否可预览视频 true or false
                //.enablePreviewAudio() // 是否可播放音频 true or false
                //.isCamera()// 是否显示拍照按钮 true or false
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(false)// 是否压缩 true or false
                //.glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                //.withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                //.hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
                //.isGif()// 是否显示gif图片 true or false
                //.compressSavePath(getPath())//压缩图片保存地址
                //.freeStyleCropEnabled()// 裁剪框是否可拖拽 true or false
                //.circleDimmedLayer()// 是否圆形裁剪 true or false
                //.showCropFrame()// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                // .showCropGrid()// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                //.openClickSound()// 是否开启点击声音 true or false
                .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                .cropCompressQuality()// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
//                .rotateEnabled() // 裁剪是否可旋转图片 true or false
//                .scaleEnabled()// 裁剪是否可放大缩小图片 true or false
//                .videoQuality()// 视频录制质量 0 or 1 int
//                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                //videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                //.recordVideoSecond()//视频秒数录制 默认60s int
                //  .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_top_back:
                onBackPressed();
                break;
            case R.id.ll_area:
              //  selectArea();
                onAddressPicker();
                break;
            case R.id.ll_fenzu:
                showFenzuPop();
                break;
            case R.id.ll_zujin:
                showZujinPop();
                break;
            case R.id.ll_mianji:
                showMianjiPop();
                break;
            case R.id.ll_zuqi:
                showZuqiPop();
                break;
            case R.id.btnSubmit:
                submit();
                break;
            case R.id.et_latitude:
                Disposable subscribe = rxPermissions
                        .request(Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                        .subscribe(granted -> {
                            if (granted) { // Always true pre-M
                                // I can control the camera now
                                locationClient.start();
                            }
                        });

                break;
        }
    }

    private void submit() {
        if(null == quyu){
            Toast.makeText(this,"请选择区域",Toast.LENGTH_SHORT).show();
            return;
        }
        if(null == zujin){
            Toast.makeText(this,"请输入租金",Toast.LENGTH_SHORT).show();
            return;
        }
        if(null == mianji){
            Toast.makeText(this,"请输入面积",Toast.LENGTH_SHORT).show();
            return;
        }
        if(null == zuqi_str){
            //Toast.makeText(this,"请选择租期",Toast.LENGTH_SHORT).show();
            zuqi_str="1年";
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
        if(null ==selectList){
            Toast.makeText(this,"请至少添加一张图片",Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("types", type + "");
        params.put("region", quyu);
        params.put("status", fabuType + "");
        if (canFenzu)
            params.put("subleaseType", "0");
        else
            params.put("subleaseType", "1");
        params.put("rent", zujin);
        params.put("rentUnit", zujin_unit);
        params.put("area", mianji);
        params.put("areaUnit",mianji_unit);
        params.put("leasePeriod", zuqi_str);
        params.put("title",title);
        params.put("address", ((EditText) findViewById(R.id.et_address)).getText().toString());
        params.put("latitude", latitude + "");
        params.put("longitude", longitude + "");
        params.put("infrastructure", ((EditText) findViewById(R.id.et_infrastructure)).getText().toString());
        params.put("peripheralPackage", ((EditText) findViewById(R.id.et_peripheralPackage)).getText().toString());
        params.put("suitableBusiness", ((EditText) findViewById(R.id.et_suitableBusiness)).getText().toString());
        params.put("detailedDescription", ((EditText) findViewById(R.id.et_detailedDescription)).getText().toString());
        params.put("contact", name);
        params.put("contactPhone", phone);
        params.put("token", BaseApplication.getSpUtils().getString("token"));
        params.put("uid",BaseApplication.getSpUtils().getInt("uid")+"");
        params.put("buildingStructure", 0+"");
        List<File> files = new ArrayList<>();

        for (LocalMedia localMedia : selectList) {
            files.add(new File(localMedia.getPath()));
        }
        Log.i("files", files.size() + "");
        OkGo.post(Api.ADD_CHUZU_OR_CHUSHOU).params(params).addFileParams("files", files).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                JSONObject jsonObject = JSON.parseObject(s);
                if (jsonObject.getInteger("code") == 0) {
                    Toast.makeText(ChuzuChushouActivity.this, "发布成功！", Toast.LENGTH_LONG).show();
                    BaseApplication.destoryActivity("Fabu2Activity");
                    BaseApplication.destoryActivity("Fabu1Activity");
                    finish();
                } else {
                    Toast.makeText(ChuzuChushouActivity.this, jsonObject.getString("msg") + jsonObject.getInteger("code"), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Toast.makeText(ChuzuChushouActivity.this, "请检查网络或遇到未知错误！", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void selectArea() {
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, option2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String tx = provinceList.get(options1).getName() + cityList.get(options1).get(option2)
                    + areaList.get(options1).get(option2).get(options3);
            tvAreaSelected.setText(tx);
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
    public void onAddPicClick() {
        Disposable subscribe = rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        // I can control the camera now
                        getPictures();
                    }
                });
    }

    public void showFenzuPop() {
        if (fenzuPopupWindow == null) {
            fenzuPopupView = View.inflate(this, R.layout.popupwindow_fenzu, null);
            // 参数2,3：指明popupwindow的宽度和高度
            fenzuPopupWindow = new PopupWindow(fenzuPopupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            fenzuPopupWindow.setOnDismissListener(() -> {
                //隐藏遮罩
                v_mask.setVisibility(View.GONE);
            });
            // 设置背景图片， 必须设置，不然动画没作用
            fenzuPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            fenzuPopupWindow.setFocusable(true);
            // 设置点击popupwindow外屏幕其它地方消失
            fenzuPopupWindow.setOutsideTouchable(true);
            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);
        }
        //设置按钮点击监听
        RadioGroup rgFenzu = fenzuPopupView.findViewById(R.id.rg_fenzu);
        rgFenzu.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_kefenzu:
                    canFenzu = true;
                    tvFenzuSelected.setText("可分租");
                    break;
                case R.id.rb_bukefenzu:
                    canFenzu = false;
                    tvFenzuSelected.setText("不可分租");
                    break;
            }
        });
        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        fenzuPopupWindow.showAtLocation(findViewById(R.id.ll_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        fenzuPopupView.startAnimation(animation);
        //显示遮罩
        v_mask.setVisibility(View.VISIBLE);
    }

    public void showZujinPop() {
        if (zujinPopupWindow == null) {
            zujinPopupView = View.inflate(this, R.layout.popupwindow_information_rent, null);
            // 参数2,3：指明popupwindow的宽度和高度
            zujinPopupWindow = new PopupWindow(zujinPopupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            zujinPopupWindow.setOnDismissListener(() -> {
                //隐藏遮罩
                v_mask.setVisibility(View.GONE);
            });
            // 设置背景图片， 必须设置，不然动画没作用
            zujinPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            zujinPopupWindow.setFocusable(true);
            // 设置点击popupwindow外屏幕其它地方消失
            zujinPopupWindow.setOutsideTouchable(true);
            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);
        }
        //设置按钮点击监听
        RadioGroup rgUnit1 = zujinPopupView.findViewById(R.id.rg_unit_1);
        RadioGroup rgUnit2 = zujinPopupView.findViewById(R.id.rg_unit_2);
        EditText etRent = zujinPopupView.findViewById(R.id.et_rent);
        rgUnit1.setOnCheckedChangeListener((group, checkedId) -> {
            if (rgUnit1.getCheckedRadioButtonId() != -1 && ((RadioButton) group.findViewById(checkedId)).isChecked()) {
                rgUnit2.clearCheck();
                if (!etRent.getText().toString().equals("")) {
                    tvZujinSelected.setText(etRent.getText().toString() + ((RadioButton) group.findViewById(checkedId)).getText());
                    zujin_unit = ((RadioButton) group.findViewById(checkedId)).getText().toString();
                    zujin = etRent.getText().toString();
                }
            }
        });
        rgUnit2.setOnCheckedChangeListener((group, checkedId) -> {
            if (rgUnit2.getCheckedRadioButtonId() != -1 && ((RadioButton) group.findViewById(checkedId)).isChecked()) {
                rgUnit1.clearCheck();
                if (!etRent.getText().toString().equals("")) {
                    tvZujinSelected.setText(etRent.getText().toString() + ((RadioButton) group.findViewById(checkedId)).getText());
                    zujin_unit = ((RadioButton) group.findViewById(checkedId)).getText().toString();
                    zujin = etRent.getText().toString();
                }
            }
        });
        etRent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etRent.getText().toString().equals("")) {
                    if (rgUnit1.getCheckedRadioButtonId() != -1) {
                        tvZujinSelected.setText(etRent.getText().toString() + ((RadioButton) rgUnit1.findViewById(rgUnit1.getCheckedRadioButtonId())).getText());
                        zujin = etRent.getText().toString();
                    }
                    if (rgUnit2.getCheckedRadioButtonId() != -1) {
                        tvZujinSelected.setText(etRent.getText().toString() + ((RadioButton) rgUnit2.findViewById(rgUnit2.getCheckedRadioButtonId())).getText());
                        zujin = etRent.getText().toString();
                    }
                } else {
                    tvZujinSelected.setText("请选择");
                }
            }
        });
        //fenzuPopupView.findViewById(R.id.open_camera).setOnClickListener(this);
        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        zujinPopupWindow.showAtLocation(findViewById(R.id.ll_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        zujinPopupView.startAnimation(animation);
        //显示遮罩
        v_mask.setVisibility(View.VISIBLE);
    }

    private void showMianjiPop() {
        if (mianjiPopupWindow == null) {
            mianjiPopupView = View.inflate(this, R.layout.popupwindow_information_area, null);
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
            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);
        }
        //设置按钮点击监听
        rgMianji = mianjiPopupView.findViewById(R.id.rg_mianji);
        EditText etArea = mianjiPopupView.findViewById(R.id.et_area_number);
        rgMianji.setOnCheckedChangeListener((group, checkedId) -> {
            if (!etArea.getText().toString().equals("")) {
                tvMianjiSelected.setText(etArea.getText().toString() + ((RadioButton) rgMianji.findViewById(checkedId)).getText());
                mianji_unit = ((RadioButton) rgMianji.findViewById(checkedId)).getText().toString();
                mianji = etArea.getText().toString();
            }
        });
        etArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    if (rgMianji.getCheckedRadioButtonId() != -1) {
                        tvMianjiSelected.setText(s.toString() + ((RadioButton) rgMianji.findViewById(rgMianji.getCheckedRadioButtonId())).getText());
                        mianji = s.toString();
                    }
                } else {
                    etArea.setText("请选择");
                }
            }
        });
        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        mianjiPopupWindow.showAtLocation(findViewById(R.id.ll_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mianjiPopupView.startAnimation(animation);
        //显示遮罩
        v_mask.setVisibility(View.VISIBLE);
    }

    public void showZuqiPop() {
        if (zuqiPopupWindow == null) {
            zuqiPopupView = View.inflate(this, R.layout.popupwindow_information_leaseterm, null);
            // 参数2,3：指明popupwindow的宽度和高度
            zuqiPopupWindow = new PopupWindow(zuqiPopupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            zuqiPopupWindow.setOnDismissListener(() -> {
                //隐藏遮罩
                v_mask.setVisibility(View.GONE);
            });
            // 设置背景图片， 必须设置，不然动画没作用
            zuqiPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            zuqiPopupWindow.setFocusable(true);
            // 设置点击popupwindow外屏幕其它地方消失
            zuqiPopupWindow.setOutsideTouchable(true);
            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);
        }
        //设置按钮点击监听
        RadioGroup rgZuqi = zuqiPopupView.findViewById(R.id.rg_zuqi);
        rgZuqi.setOnCheckedChangeListener((group, checkedId) ->{
            tvZuqiSelected.setText(((RadioButton) group.findViewById(checkedId)).getText());
            zuqi_str = tvZuqiSelected.getText().toString();
        } );
        //fenzuPopupView.findViewById(R.id.open_photos).setOnClickListener(this);
        //fenzuPopupView.findViewById(R.id.open_camera).setOnClickListener(this);
        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        zuqiPopupWindow.showAtLocation(findViewById(R.id.ll_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        zuqiPopupView.startAnimation(animation);
        //显示遮罩
        v_mask.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        //获取纬度信息
        latitude = bdLocation.getLatitude();
        //获取经度信息
        longitude = bdLocation.getLongitude();
        int errorCode = bdLocation.getLocType();
        Log.i("latitude:", latitude + "");
        Log.i("longitude:", longitude + "");
        Log.i("errorCode:", errorCode + "");
        float radius = bdLocation.getRadius();    //获取定位精度，默认值为0.0f
        if (errorCode == 61 || errorCode == 161) {
            if (radius < 50) {
                locationClient.stop();
                et_latitude.setText(latitude + "," + longitude);
            }
        }
    }
}
