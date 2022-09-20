package com.udoolleh;

import com.google.gson.annotations.SerializedName;

public class FoodResponseList {
    @SerializedName("name")
    public String name;

    @SerializedName("placeType")
    public String placeType;

    @SerializedName("category")
    public String category;

    @SerializedName("address")
    public String address;

    @SerializedName("imagesUrl")
    public String imagesUrl;

    @SerializedName("totalGrade")
    public String totalGrade;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceType() {
        return placeType;
    }
    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }
    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getTotalGrade() {
        return totalGrade;
    }
    public void setTotalGrade(String totalGrade) {
        this.totalGrade = totalGrade;
    }

    /* FoodResponse

    @SerializedName("id")
    public String id;

    @SerializedName("dateTime")
    public String dateTime;

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("list")
    public List<FoodResponseList> list;

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

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public List<FoodResponseList> getList() {
        return list;
    }
    public void setList(List<FoodResponseList> list) {
        this.list = list;
    }
     */
}
