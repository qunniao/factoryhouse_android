package com.kufangdidi.www.bean;

import java.io.Serializable;
import java.util.List;

public class ProductBean  implements Serializable{

    /**
     * code : 0
     * msg : 成功
     * data : {"content":[{"fid":1,"uid":3,"productId":"fdas","region":"fdsaw","types":1,"status":1,"subleaseType":1,"rent":"fsa","area":100,"leasePeriod":1,"title":"1","address":"1","latitude":"1","longitude":"1","infrastructure":"1","peripheralPackage":"1","suitableBusiness":"1","detailedDescription":"1","contact":"1","contactPhone":"1","createTime":"2019-04-18T03:05:57.000+0000","titlePicture":null,"pictureStoragePOS":[],"isEnd":true}],"pageable":{"sort":{"unsorted":false,"sorted":true,"empty":false},"pageSize":15,"pageNumber":0,"offset":0,"paged":true,"unpaged":false},"totalPages":1,"totalElements":1,"last":true,"sort":{"unsorted":false,"sorted":true,"empty":false},"numberOfElements":1,"first":true,"size":15,"number":0,"empty":false}
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

    public static class DataBean implements Serializable{
        /**
         * content : [{"fid":1,"uid":3,"productId":"fdas","region":"fdsaw","types":1,"status":1,"subleaseType":1,"rent":"fsa","area":100,"leasePeriod":1,"title":"1","address":"1","latitude":"1","longitude":"1","infrastructure":"1","peripheralPackage":"1","suitableBusiness":"1","detailedDescription":"1","contact":"1","contactPhone":"1","createTime":"2019-04-18T03:05:57.000+0000","titlePicture":null,"pictureStoragePOS":[],"isEnd":true}]
         * pageable : {"sort":{"unsorted":false,"sorted":true,"empty":false},"pageSize":15,"pageNumber":0,"offset":0,"paged":true,"unpaged":false}
         * totalPages : 1
         * totalElements : 1
         * last : true
         * sort : {"unsorted":false,"sorted":true,"empty":false}
         * numberOfElements : 1
         * first : true
         * size : 15
         * number : 0
         * empty : false
         */

        private PageableBean pageable;
        private int totalPages;
        private int totalElements;
        private boolean last;
        private SortBeanX sort;
        private int numberOfElements;
        private boolean first;
        private int size;
        private int number;
        private boolean empty;
        private List<ContentBean> content;

        public PageableBean getPageable() {
            return pageable;
        }

        public void setPageable(PageableBean pageable) {
            this.pageable = pageable;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public boolean isLast() {
            return last;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

        public SortBeanX getSort() {
            return sort;
        }

        public void setSort(SortBeanX sort) {
            this.sort = sort;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public boolean isFirst() {
            return first;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }

        public List<ContentBean> getContent() {
            return content;
        }

        public void setContent(List<ContentBean> content) {
            this.content = content;
        }

        public static class PageableBean  implements Serializable{
            /**
             * sort : {"unsorted":false,"sorted":true,"empty":false}
             * pageSize : 15
             * pageNumber : 0
             * offset : 0
             * paged : true
             * unpaged : false
             */

            private SortBean sort;
            private int pageSize;
            private int pageNumber;
            private int offset;
            private boolean paged;
            private boolean unpaged;

            public SortBean getSort() {
                return sort;
            }

            public void setSort(SortBean sort) {
                this.sort = sort;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getPageNumber() {
                return pageNumber;
            }

            public void setPageNumber(int pageNumber) {
                this.pageNumber = pageNumber;
            }

            public int getOffset() {
                return offset;
            }

            public void setOffset(int offset) {
                this.offset = offset;
            }

            public boolean isPaged() {
                return paged;
            }

            public void setPaged(boolean paged) {
                this.paged = paged;
            }

            public boolean isUnpaged() {
                return unpaged;
            }

            public void setUnpaged(boolean unpaged) {
                this.unpaged = unpaged;
            }

            public static class SortBean  implements Serializable{
                /**
                 * unsorted : false
                 * sorted : true
                 * empty : false
                 */

                private boolean unsorted;
                private boolean sorted;
                private boolean empty;

                public boolean isUnsorted() {
                    return unsorted;
                }

                public void setUnsorted(boolean unsorted) {
                    this.unsorted = unsorted;
                }

                public boolean isSorted() {
                    return sorted;
                }

                public void setSorted(boolean sorted) {
                    this.sorted = sorted;
                }

                public boolean isEmpty() {
                    return empty;
                }

                public void setEmpty(boolean empty) {
                    this.empty = empty;
                }
            }
        }

        public static class SortBeanX {
            /**
             * unsorted : false
             * sorted : true
             * empty : false
             */

            private boolean unsorted;
            private boolean sorted;
            private boolean empty;

            public boolean isUnsorted() {
                return unsorted;
            }

            public void setUnsorted(boolean unsorted) {
                this.unsorted = unsorted;
            }

            public boolean isSorted() {
                return sorted;
            }

            public void setSorted(boolean sorted) {
                this.sorted = sorted;
            }

            public boolean isEmpty() {
                return empty;
            }

            public void setEmpty(boolean empty) {
                this.empty = empty;
            }
        }

        public static class ContentBean  implements Serializable {
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
            private String areaUnit;
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
            private int isEnd;
            private List<?> pictureStoragePOS;

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

            public int isIsEnd() {
                return isEnd;
            }

            public void setIsEnd(int isEnd) {
                this.isEnd = isEnd;
            }

            public List<?> getPictureStoragePOS() {
                return pictureStoragePOS;
            }

            public void setPictureStoragePOS(List<?> pictureStoragePOS) {
                this.pictureStoragePOS = pictureStoragePOS;
            }

            public String getRentUnit() {
                return rentUnit;
            }

            public void setRentUnit(String rentUnit) {
                this.rentUnit = rentUnit;
            }

            public String getAreaUnit() {
                return areaUnit;
            }

            public void setAreaUnit(String areaUnit) {
                this.areaUnit = areaUnit;
            }
        }
    }
}
