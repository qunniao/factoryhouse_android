package com.kufangdidi.www.bean;

import java.io.Serializable;
import java.util.List;


public class BaiKeBean implements Serializable {
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

    public static class Data implements Serializable {

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

        public static class Content implements Serializable {

            private int elid;
            private String createName;
            private int createId;
            private String title;
            private String createTime;
            private int isEnd;
            private int type;
            private List<EncyclopediaAnswer> encyclopediaAnswer;

            public void setElid(int elid) {
                this.elid = elid;
            }

            public int getElid() {
                return elid;
            }

            public void setCreateName(String createName) {
                this.createName = createName;
            }

            public String getCreateName() {
                return createName;
            }

            public void setCreateId(int createId) {
                this.createId = createId;
            }

            public int getCreateId() {
                return createId;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle() {
                return title;
            }


            public void setIsEnd(int isEnd) {
                this.isEnd = isEnd;
            }

            public int getIsEnd() {
                return isEnd;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getType() {
                return type;
            }

            public void setEncyclopediaAnswer(List<EncyclopediaAnswer> encyclopediaAnswer) {
                this.encyclopediaAnswer = encyclopediaAnswer;
            }

            public List<EncyclopediaAnswer> getEncyclopediaAnswer() {
                return encyclopediaAnswer;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public static class EncyclopediaAnswer implements Serializable{

                private int eaid;
                private int elid;
                private String createName;
                private int createId;
                private String createTime;
                private String content;
                private int isEnd;

                public void setEaid(int eaid) {
                    this.eaid = eaid;
                }

                public int getEaid() {
                    return eaid;
                }

                public void setElid(int elid) {
                    this.elid = elid;
                }

                public int getElid() {
                    return elid;
                }

                public void setCreateName(String createName) {
                    this.createName = createName;
                }

                public String getCreateName() {
                    return createName;
                }

                public void setCreateId(int createId) {
                    this.createId = createId;
                }

                public int getCreateId() {
                    return createId;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getContent() {
                    return content;
                }

                public void setIsEnd(int isEnd) {
                    this.isEnd = isEnd;
                }

                public int getIsEnd() {
                    return isEnd;
                }

                public String getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(String createTime) {
                    this.createTime = createTime;
                }
            }
        }

        public static class Pageable implements Serializable {

            private Sort sort;
            private int offset;
            private int pageNumber;
            private int pageSize;
            private boolean paged;
            private boolean unpaged;

            public void setSort(Sort sort) {
                this.sort = sort;
            }

            public Sort getSort() {
                return sort;
            }

            public void setOffset(int offset) {
                this.offset = offset;
            }

            public int getOffset() {
                return offset;
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

        public static class Sort implements Serializable {

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