package com.udoolleh;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FoodDetailMenuResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("message")
    private String message;

    @SerializedName("list")
    private java.util.List<FoodMenuList> list = new ArrayList<>();

    public String getId() {
        return id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }

    public java.util.List<FoodMenuList> getList() {
        return list;
    }

    public class FoodMenuList {
        @SerializedName("name")
        private String name;

        @SerializedName("photo")
        private String photo;

        @SerializedName("price")
        private String price;

        @SerializedName("description")
        private String description;

        public String getName() {
            return name;
        }

        public String getPhoto() {
            return photo;
        }

        public String getPrice() {
            return price;
        }

        public String getDescription() {
            return description;
        }
    }
}
