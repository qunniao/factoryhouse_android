package com.kufangdidi.www.app;

public interface Api {
    String BASE_URL = "http://back.zhanchengwlkj.com/factoryhouse";
    //    String BASE_URL = "http://192.168.1.32";
    //1:注册 2:登录 3:修改密码
    String GET_CODE_URL = BASE_URL + "/login/geCheckCode";
    String REGISTER_URL = BASE_URL + "/login/registrationUser";
    String LOGIN_URL = BASE_URL + "/login/loginByPassWord";
    String GET_CHINA = BASE_URL + "/data/queryAllCity";
    String FORGET_PASSWORD = BASE_URL + "/login/updatePassword";
    //添加求租求购信息
    String ADD_BUYING = BASE_URL + "/buyingPurchase/addBuyingPurchase";
    String ADD_CHUZU_OR_CHUSHOU = BASE_URL + "/fplHouse/addFplHouse";
    String SERCH_RENT_OR_SALE = BASE_URL + "/fplHouse/pageFplHouse";
    //String REGISTER_URL = "http://192.168.1.5/";
}
