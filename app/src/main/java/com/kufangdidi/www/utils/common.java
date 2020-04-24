package com.kufangdidi.www.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;



import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by root on 2017/8/31.
 */

public class common {
    private static final double PI = 3.14159265358979323; //圆周率
    private static final double R = 6371229;              //地球的半径
    public final static String PHONE_PATTERN = "^((14[0-9])|(13[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9]))\\d{8}$";

    /**
     * make true current connect service is wifi
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    //计算两个经纬度之间的距离
    public static String getDistance(double longt1, double lat1, double longt2, double lat2){
       // LogUtils.d("longt1:"+longt1+" lat1:"+lat1+"  longt2:"+longt2+" lat2:"+lat2);
        double x,y,distance;
        x=(longt2-longt1)*PI*R* Math.cos( ((lat1+lat2)/2) *PI/180)/180;
        y=(lat2-lat1)*PI*R/180;
        distance=(Math.hypot(x,y))/1000;
        return String.format("%.2f", distance);
    }

    //计算两个经纬度之间的距离
    public static double getDistanceDouble(double longt1, double lat1, double longt2, double lat2){
       // LogUtils.d("longt1:"+longt1+" lat1:"+lat1+"  longt2:"+longt2+" lat2:"+lat2);
        double x,y,distance;
        x=(longt2-longt1)*PI*R* Math.cos( ((lat1+lat2)/2) *PI/180)/180;
        y=(lat2-lat1)*PI*R/180;
        distance=(Math.hypot(x,y))/1000;
        return distance;
    }
    /**
     * scrollview嵌套listview显示不全解决
     *
     * @param listView
     */
    public static double setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        return params.height;
    }


    public static boolean isNumber(String str){
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        if(pattern.matcher(str).matches()){
            //数字
            return true;
        } else {
            //非数字
            return false;
        }
    }

    public static long getNowTime(){
        long currentTime= Calendar.getInstance().getTimeInMillis();
        return currentTime;
    }

    public static String splitName(String url) {
        String substring = null;
        try{
            substring = url.substring(url.lastIndexOf("/") + 1, url.length());
        } catch (Exception e) {
            Log.d("web","splitName error");
            e.printStackTrace();
        }
        return substring;
    }

    /**
     * 字符串转换成日期
     * @param str
     * @return date
     */
    public static long StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date date = null;
        try {
            date = format.parse(str);
            return date.getTime();
        } catch (ParseException e) {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                date = format.parse(str);
                return date.getTime();
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return 0;
    }



    /**
     * 时间字符串
     * @return
     */
    public static String getNowTimeString(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.getDefault());
        long currentTime= Calendar.getInstance().getTimeInMillis();
        return sdf.format(currentTime);
    }

    public static String getNowTimeDay(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
        if(time==0){
            time= Calendar.getInstance().getTimeInMillis();
        }
        return sdf.format(time);
    }

    /**
     * 时间转日期
     * @param time
     * @return
     */
    public static String timeToDateByLong(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        return sdf.format(time);
    }

    public static String timeToBirth(int time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format((long)time*1000);
    }

    public static long birthToTome(String str){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getTimeDay(long time){
        SimpleDateFormat yearSdf = new SimpleDateFormat("yyyy", Locale.getDefault());
        long currentTime= Calendar.getInstance().getTimeInMillis();
        String year1 = yearSdf.format(currentTime);
        String year2 = yearSdf.format(time);

        if(year1.equals(year2)){
            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault());
            return sdf.format(time);
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
            return sdf.format(time);
        }
    }

    public static Map<String,Integer> getNowDay(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if(time==0){
            time= Calendar.getInstance().getTimeInMillis();
        }

        String date = sdf.format(time);
        String[] da = date.split("-");
        Map<String,Integer> map = new HashMap<>();
        map.put("year",new Integer(da[0]));
        map.put("month",new Integer(da[1]));
        map.put("day",new Integer(da[2]));
        return map;
    }

    public static String formatPrice(double price){
        DecimalFormat df;
        double p = price-(int)price;
        LogUtils.d("formatPrice price:"+price+" p:"+p);
        if(p==0.00){
            df = new DecimalFormat("#");
            LogUtils.d("formatPrice ==0");
        }else{
            df = new DecimalFormat("0.00");
        }
        return df.format(price);
    }


    public ArrayList<String> initListZhuangXiu() {
        ArrayList<String> list = new ArrayList();
        list.add("装修公司");list.add("室内设计");list.add("施工监理");list.add("现场视频");
        list.add("安防安装");list.add("厨卫改造");list.add("水电工长");list.add("泥工工长");
        list.add("木工工长");list.add("油漆工长");list.add("展柜制作");list.add("甲醛检测");
        list.add("拆除清理");list.add("专业打孔");list.add("灯具安装");list.add("洁具安装");
        list.add("马桶安装");list.add("挂件安装");list.add("窗帘安装");list.add("地板安装");
        list.add("卫浴安装");list.add("吊顶安装");list.add("铝合金门窗");list.add("房门安装");
        list.add("防盗门安装");list.add("阳光房安装");list.add("家具安装");list.add("家电安装");
        list.add("浴霸安装");list.add("楼梯安装");list.add("空调安装");list.add("油烟机安装");
        list.add("电视安装");list.add("配饰安装");list.add("墙纸铺贴");list.add("瓷砖铺贴");
        list.add("瓷砖美缝");list.add("地毯安装");list.add("石材安装");list.add("地暖安装");
        list.add("办公桌拆装");list.add("地胶板安装");list.add("自流平安装");list.add("办公隔断");
        list.add("晾衣杆");list.add("五金配件");
        return list;
    }

    public ArrayList<String> initListJiaZheng() {
        ArrayList<String> list = new ArrayList();
        list.add("钟点工");list.add("月嫂");list.add("保姆");list.add("陪护");
        list.add("催乳师");list.add("育婴师");list.add("涉外家政");list.add("病人陪护");
        list.add("老人陪护");list.add("工程拓荒");list.add("地板打蜡");list.add("石材养护");
        list.add("墙面粉刷");list.add("外墙清洗");list.add("空调清洗");list.add("灯具清洗");
        list.add("油烟机清洗");list.add("地毯清洗");list.add("物业保洁");list.add("沙发翻新");
        list.add("房屋补漏");list.add("接送小孩");list.add("厨师上门");list.add("开锁修锁");

        list.add("水电维修");list.add("管道疏通");list.add("家电维修");list.add("白事服务");
        list.add("红事服务");list.add("专业打孔");list.add("修改衣裤");list.add("磨剪刀");
        list.add("磨菜刀");
        return list;
    }

    public ArrayList<String> initListBanJia() {
        ArrayList<String> list = new ArrayList();
        list.add("无车搬家");list.add("三轮车");list.add("小面包车");
        return list;
    }

    public ArrayList<String> initListShuiGuo() {
        ArrayList<String> list = new ArrayList();
        list.add("鲜花配送");list.add("水果配送");
        return list;
    }
    public ArrayList<String> initListWeiXiu() {
        ArrayList<String> list = new ArrayList();
        list.add("开锁修锁");list.add("空调维修");list.add("管道疏通");
        return list;
    }
    public ArrayList<String> initListErShou() {
        ArrayList<String> list = new ArrayList();
        list.add("废品回收");list.add("头发回收");list.add("鸡毛鸭毛");
        return list;
    }

    public ArrayList<String> initListZuFang() {
        ArrayList<String> list = new ArrayList();
        list.add("城中村");list.add("整租");list.add("合租");list.add("短租");
        return list;
    }
    public ArrayList<String> initListGongZuo() {
        ArrayList<String> list = new ArrayList();
        list.add("附近工作");
        return list;
    }

    public ArrayList<String> initListJiJiu() {
        ArrayList<String> list = new ArrayList();
        list.add("家庭急救");
        return list;
    }


    /*
    旋转动画
    bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.menu_page_sanjiao)).getBitmap();
        matrix.setRotate(180);

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);
     */

    //金额验证
    public static boolean isMoney(String str)
    {
        java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        java.util.regex.Matcher match=pattern.matcher(str);
        if(match.matches()==false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static String getVersionCode(Context context)
    {
        String versionName = "";
        try
        {
            // 获取软件版本号，
            versionName = context.getPackageManager().getPackageInfo("com.zhanchengwlkj.www", 0).versionName;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 检查app是否安装
     * @param context
     * @param app
     * @return
     */
    public static boolean checkApp(Context context, String app){
        PackageInfo packageInfo;
        boolean flag = false;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(
                    app, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo !=null){
            return true;
        }
        return false;
    }

    /**
     * 按照指定字节长度截取字符串，防止中文被截成一半的问题
     * @param s 源字符串
     * @param length 截取的字节数
     * @return 截取后的字符串
     * @throws
     */
    public static String cutString(String s, int length) throws UnsupportedEncodingException {

        byte[] bytes = s.getBytes("Unicode");
        int n = 0; // 表示当前的字节数
        int i = 2; // 要截取的字节数，从第3个字节开始
        for (; i < bytes.length && n < length; i++){
            // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
            if (i % 2 == 1){
                n++; // 在UCS2第二个字节时n加1
            }
            else{
                // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
                if (bytes[i] != 0){
                    n++;
                }
            }
        }
        // 如果i为奇数时，处理成偶数
        if (i % 2 == 1){
            // 该UCS2字符是汉字时，去掉这个截一半的汉字
            if (bytes[i - 1] != 0){
                i = i - 1;
            }
            // 该UCS2字符是字母或数字，则保留该字符
            else{
                i = i + 1;
            }
        }

        return new String(bytes, 0, i, "Unicode");

    }

}
