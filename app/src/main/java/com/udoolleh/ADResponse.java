package com.udoolleh;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ADResponse {

    @SerializedName("id")
    private String id;
    @SerializedName("dateTime")
    private String dateTime;
    @SerializedName("message")
    private String message;
    @SerializedName("list")
    private java.util.List<ADList> list = new ArrayList<>();

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

    public java.util.List<ADList> getList() {
        return list;
    }

    public void setList(java.util.List<ADList> list) {
        this.list = list;
    }

    public class ADList{
        @SerializedName("id")
        private String id;
        @SerializedName("photo")
        private String photo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }


}
