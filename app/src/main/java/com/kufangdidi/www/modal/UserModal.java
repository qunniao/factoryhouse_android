package com.kufangdidi.www.modal;


/**
 * UserPO对象, description = "登录信息"
 */

public class UserModal{

    private String userPhone;//电话

    private String userPassword;//密码

    private String jiguang_username;
    private String jiguang_password;



    public String getJiguang_username() {
        return jiguang_username;
    }

    public void setJiguang_username(String jiguang_username) {
        this.jiguang_username = jiguang_username;
    }

    public String getJiguang_password() {
        return jiguang_password;
    }

    public void setJiguang_password(String jiguang_password) {
        this.jiguang_password = jiguang_password;
    }


    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }


    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }


}
