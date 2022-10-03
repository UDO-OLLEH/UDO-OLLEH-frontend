package com.udoolleh;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FoodResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("status")
    private Integer status;

    @SerializedName("message")
    private String message;

    @SerializedName("list")
    private List<FoodList> list = new ArrayList();

    public class FoodList {
        @SerializedName("name")
        private String name;

        @SerializedName("placeType")
        private String placeType;

        @SerializedName("category")
        private String category;

        @SerializedName("address")
        private String address;

        @SerializedName("imagesUrl")
        private java.util.List<Object> imagesUrl = null;

        @SerializedName("totalGrade")
        private Double totalGrade;

        @SerializedName("xcoordinate")
        private String xcoordinate;

        @SerializedName("ycoordinate")
        private String ycoordinate;

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

        public java.util.List<Object> getImagesUrl() {
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

    public List<FoodList> getList() {
        return list;
    }
}
