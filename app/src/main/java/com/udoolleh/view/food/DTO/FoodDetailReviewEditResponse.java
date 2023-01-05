package com.udoolleh.view.food.DTO;

import com.google.gson.annotations.SerializedName;

public class FoodDetailReviewEditResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("message")
    private String message;

    @SerializedName("list")
    private String list;

    public String getId() {
        return id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }

    public Object getList() {
        return list;
    }
}
