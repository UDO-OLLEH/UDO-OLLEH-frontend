package com.udoolleh;

import com.google.gson.annotations.SerializedName;

public class LogoutResponse {
    @SerializedName("id")
    public String id;

    @SerializedName("dateTime")
    public String dateTime;

    @SerializedName("message")
    public String message;

    @SerializedName("list")
    private List list;

    public class List{
    }

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

    public List getList() {
        return list;
    }
}
