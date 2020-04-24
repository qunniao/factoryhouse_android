package com.kufangdidi.www.utils;

/**
 * Created by Administrator on 2017/8/22 0022.
 */

public class Constant {
    public static final int MENU_CHANGFANG = 1;
    public static final int MENU_CHANGKU = 2;
    public static final int MENU_TUDI = 3;
    public static final int MENU_YUANQUZHAOSHANG = 4;
    public static final int MENU_QIUZUQIUGOU = 5;
    public static final int MENU_JINGJIREN = 6;
    public static final int MENU_BANGWOXUANZHI=7;
    public static final int MENU_XINWEN=8;

    public static final int SEARCH_PRODUCT = 111;
    public static final String CACHE_DATA = "kufangdidi/data/";//数据缓存
    public static final String CACHE_DATA_APK = "kufangdidi/data/apk/";//apk包缓存
    public static final String CACHE_DATA_IMAGE = "kufangdidi/data/images/";//apk包缓存
    public static final String CONFIG_PATH = "kufangdidi/config/";//配置文件路径

    //public static String  SERVER_HOST="http://47.93.212.57:8080/factoryhouse";//服务器地址
   // public static String SERVER_PIC_HOSTds="http://39.106.78.98/";//图片地址
    public static String SERVER_PIC_HOSTds="";//图片地址
    public static String  SERVER_DOWNLOAD_HOST="http://back.zhanchengwlkj.com/DownloadProject/";//app下载服务器地址-app检查最新版本用的
   //public static String SERVER_FILE_HOST="http://upload.jjlxxjs.net:8099/weblingfile";//文件服务器地址
   //public static String SERVER_UPLOAD_HOST="http://upload.jjlxxjs.net:8099/weblingfile";//文件服务器地址

    public static String SERVER_HOST="http://back.zhanchengwlkj.com/factoryhouse";//公司服务器地址
    public static String SERVER_FILE_HOST="http://test.jijiling.net:8080/weblingfile";//文件服务器地址
    public static String SERVER_UPLOAD_HOST="http://test.jijiling.net:8080/weblingfile";//文件服务器地址
    public static String key="-1";

    public static String APP_KEY="15cc251f03b45e3c3a95898d";

    public static final String OPEN_FABU_TONGZHI = "OPEN_FABU_TONGZHI";//发布通知开关


    //定位的位置
    public static double centerLat = 0.0;
    public static double centerLon = 0.0;
    public static double nowLat = 0.0;
    public static double nowLon = 0.0;
    public static String nowLocationAddress;
    public static String nowProvince;
    public static String nowCity = "北京";
    public static String nowCounty;

    public static final int ad_home=1;
    public static final int ad_map_center=2;

    public static String IMEI=null;

    public static final int WECHAT_PAY_FLAG = 3;

    public static final String REQUEST_SCAN_MODE="ScanMode";
    /**
     * 条形码： REQUEST_SCAN_MODE_BARCODE_MODE
     */
    public static final int REQUEST_SCAN_MODE_BARCODE_MODE = 0X100;
    /**
     * 二维码：REQUEST_SCAN_MODE_ALL_MODE
     */
    public static final int REQUEST_SCAN_MODE_QRCODE_MODE = 0X200;
    /**
     * 条形码或者二维码：REQUEST_SCAN_MODE_ALL_MODE
     */
    public static final int REQUEST_SCAN_MODE_ALL_MODE = 0X300;

    /**
     * 支付宝秘钥
     */
    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCGKe+N2TuMCQ/EVeeAj25CuAUzkrN64Snkb9fjTP/WRUwrUWswTWEiIg9+VfKTvHoWbIdgVn6lyu5jS53Ls+lNTyhL0pKsxVPACX/PGr+yxzKdeYUEsZPfhLhuedVH3CXOUQzsal+OCC1W+YFeG/e3OLW6uBmrBX6gIt/JLI5O9cIc/xs0gWPhshT6mXtEuf4HbxWPnZU51Y7aScQs9Ss2QDm1mauwexov7FXS/b/9IwVi0ADc3yHqMqPQFUMqwajs8dBu4o1aJhl7zflXuynzGC1sGpQJ9YCl+tSYOL9K9FtM3qfkNiWb0dmq/xJ7RhfcaOw6MB+UUj/Kg3eL/GVpAgMBAAECggEAcY4hEmChg/9PdxB2p1u/KX1z+ZjbjM/zk0QO6HaNvCBfPfLl1LZJ1ogZsQayENVgSIaHdsT+4PkIGMcxwe5/M04ifzq1psJGBq6DnRm402rCroLiUXyz+mXtbKR2HyJ4ZU44sAnA4wiMx6bbFnp5inzXZDjakORPrt6Vp0KtGKqw650ROcMjRP2nszXOJ4jbaMZSLc9qnvAwVajENc6WyQTyMAYRRKxSfrlPRtVfpK1o82tWyaYBep1fXG/+uvSEn4BgebQY18owRqAb28yru4ExYXz4UmhEP0B7iVsbt2q86JeKkIkNAq30PIiyOrfyQ5a6KFI6iiK8V5w+ERuBGQKBgQDXNaUrnRRChiJWrzOeHEkMP1rJbYqXdamqoReRr6SOGPf1turD062OrFTtZy7vcrwdRoeAxJy3RqxlvUxr8YnVkOfDmie9QNfqa5dC1pdnI5HSG/Mr0kPtAAfMeZoJ0q0k6Z0t06t1KjpjsBPzW5UTDzQrkvt4K/F3QjBAuVrMxwKBgQCfl84tZwV4YSjz1cLXwIqNz5D0zjEd4GcnH+2LIkOt8WAaB6KmuLI6NZ/taM4Xqx5EQHuWseueoTQjy7u6eeqGRyWMCpqWcowu0dDRfng/TpVs8I4TXff4+tFbM5aJAWZvz92Zvgs7NkYZ/VUbzbny9jO9B7JRv+lZ8/S1HRMsTwKBgDV5+hINVUR2ij/aB0yJoMkvUQLuelrKfR+OKgokiBJbN/pRuqJwjhb+0ZewZSRQ8Veg9/jSYlLUd/BMCppkCakM+dpMCwXnAAAbPhn4suSJHSoFkfZYqFJTCJ0RtmosdKPQfSMvDtLtbFbrIPn+zttOktslCeHBoZ3Nz6m1pU9vAoGALsllU2ApdWIejGV1iqTSucJJP8uXEKgx9YFhtXFlIABsaRUl9PSeo3wHF6WrDbtH00CIOuSMK+QckgXvUjrdq2H+OvD84vBP8oXmTxRhkkWqftaDAGCDCwYVDESydBXp/wuUbrcNOd2ubX4cTltgkT7mahtwhpDZi1p+uf39GfcCgYEAuSTE2JvwjaRWZgQ8Xl9lwaVDfGPbghLRUyicgXdki6PcYskMCdtd2QN0TwP43zUxoAtFeYLXpIFmpuexn2x7c6mtKdjAYJNK3rpc8RECmj1Qz8zhcjabu43DzOPJaaKuXVwwD4hC9j6LuGqQwD1zMhe+WWP1Hfz32fnhGChEmDY=";
    public static final String RSA_PRIVATE = "";

    public static final int ALIPAY_PAY_FLAG = 1;
    public static final int ALIPAY_AUTH_FLAG = 2;

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2017112100068867";
    public static boolean firstOpen = true;

    public static final int HISTORY_NOTI=1;


    /**
     * 微信登录
     */
    public static final String APP_ID_WX = "wx792afd40dc634f59";
    /**
     * qq
     */
    public static final String APP_ID_QQ = "1106598190";
}
