package com.kufangdidi.www.modal;

public class LoginBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"uid":0,"userName":null,"userPhone":"18702528557","userSex":0,"userPassword":null,"balance":0,"createTime":null,"token":"9lnzgtft0zkCatmUlobRdQ=="}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 0
         * userName : null
         * userPhone : 18702528557
         * userSex : 0
         * userPassword : null
         * balance : 0.0
         * createTime : null
         * token : 9lnzgtft0zkCatmUlobRdQ==
         */

        private int uid;
        private String userName;
        private String userPhone;
        private int userSex;
        private String userPassword;
        private double balance;
        private String createTime;
        private String token;
        private String jiguangUsername;
        private String jiguangPassword;
        private String avatarUrl;
        private int type;
        private String mainArea;
        private String introduction;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public int getUserSex() {
            return userSex;
        }

        public void setUserSex(int userSex) {
            this.userSex = userSex;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }


        public String getJiguangUsername() {
            return jiguangUsername;
        }

        public void setJiguangUsername(String jiguangUsername) {
            this.jiguangUsername = jiguangUsername;
        }

        public String getJiguangPassword() {
            return jiguangPassword;
        }

        public void setJiguangPassword(String jiguangPassword) {
            this.jiguangPassword = jiguangPassword;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getMainArea() {
            return mainArea;
        }

        public void setMainArea(String mainArea) {
            this.mainArea = mainArea;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }
    }
}
