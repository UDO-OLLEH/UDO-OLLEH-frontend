package com.udoolleh;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BoardResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("status")
    private Integer status;

    @SerializedName("message")
    private String message;

    @SerializedName("list")
    private List list;

    public String getId() {
        return id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List getList() {
        return list;
    }

    public class List {
        @SerializedName("content")
        private java.util.List<Content> content = new ArrayList<>();

        @SerializedName("pageable")
        private Pageable pageable;

        @SerializedName("last")
        private Boolean last;

        @SerializedName("totalPages")
        private Integer totalPages;

        @SerializedName("totalElements")
        private Integer totalElements;

        @SerializedName("size")
        private Integer size;

        @SerializedName("number")
        private Integer number;

        @SerializedName("sort")
        private Sort__1 sort;

        @SerializedName("first")
        private Boolean first;

        @SerializedName("numberOfElements")
        private Integer numberOfElements;

        @SerializedName("empty")
        private Boolean empty;

        public java.util.List<Content> getContent() {
            return content;
        }

        public Pageable getPageable() {
            return pageable;
        }

        public Boolean getLast() {
            return last;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public Integer getTotalElements() {
            return totalElements;
        }

        public Integer getSize() {
            return size;
        }

        public Integer getNumber() {
            return number;
        }

        public Sort__1 getSort() {
            return sort;
        }

        public Boolean getFirst() {
            return first;
        }

        public Integer getNumberOfElements() {
            return numberOfElements;
        }

        public Boolean getEmpty() {
            return empty;
        }

        public class Content {
            @SerializedName("title")
            private String title;

            @SerializedName("context")
            private String context;

            @SerializedName("createAt")
            private String createAt;

            public String getTitle() {
                return title;
            }

            public String getContext() {
                return context;
            }

            public String getCreateAt() {
                return createAt;
            }
        }

        public class Pageable {
            @SerializedName("sort")
            private Sort sort;

            @SerializedName("offset")
            private Integer offset;

            @SerializedName("pageSize")
            private Integer pageSize;

            @SerializedName("pageNumber")
            private Integer pageNumber;

            @SerializedName("paged")
            private Boolean paged;

            @SerializedName("unpaged")
            private Boolean unpaged;

            public Sort getSort() {
                return sort;
            }

            public Integer getOffset() {
                return offset;
            }

            public Integer getPageSize() {
                return pageSize;
            }

            public Integer getPageNumber() {
                return pageNumber;
            }

            public Boolean getPaged() {
                return paged;
            }

            public Boolean getUnpaged() {
                return unpaged;
            }

            public class Sort {
                @SerializedName("empty")
                private Boolean empty;

                @SerializedName("unsorted")
                private Boolean unsorted;

                @SerializedName("sorted")
                private Boolean sorted;

                public Boolean getEmpty() {
                    return empty;
                }

                public Boolean getUnsorted() {
                    return unsorted;
                }

                public Boolean getSorted() {
                    return sorted;
                }
            }
        }

        public class Sort__1 {
            @SerializedName("empty")
            private Boolean empty;

            @SerializedName("unsorted")
            private Boolean unsorted;

            @SerializedName("sorted")
            private Boolean sorted;

            public Boolean getEmpty() {
                return empty;
            }

            public Boolean getUnsorted() {
                return unsorted;
            }

            public Boolean getSorted() {
                return sorted;
            }
        }
    }
}

