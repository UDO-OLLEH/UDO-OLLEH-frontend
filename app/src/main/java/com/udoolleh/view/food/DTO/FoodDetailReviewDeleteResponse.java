package com.udoolleh.view.food.DTO;

import com.google.gson.annotations.SerializedName;

public class FoodDetailReviewDeleteResponse {
    @SerializedName("id")
    public String id;

    @SerializedName("dateTime")
    public String dateTime;

    @SerializedName("message")
    public String message;

    @SerializedName("list")
    public String list;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getList() {
        return list;
    }
    public void setList(String list) {
        this.list = list;
    }
}
