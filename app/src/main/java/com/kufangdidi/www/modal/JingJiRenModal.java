package com.kufangdidi.www.modal;


import java.io.Serializable;


public class JingJiRenModal implements Serializable {

        private int uid;
        private String userName;
        private String userPhone;
        private int userSex;
        private String userPassword;
        private String createTime;
        private int status;
        private String jiguangUsername;
        private String jiguangPassword;
        private String avatarUrl;
        private int realNameReview;
        private int type;
        private String mainArea;
        private String introduction;
        private boolean isEnd;
        private int fplHouse;
        private int popularValue;
        public void setUid(int uid) {
            this.uid = uid;
        }
        public int getUid() {
            return uid;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
        public String getUserName() {
            return userName;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }
        public String getUserPhone() {
            return userPhone;
        }

        public void setUserSex(int userSex) {
            this.userSex = userSex;
        }
        public int getUserSex() {
            return userSex;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }
        public String getUserPassword() {
            return userPassword;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
        public String getCreateTime() {
            return createTime;
        }

        public void setStatus(int status) {
            this.status = status;
        }
        public int getStatus() {
            return status;
        }

        public void setJiguangUsername(String jiguangUsername) {
            this.jiguangUsername = jiguangUsername;
        }
        public String getJiguangUsername() {
            return jiguangUsername;
        }

        public void setJiguangPassword(String jiguangPassword) {
            this.jiguangPassword = jiguangPassword;
        }
        public String getJiguangPassword() {
            return jiguangPassword;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }
        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setRealNameReview(int realNameReview) {
            this.realNameReview = realNameReview;
        }
        public int getRealNameReview() {
            return realNameReview;
        }

        public void setType(int type) {
            this.type = type;
        }
        public int getType() {
            return type;
        }

        public void setMainArea(String mainArea) {
            this.mainArea = mainArea;
        }
        public String getMainArea() {
            return mainArea;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }
        public String getIntroduction() {
            return introduction;
        }

        public void setIsEnd(boolean isEnd) {
            this.isEnd = isEnd;
        }
        public boolean getIsEnd() {
            return isEnd;
        }

        public void setFplHouse(int fplHouse) {
            this.fplHouse = fplHouse;
        }
        public int getFplHouse() {
            return fplHouse;
        }

        public void setPopularValue(int popularValue) {
            this.popularValue = popularValue;
        }
        public int getPopularValue() {
            return popularValue;
        }
}
