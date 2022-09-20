package com.udoolleh;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FoodResponse {
    @SerializedName("status")
    public String status;

    @SerializedName("list")
    public List<foodList> foodList = new ArrayList<>();

    public class foodList {
        @SerializedName("name") String name;
        @SerializedName("placeType") String placeType;
        @SerializedName("category") String category;
        @SerializedName("address") String address;
        @SerializedName("imagesUrl") String imagesUrl;
        @SerializedName("totalGrade") String totalGrade;

        public String getName() {return name;}
        public String getPlaceType() {return placeType;}
        public String getCategory() {return category;}
        public String getAddress() {return address;}
        public String getImagesUrl() {return imagesUrl;}
        public String getTotalGrade() {return totalGrade;}
    }

    public String getStatus() {
        return status;
    }
    public List<foodList> getList() {return foodList;}
}
