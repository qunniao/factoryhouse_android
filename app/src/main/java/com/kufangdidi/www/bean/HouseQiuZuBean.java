package com.kufangdidi.www.bean;

import java.util.List;

public class HouseQiuZuBean {
    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }


    public static class DataBean {

        private int bpid;
        private int uid;
        private String productId;
        private String region;
        private int type;
        private int status;
        private int areaCap;
        private int areaLower;
        private String functionalUse;
        private int singleLayerArea;
        private String supportingDemand;
        private String title;
        private int layerHeight;
        private int loadBearing;
        private int minimumCharge;
        private String detailedDescription;
        private String contact;
        private String contactPhone;
        private String createTime;
        private String isEnd;

        public void setBpid(int bpid) {
            this.bpid = bpid;
        }

        public int getBpid() {
            return bpid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getUid() {
            return uid;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductId() {
            return productId;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getRegion() {
            return region;
        }



        public void setStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public void setAreaCap(int areaCap) {
            this.areaCap = areaCap;
        }

        public int getAreaCap() {
            return areaCap;
        }

        public void setAreaLower(int areaLower) {
            this.areaLower = areaLower;
        }

        public int getAreaLower() {
            return areaLower;
        }

        public void setFunctionalUse(String functionalUse) {
            this.functionalUse = functionalUse;
        }

        public String getFunctionalUse() {
            return functionalUse;
        }

        public void setSingleLayerArea(int singleLayerArea) {
            this.singleLayerArea = singleLayerArea;
        }

        public int getSingleLayerArea() {
            return singleLayerArea;
        }

        public void setSupportingDemand(String supportingDemand) {
            this.supportingDemand = supportingDemand;
        }

        public String getSupportingDemand() {
            return supportingDemand;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setLayerHeight(int layerHeight) {
            this.layerHeight = layerHeight;
        }

        public int getLayerHeight() {
            return layerHeight;
        }

        public void setLoadBearing(int loadBearing) {
            this.loadBearing = loadBearing;
        }

        public int getLoadBearing() {
            return loadBearing;
        }

        public void setMinimumCharge(int minimumCharge) {
            this.minimumCharge = minimumCharge;
        }

        public int getMinimumCharge() {
            return minimumCharge;
        }

        public void setDetailedDescription(String detailedDescription) {
            this.detailedDescription = detailedDescription;
        }

        public String getDetailedDescription() {
            return detailedDescription;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getContact() {
            return contact;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public String getContactPhone() {
            return contactPhone;
        }


        public void setIsEnd(String isEnd) {
            this.isEnd = isEnd;
        }

        public String getIsEnd() {
            return isEnd;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

}
