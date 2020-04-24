package com.kufangdidi.www.bean;

public class IsVipBean {
    private int code;
    private String msg;
    private Data data;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }
    public static class Data {

        private int mid;
        private int uid;
        private String createTime;
        private String expiryDate;
        public void setMid(int mid) {
            this.mid = mid;
        }
        public int getMid() {
            return mid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
        public int getUid() {
            return uid;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
        public String getCreateTime() {
            return createTime;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }
        public String getExpiryDate() {
            return expiryDate;
        }

    }
}
