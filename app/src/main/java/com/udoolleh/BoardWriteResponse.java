package com.udoolleh;

import com.google.gson.annotations.SerializedName;

public class BoardWriteResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("status")
    private String status;

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

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getList() {
        return list;
    }
}
