package com.udoolleh.view.food.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FoodResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("message")
    private String message;

    @SerializedName("list")
    private FoodList list;

    public String getId() {
        return id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }

    public FoodList getList() {
        return list;
    }

    public class FoodList {
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

        @SerializedName("number")
        private Integer number;

        @SerializedName("sort")
        private Sort__1 sort;

        @SerializedName("numberOfElements")
        private Integer numberOfElements;

        @SerializedName("first")
        private Boolean first;

        @SerializedName("size")
        private Integer size;

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

        public Integer getNumber() {
            return number;
        }

        public Sort__1 getSort() {
            return sort;
        }

        public Integer getNumberOfElements() {
            return numberOfElements;
        }

        public Boolean getFirst() {
            return first;
        }

        public Integer getSize() {
            return size;
        }

        public Boolean getEmpty() {
            return empty;
        }

        public class Content {
            @SerializedName("id")
            private String id;

            @SerializedName("name")
            private String name;

            @SerializedName("placeType")
            private String placeType;

            @SerializedName("category")
            private String category;

            @SerializedName("address")
            private String address;

            @SerializedName("imagesUrl")
            private java.util.List<String> imagesUrl = null;

            @SerializedName("totalGrade")
            private Double totalGrade;

            @SerializedName("xcoordinate")
            private String xcoordinate;

            @SerializedName("ycoordinate")
            private String ycoordinate;

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getPlaceType() {
                return placeType;
            }

            public String getCategory() {
                return category;
            }

            public String getAddress() {
                return address;
            }

            public java.util.List<String> getImagesUrl() {
                return imagesUrl;
            }

            public Double getTotalGrade() {
                return totalGrade;
            }

            public String getXcoordinate() {
                return xcoordinate;
            }

            public String getYcoordinate() {
                return ycoordinate;
            }
        }

        public class Pageable {
            @SerializedName("sort")
            private Sort sort;

            @SerializedName("pageNumber")
            private Integer pageNumber;

            @SerializedName("pageSize")
            private Integer pageSize;

            @SerializedName("offset")
            private Integer offset;

            @SerializedName("unpaged")
            private Boolean unpaged;

            @SerializedName("paged")
            private Boolean paged;

            public Sort getSort() {
                return sort;
            }

            public Integer getPageNumber() {
                return pageNumber;
            }

            public Integer getPageSize() {
                return pageSize;
            }

            public Integer getOffset() {
                return offset;
            }

            public Boolean getUnpaged() {
                return unpaged;
            }

            public Boolean getPaged() {
                return paged;
            }

            public class Sort {
                @SerializedName("sorted")
                private Boolean sorted;

                @SerializedName("unsorted")
                private Boolean unsorted;

                @SerializedName("empty")
                private Boolean empty;

                public Boolean getSorted() {
                    return sorted;
                }

                public Boolean getUnsorted() {
                    return unsorted;
                }

                public Boolean getEmpty() {
                    return empty;
                }
            }
        }

        public class Sort__1 {
            @SerializedName("sorted")
            private Boolean sorted;

            @SerializedName("unsorted")
            private Boolean unsorted;

            @SerializedName("empty")
            private Boolean empty;

            public Boolean getSorted() {
                return sorted;
            }

            public Boolean getUnsorted() {
                return unsorted;
            }

            public Boolean getEmpty() {
                return empty;
            }
        }
    }


}
