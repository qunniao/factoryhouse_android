package com.kufangdidi.www.bean;

import java.io.Serializable;
import java.util.List;

public class WalletBillBean implements Serializable {
    private int code;
    private String msg;
    private List<Data> data;
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

    public void setData(List<Data> data) {
        this.data = data;
    }
    public List<Data> getData() {
        return data;
    }
    public static class Data implements Serializable{

        private int woid;
        private int wid;
        private int uid;
        private String plid;
        private String orderNumber;
        private double orderMoney;
        private String orderName;
        private String description;
        private String outTradeNo;
        private int type;
        private int paymentType;
        private int status;
        private String createTime;
        private String successTime;
        public void setWoid(int woid) {
            this.woid = woid;
        }
        public int getWoid() {
            return woid;
        }

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

        public void setPlid(String plid) {
            this.plid = plid;
        }
        public String getPlid() {
            return plid;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }
        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderMoney(double orderMoney) {
            this.orderMoney = orderMoney;
        }
        public double getOrderMoney() {
            return orderMoney;
        }

        public void setOrderName(String orderName) {
            this.orderName = orderName;
        }
        public String getOrderName() {
            return orderName;
        }

        public void setDescription(String description) {
            this.description = description;
        }
        public String getDescription() {
            return description;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }
        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setType(int type) {
            this.type = type;
        }
        public int getType() {
            return type;
        }

        public void setPaymentType(int paymentType) {
            this.paymentType = paymentType;
        }
        public int getPaymentType() {
            return paymentType;
        }

        public void setStatus(int status) {
            this.status = status;
        }
        public int getStatus() {
            return status;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
        public String getCreateTime() {
            return createTime;
        }

        public void setSuccessTime(String successTime) {
            this.successTime = successTime;
        }
        public String getSuccessTime() {
            return successTime;
        }

    }
}
