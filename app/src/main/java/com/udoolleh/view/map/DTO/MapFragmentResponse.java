package com.udoolleh.view.map.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MapFragmentResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("message")
    private String message;

    @SerializedName("list")
    private List<harborList> list = null;

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

    public List<harborList> getList() {
        return list;
    }

    public void setList(List<harborList> list) {
        this.list = list;
    }

    public class harborList {

        @SerializedName("id")
        private Integer id;

        @SerializedName("name")
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
