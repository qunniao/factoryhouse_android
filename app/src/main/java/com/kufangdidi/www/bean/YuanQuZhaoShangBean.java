package com.kufangdidi.www.bean;

import java.io.Serializable;
import java.util.List;

public class YuanQuZhaoShangBean implements Serializable {
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

    public static class Data implements Serializable{

        private List<Content> content;
        private Pageable pageable;
        private boolean last;
        private int totalPages;
        private int totalElements;
        private Sort sort;
        private int numberOfElements;
        private boolean first;
        private int size;
        private int number;
        private boolean empty;

        public void setContent(List<Content> content) {
            this.content = content;
        }

        public List<Content> getContent() {
            return content;
        }

        public void setPageable(Pageable pageable) {
            this.pageable = pageable;
        }

        public Pageable getPageable() {
            return pageable;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

        public boolean getLast() {
            return last;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public int getTotalElements() {
            return totalElements;
        }

        public void setSort(Sort sort) {
            this.sort = sort;
        }

        public Sort getSort() {
            return sort;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }

        public boolean getFirst() {
            return first;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }

        public boolean getEmpty() {
            return empty;
        }

        public static class Content implements Serializable{

            private int pid;
            private int allBuildingsNum;
            private String title;
            private String ahtStructure;
            private String fireRating;
            private String productionCertificate;
            private String parkAddress;
            private String policy;
            private String matching;
            private String introduce;
            private int parkprice;
            private int priceArea;
            private String createTime;
            private String deliveryTime;
            private int latitude;
            private int longitude;
            private String suitableBusiness;
            private String contact;
            private String contactPhone;
            private String titlePicture;
            private List<PictureStorage> pictureStorage;

            public void setPid(int pid) {
                this.pid = pid;
            }

            public int getPid() {
                return pid;
            }

            public void setAllBuildingsNum(int allBuildingsNum) {
                this.allBuildingsNum = allBuildingsNum;
            }

            public int getAllBuildingsNum() {
                return allBuildingsNum;
            }

            public void setAhtStructure(String ahtStructure) {
                this.ahtStructure = ahtStructure;
            }

            public String getAhtStructure() {
                return ahtStructure;
            }

            public void setFireRating(String fireRating) {
                this.fireRating = fireRating;
            }

            public String getFireRating() {
                return fireRating;
            }

            public void setProductionCertificate(String productionCertificate) {
                this.productionCertificate = productionCertificate;
            }

            public String getProductionCertificate() {
                return productionCertificate;
            }

            public void setParkAddress(String parkAddress) {
                this.parkAddress = parkAddress;
            }

            public String getParkAddress() {
                return parkAddress;
            }

            public void setPolicy(String policy) {
                this.policy = policy;
            }

            public String getPolicy() {
                return policy;
            }

            public void setMatching(String matching) {
                this.matching = matching;
            }

            public String getMatching() {
                return matching;
            }

            public void setIntroduce(String introduce) {
                this.introduce = introduce;
            }

            public String getIntroduce() {
                return introduce;
            }

            public void setParkprice(int parkprice) {
                this.parkprice = parkprice;
            }

            public int getParkprice() {
                return parkprice;
            }

            public void setPriceArea(int priceArea) {
                this.priceArea = priceArea;
            }

            public int getPriceArea() {
                return priceArea;
            }


            public void setLatitude(int latitude) {
                this.latitude = latitude;
            }

            public int getLatitude() {
                return latitude;
            }

            public void setLongitude(int longitude) {
                this.longitude = longitude;
            }

            public int getLongitude() {
                return longitude;
            }

            public void setSuitableBusiness(String suitableBusiness) {
                this.suitableBusiness = suitableBusiness;
            }

            public String getSuitableBusiness() {
                return suitableBusiness;
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

            public void setPictureStorage(List<PictureStorage> pictureStorage) {
                this.pictureStorage = pictureStorage;
            }

            public List<PictureStorage> getPictureStorage() {
                return pictureStorage;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getDeliveryTime() {
                return deliveryTime;
            }

            public void setDeliveryTime(String deliveryTime) {
                this.deliveryTime = deliveryTime;
            }

            public String getTitlePicture() {
                return titlePicture;
            }

            public void setTitlePicture(String titlePicture) {
                this.titlePicture = titlePicture;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public static class PictureStorage implements Serializable{

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

        public static class Pageable {

            private Sort sort;
            private int pageNumber;
            private int pageSize;
            private int offset;
            private boolean paged;
            private boolean unpaged;

            public void setSort(Sort sort) {
                this.sort = sort;
            }

            public Sort getSort() {
                return sort;
            }

            public void setPageNumber(int pageNumber) {
                this.pageNumber = pageNumber;
            }

            public int getPageNumber() {
                return pageNumber;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setOffset(int offset) {
                this.offset = offset;
            }

            public int getOffset() {
                return offset;
            }

            public void setPaged(boolean paged) {
                this.paged = paged;
            }

            public boolean getPaged() {
                return paged;
            }

            public void setUnpaged(boolean unpaged) {
                this.unpaged = unpaged;
            }

            public boolean getUnpaged() {
                return unpaged;
            }

        }

        public static class Sort {

            private boolean unsorted;
            private boolean sorted;
            private boolean empty;

            public void setUnsorted(boolean unsorted) {
                this.unsorted = unsorted;
            }

            public boolean getUnsorted() {
                return unsorted;
            }

            public void setSorted(boolean sorted) {
                this.sorted = sorted;
            }

            public boolean getSorted() {
                return sorted;
            }

            public void setEmpty(boolean empty) {
                this.empty = empty;
            }

            public boolean getEmpty() {
                return empty;
            }

        }
    }
}
