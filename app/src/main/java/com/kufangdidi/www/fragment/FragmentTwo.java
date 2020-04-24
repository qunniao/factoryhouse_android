package com.kufangdidi.www.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.squareup.picasso.Picasso;
import com.kufangdidi.www.R;
import com.kufangdidi.www.activity.product.ProductHomeActivity;
import com.kufangdidi.www.app.BaseApplication;
import com.kufangdidi.www.app.Constants;
import com.kufangdidi.www.bean.HouseBean;
import com.kufangdidi.www.bean.XuanZhiBean;
import com.kufangdidi.www.utils.Constant;
import com.kufangdidi.www.utils.LogUtils;
import com.kufangdidi.www.utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Response;

import static cn.jpush.im.android.api.jmrtc.JMRTCInternalUse.getApplicationContext;


/**
 * Created by admin on 2017/5/16.
 */

public class FragmentTwo extends Fragment implements View.OnClickListener, SensorEventListener,
        OnGetGeoCoderResultListener {
    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

    private float mCurrentAccracy = 3.0f;
    LocationClient mLocClient;
    private int mCurrentDirection = 0;
    private MyLocationData locData;
    public MyLocationListenner myListener = new MyLocationListenner();

    //手机的位置
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private int showPermissionDialog = 0;

    private List<XuanZhiBean.Data> list;
    private List<HouseBean.DataBean> productList;

    //产品
    private RelativeLayout layout_product;
    private ImageView iv_image;
    private TextView tv_title,tv_address,tv_type,tv_price,tv_price_unit,tv_area;
    private Map<String,View> map_bg = new HashMap<>();
    private Map<String,View> map_sanjiao = new HashMap<>();
    private View click_view_bg,click_view_sanjiao;

    private TextView tv_city;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab2, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d("FragmentTwo 启动");
        initView();

        mMapView = (MapView) getView().findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(11f);
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setMyLocationEnabled(true);
        //setMapCustomFile(mContext, PATH);
        // MapView.setMapCustomEnable(true);

        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        // option.setCoorType("bd09ll"); // 设置坐标类型
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000*60*10);//每10秒更新一次位置

        mLocClient.setLocOption(option);
        mLocClient.start();

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);




        //findListByArea();
        initListener();
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                Bundle bundle = marker.getExtraInfo();

                switch (bundle.getInt("type")){
                    case 0:
                        //点击圆
                        LogUtils.d("Latitude"+bundle.getDouble("Latitude")+"    Longitude"+bundle.getDouble("Longitude"));
                      //  findListByLatitude(bundle.getDouble("Latitude"), bundle.getDouble("Longitude"));
                        LatLng southwest;
                        LatLng northeast;
                        southwest = new LatLng(bundle.getDouble("Latitude"), bundle.getDouble("Longitude"));
                        northeast = new LatLng(bundle.getDouble("Latitude"), bundle.getDouble("Longitude"));

                        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
                                .include(southwest).build();
                        MapStatusUpdate u = MapStatusUpdateFactory
                                .newLatLng(bounds.getCenter());
                        mBaiduMap.setMapStatus(u);


                            MapStatusUpdate zoomTo = MapStatusUpdateFactory.zoomTo(14);
                            mBaiduMap.setMapStatus(zoomTo);
                        findDataByArea(bundle.getString("area"));
                        break;
                    case 1:
                        //点击方
                        HouseBean.DataBean product = (HouseBean.DataBean) bundle.getSerializable("product");
                        showProductView(product);
                        break;
                }


                return true;
            }
        });

    }

    private void initView() {
        View view = getView();
        tv_city= view.findViewById(R.id.tv_city);
        layout_product = view.findViewById(R.id.layout_product);
        iv_image = view.findViewById(R.id.iv_image);
        tv_title = view.findViewById(R.id.tv_title);
        tv_address = view.findViewById(R.id.tv_address);
        tv_type = view.findViewById(R.id.tv_type);
        tv_price = view.findViewById(R.id.tv_price);
        tv_price_unit = view.findViewById(R.id.tv_price_unit);
        tv_area = view.findViewById(R.id.tv_area);
    }

    private void showProductView(HouseBean.DataBean modal) {
        layout_product.setVisibility(View.VISIBLE);
        //获取当前点击的view，改变背景色
        /*
        if(click_view_bg!=null){
            //将上一次点击的view恢复成红色
            click_view_bg.setBackgroundResource(R.drawable.btn_ge_bg);
           // click_view_sanjiao.setBackgroundResource(R.mipmap.ic_daosanjiao);
            LogUtils.d("click_view_bg!=null");
        }
*/
       // click_view_bg = map_bg.get(modal.getProductId());
      //  click_view_bg.setBackgroundResource(R.drawable.btn_ge_bg_select);
       // click_view_sanjiao = map_sanjiao.get(modal.getProductId());
        //click_view_sanjiao.setBackgroundResource(R.mipmap.ic_daosanjiao_select);
        LogUtils.d("22222");

        Picasso.with(getContext())
                .load(Constant.SERVER_PIC_HOSTds+modal.getTitlePicture())
                .into(iv_image);

        tv_title.setText(modal.getTitle());
        tv_address.setText(modal.getAddress());
        if(modal.getStatus()==1){
            tv_type.setText("出租");
        }else{
            tv_type.setText("出售");
        }
        tv_price.setText(modal.getRent());
        tv_price_unit.setText(modal.getRentUnit());
        tv_area.setText(modal.getArea()+"");
        layout_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProductHomeActivity.class);
                i.putExtra("product_id",modal.getProductId());
                getContext().startActivity(i);
            }
        });
    }


    private void findCountByCity() {
        OkGo.post(Constant.SERVER_HOST + "/fplHouse" +
                "/queryFplHouseRegionalInformation\n").tag(this)
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                .params("city", Constant.nowCity)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("findCountByCity 返回结果s" + s);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        if (jsonObject.getInteger("code") != 0) return;
                        XuanZhiBean xuanZhiBean = JSONObject.parseObject(s, XuanZhiBean.class);
                        list = xuanZhiBean.getData();
                        mBaiduMap.clear();
                        fuGaiYuan();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        LogUtils.d("出错了");
//                        Toast.makeText(FragmentTwo.this, "请检查网络或遇到未知错误！", Toast.LENGTH_LONG)
//                        .show();
                    }
                });
    }

    private void findDataByArea(String area) {
        OkGo.post(Constant.SERVER_HOST + "/fplHouse" +
                "/queryFplHouseRegionalInformation\n").tag(this)
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                .params("city", area)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("findDataByArea 返回结果s" + s);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        if (jsonObject.getInteger("code") != 0) return;
                        HouseBean houseBean = JSONObject.parseObject(s, HouseBean.class);
                        productList = houseBean.getData();

                        LogUtils.d("findDataByArea productList size:" + productList.size());
                        if (productList == null || productList.size() == 0) return;
                       mBaiduMap.clear();
                        fuGaiFang();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        LogUtils.d("出错了");
//                        Toast.makeText(FragmentTwo.this, "请检查网络或遇到未知错误！", Toast.LENGTH_LONG)
//                        .show();
                    }
                });
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            LogUtils.d("百度地图onReceiveLocation");
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                // LogUtils.d("location == null || mMapView == null");
                return;
            }
             mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            LogUtils.d("nowCity:" + Constant.nowCity +"province:"+location.getProvince()+ " city:" +location.getCity()+" area:" +location.getCountry());
            LogUtils.d("s:"+location.getAddrStr());
            LogUtils.d("mCurrentLat:" + mCurrentLat + " mCurrentLon:" + mCurrentLon);
            LogUtils.d("mCurrentLat2:" + location.getLatitude() + " mCurrentLon2:" + location.getLongitude());
            //测试时写死，定位到杭州
           // mCurrentLat = 30.34855;
           // mCurrentLon = 120.106262;

            // LogUtils.d("nowCity:"+ Constant.nowCity+" city:"+location.getCity()+"
            // address:"+location.getLatitude());


            if (mCurrentLat == 4.9E-324 && mCurrentLon == 4.9E-324) {
                if (getInternetStatus()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showPermissionError();
                        }
                    });

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "无法连接网络", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                //测试
                // mBaiduMap.setMyLocationData(locData);
                // Constant.centerLat = 30.255098;
                // Constant.centerLon = 120.043848;
                // initMap(null);
                return;
            }
            Constant.nowLat = mCurrentLat;
            Constant.nowLon = mCurrentLon;

            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);

            //if (Constant.centerLat != 0) return;
            //第一次定位时
            Constant.centerLat = mCurrentLat;
            Constant.centerLon = mCurrentLon;
            initMap(null);
            Constant.nowCity = location.getCity();
            LogUtils.d("当前城市：" + Constant.nowCity);
            // 反Geo搜索当前定位地址
            LatLng pt = new LatLng(mCurrentLat, mCurrentLon);
            mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(pt));

        }


    }

    /**
     * 初始化地图位置和数据
     */
    private void initMap(String key) {
        LatLng southwest;
        LatLng northeast;
        southwest = new LatLng(Constant.centerLat, Constant.centerLon);
        northeast = new LatLng(Constant.centerLat, Constant.centerLon);

        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
                .include(southwest).build();
        MapStatusUpdate u = MapStatusUpdateFactory
                .newLatLng(bounds.getCenter());
        mBaiduMap.setMapStatus(u);
        // updateCity();
    }

    private void showPermissionError() {
        if (showPermissionDialog != 1 && showPermissionDialog != 2) {
            showPermissionDialog = 1;
            LogUtils.d("没有获取到权限");
            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                LogUtils.d("没有权限,请手动开启位置权限");
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                //builder.create().show();
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION}, 520);
            } else {
                /*
                CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
                builder.setTitle("无法定位");
                builder.setMessage("需要开启权限后才能使用定位");
                builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showPermissionDialog=2;
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if(mCurrentLat==4.9E-324 && mCurrentLon==4.9E-324)
                                showPermissionDialog=3;
                                else showPermissionDialog=1;
                            }
                        },6000*10);
                        Intent intent=getAppDetailSettingIntent(mContext);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                showPermissionDialog=1;
                                dialog.dismiss();
                            }
                        });

                builder.create().show();*/
            }

        }

    }

    public boolean getInternetStatus() {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = null;
        NetworkInfo[] networkInfo = new NetworkInfo[0];

        try {
            connectivityManager =
                    (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            networkInfo = connectivityManager.getAllNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < networkInfo.length; i++) {
            if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
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
        initMap(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        List<PoiInfo> poi = result.getPoiList();
        if(poi==null)return;
        LogUtils.d("poi:"+poi.size());

        String addr = result.getAddress();
        LogUtils.d("addr:"+addr);
        String[] str = addr.split("市");
        if(str.length>1){
            LogUtils.d("str[0]:"+str[0]);
            String[] sheng = str[0].split("省");
            if(sheng.length>1){
                Constant.nowCity = sheng[1]+"市";
                Constant.nowProvince = sheng[0];

            }else{
                Constant.nowCity = str[0]+"市";
                Constant.nowProvince = str[0];
            }
            //计算区
            String[] qu = str[1].split("区");
            if(qu.length>=1){
                Constant.nowCounty = qu[0]+"区";
            }
        }
        LogUtils.d("当前省:"+Constant.nowProvince);
        LogUtils.d("当前市:"+Constant.nowCity);
        LogUtils.d("当前区:"+Constant.nowCounty);


        if(poi.size()>0){
            Constant.nowLocationAddress = poi.get(0).name;
        }
        EventBus.getDefault().post(new MessageEvent(Constants.FRAGMENTONE_UPDATE_CITY));
        //根据城市获取数据
        findCountByCity();
    }



    /**
     * 地图状态改变，滑动前后
     */
    private void initListener() {
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            double zoomLevel1, zoomLevel2;

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                zoomLevel1 = mBaiduMap.getMapStatus().zoom;
                layout_product.setVisibility(View.GONE);
            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus status) {
                zoomLevel2 = mBaiduMap.getMapStatus().zoom;
                LogUtils.d("zoomLevel1:" + zoomLevel1 +
                        "zoomLevel2:" + zoomLevel2);

                if (zoomLevel2 >= 13 && zoomLevel1 < 13) {
                    //放大
                    LatLng _latLng = status.target;
                    LogUtils.d("1231231231223    "+_latLng.latitude + "," + _latLng.longitude);
                    mBaiduMap.clear();
                    findListByLatitude(_latLng.latitude, _latLng.longitude);
                }
                if (zoomLevel2 < 13 && zoomLevel1 >= 13) {
                    LogUtils.d("22222    zoomLevel2 < 13 && zoomLevel1 >= 13");
                    findCountByCity();
                }
            }

            @Override
            public void onMapStatusChange(MapStatus status) {
                // updateMapState(status);
            }
        });
    }

    //按坐标查产品列表
    private void findListByLatitude(double latitude, double longitude) {
        LogUtils.d("findListByLatitude    "+latitude + "," + longitude);
        if(mBaiduMap.getMapStatus().zoom<13){
            MapStatusUpdate zoomTo = MapStatusUpdateFactory.zoomTo(15);
            mBaiduMap.setMapStatus(zoomTo);}
        OkGo.post(Constant.SERVER_HOST + "/fplHouse/queryFplHousePOByLatitudeAndLongitude").tag(this)
                .params("token", BaseApplication.getSpUtils().getString("token"))
                .params("uid", BaseApplication.getSpUtils().getInt("uid"))
                .params("latitude", latitude)
                .params("longitude", longitude)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("findListByLatitude 返回结果s" + s);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        if (jsonObject.getInteger("code") != 0) return;
                        HouseBean houseBean = JSONObject.parseObject(s, HouseBean.class);
                        productList = houseBean.getData();


                        LogUtils.d("findListByLatitude productList size:" + productList.size());
                        if (productList == null || productList.size() == 0) return;

                        fuGaiFang();



                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        LogUtils.d("出错了");
//                        Toast.makeText(FragmentTwo.this, "请检查网络或遇到未知错误！", Toast.LENGTH_LONG)
//                        .show();
                    }
                });
    }


    public void fuGaiYuan() {
        Bundle bundle;
        LogUtils.d("title:" + list.size());
        for (int i = 0; i < list.size(); i++) {
            LogUtils.d("longitude:" + list.get(i).getLongitude()+" - latitude:"+list.get(i).getLatitude());
            //自定义布局
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab1_overlay_item2, null);
            TextView tv_number = view.findViewById(R.id.tv_number);
            TextView tv_name = view.findViewById(R.id.tv_name);


            Random r = new Random();
            tv_number.setText(list.get(i).getCount()+"个");
            tv_name.setText(list.get(i).getTitle());
            //定义Maker坐标点
//            LatLng point = new LatLng(list.get(i).getLatitude() + r.nextDouble() / 3,
//                    list.get(i).getLongitude() + r.nextDouble() / 3);   //横纵坐标值

            LatLng point = new LatLng(list.get(i).getLatitude(),
                           list.get(i).getLongitude());   //横纵坐标值
            //构建Marker图标
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
            //构建MarkerOption，用于在地图上添加Marker
            MarkerOptions optionPosition = new MarkerOptions()
                    .position(point)
                    .icon(bitmapDescriptor);
            //在地图上添加Marker，并显示
            Marker mMarkerA = (Marker) (mBaiduMap.addOverlay(optionPosition));
            LogUtils.d("i" + i);
            bundle = new Bundle();
            bundle.putInt("type",0);
            bundle.putDouble("Latitude", list.get(i).getLatitude());
            bundle.putDouble("Longitude", list.get(i).getLongitude());
            bundle.putString("area",list.get(i).getTitle());
            mMarkerA.setExtraInfo(bundle);


        }
    }


    public void fuGaiFang() {
        LogUtils.d("进入fuGaiFang");
        Bundle bundle;
        //自定义布局

        for (int i = 0; i < productList.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.tab1_overlay_item, null);
            TextView tv_name = view.findViewById(R.id.tv_name);
            RelativeLayout bg = view.findViewById(R.id.ll_store_maker_item);
            ImageView img_logo = view.findViewById(R.id.img_logo);
          //  map_bg.put(productList.get(i).getProductId(),tv_name);
           // map_sanjiao.put(productList.get(i).getProductId(),img_logo);

            Random r = new Random();
            LogUtils.d("fugaifang i:" + i);
            tv_name.setText(productList.get(i).getTitle());
            double lat = r.nextDouble() / 1000;
            double lon = r.nextDouble() / 1000;
            LogUtils.d("随机数 lat:"+lat+" lon:"+lon);
            //定义Maker坐标点
            LatLng point = new LatLng(Double.parseDouble(productList.get(i).getLatitude())+lat, Double.parseDouble(productList.get(i).getLongitude())+lon);   //横纵坐标值
            LatLng myPosition = point;
            //构建Marker图标
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
            //构建MarkerOption，用于在地图上添加Marker
            MarkerOptions optionPosition = new MarkerOptions()
                    .position(point)
                    .icon(bitmapDescriptor);
            //在地图上添加Marker，并显示
            //在地图上添加Marker，并显示
            Marker mMarkerA = (Marker) (mBaiduMap.addOverlay(optionPosition));
            LogUtils.d("i" + i);
            bundle = new Bundle();
            bundle.putInt("type",1);
            bundle.putSerializable("product",productList.get(i));
            mMarkerA.setExtraInfo(bundle);
        }

    }

}
