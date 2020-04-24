package com.kufangdidi.www.bean;

public class WalletBean {
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

        private int wid;
        private int uid;
        private double balance;
        private int status;
        private String payPassWord;
        public void setWid(int wid) {
            this.wid = wid;
        }
        public int getWid() {
            return wid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
        public int getUid() {
            return uid;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }
        public double getBalance() {
            return balance;
        }

        public void setStatus(int status) {
            this.status = status;
        }
        public int getStatus() {
            return status;
        }

        public void setPayPassWord(String payPassWord) {
            this.payPassWord = payPassWord;
        }
        public String getPayPassWord() {
            return payPassWord;
        }

    }
}
