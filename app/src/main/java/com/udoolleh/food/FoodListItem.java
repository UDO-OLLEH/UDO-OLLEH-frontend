package com.udoolleh.food;

public class FoodListItem {
    String name;
    String placeType;
    String category;
    String address;
    String imagesUrl;
    String totalGrade;
    String xcoordinate;
    String ycoordinate;

    public FoodListItem(String name, String placeType, String category, String address, String imagesUrl, String totalGrade, String xcoordinate, String ycoordinate) {
        this.name = name;
        this.placeType = placeType;
        this.category = category;
        this.address = address;
        this.imagesUrl = imagesUrl;
        this.totalGrade = totalGrade;
        this.xcoordinate = xcoordinate;
        this.ycoordinate = ycoordinate;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getXcoordinate() {
        return xcoordinate;
    }

    public void setXcoordinate(String xcoordinate) {
        this.xcoordinate = xcoordinate;
    }

    public String getYcoordinate() {
        return ycoordinate;
    }

    public void setYcoordinate(String ycoordinate) {
        this.ycoordinate = ycoordinate;
    }
}
