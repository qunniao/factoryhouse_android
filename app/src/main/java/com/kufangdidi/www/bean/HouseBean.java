package com.kufangdidi.www.bean;

import java.io.Serializable;
import java.util.List;

public class HouseBean {
    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }


    public static class DataBean  implements Serializable{
        /**
         * fid : 1
         * uid : 3
         * productId : fdas
         * region : fdsaw
         * types : 1
         * status : 1
         * subleaseType : 1
         * rent : fsa
         * area : 100
         * leasePeriod : 1
         * title : 1
         * address : 1
         * latitude : 1
         * longitude : 1
         * infrastructure : 1
         * peripheralPackage : 1
         * suitableBusiness : 1
         * detailedDescription : 1
         * contact : 1
         * contactPhone : 1
         * createTime : 2019-04-18T03:05:57.000+0000
         * titlePicture : null
         * pictureStoragePOS : []
         * isEnd : true
         */

        private int fid;
        private int uid;
        private String productId;
        private String region;
        private int types;
        private int status;
        private int subleaseType;
        private String rent;
        private String rentUnit;
        private int area;
        private String leasePeriod;
        private String title;
        private String address;
        private String latitude;
        private String longitude;
        private String infrastructure;
        private String peripheralPackage;
        private String suitableBusiness;
        private String detailedDescription;
        private String contact;
        private String contactPhone;
        private String createTime;
        private String titlePicture;
        private String isEnd;
        private List<?> pictureStorage;

        public int getFid() {
            return fid;
        }

        public void setFid(int fid) {
            this.fid = fid;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public int getTypes() {
            return types;
        }

        public void setTypes(int types) {
            this.types = types;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getSubleaseType() {
            return subleaseType;
        }

        public void setSubleaseType(int subleaseType) {
            this.subleaseType = subleaseType;
        }

        public String getRent() {
            return rent;
        }

        public void setRent(String rent) {
            this.rent = rent;
        }

        public int getArea() {
            return area;
        }

        public void setArea(int area) {
            this.area = area;
        }

        public String getLeasePeriod() {
            return leasePeriod;
        }

        public void setLeasePeriod(String leasePeriod) {
            this.leasePeriod = leasePeriod;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getInfrastructure() {
            return infrastructure;
        }

        public void setInfrastructure(String infrastructure) {
            this.infrastructure = infrastructure;
        }

        public String getPeripheralPackage() {
            return peripheralPackage;
        }

        public void setPeripheralPackage(String peripheralPackage) {
            this.peripheralPackage = peripheralPackage;
        }

        public String getSuitableBusiness() {
            return suitableBusiness;
        }

        public void setSuitableBusiness(String suitableBusiness) {
            this.suitableBusiness = suitableBusiness;
        }

        public String getDetailedDescription() {
            return detailedDescription;
        }

        public void setDetailedDescription(String detailedDescription) {
            this.detailedDescription = detailedDescription;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getTitlePicture() {
            return titlePicture;
        }

        public void setTitlePicture(String titlePicture) {
            this.titlePicture = titlePicture;
        }


        public List<?> getPictureStorage() {
            return pictureStorage;
        }

        public void setPictureStorage(List<?> pictureStorage) {
            this.pictureStorage = pictureStorage;
        }

        public String getRentUnit() {
            return rentUnit;
        }

        public void setRentUnit(String rentUnit) {
            this.rentUnit = rentUnit;
        }

        public String getIsEnd() {
            return isEnd;
        }

        public void setIsEnd(String isEnd) {
            this.isEnd = isEnd;
        }

        public static class PictureStorage implements Serializable {

            private int psid;
            private int primaryid;
            private String url;
            private int type;
            private String createTime;
            private String description;
            private int createUserId;

            public void setPsid(int psid) {
                this.psid = psid;
            }

            public int getPsid() {
                return psid;
            }

            public void setPrimaryid(int primaryid) {
                this.primaryid = primaryid;
            }

            public int getPrimaryid() {
                return primaryid;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getUrl() {
                return url;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getType() {
                return type;
            }


            public void setDescription(String description) {
                this.description = description;
            }

            public String getDescription() {
                return description;
            }

            public void setCreateUserId(int createUserId) {
                this.createUserId = createUserId;
            }

            public int getCreateUserId() {
                return createUserId;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
        }
    }
}
