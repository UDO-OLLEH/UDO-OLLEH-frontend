package com.udoolleh;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FoodDetailReviewResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("message")
    private String message;

    @SerializedName("list")
    private java.util.List<FoodReviewList> list = new ArrayList<>();

    public String getId() {
        return id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }

    public java.util.List<FoodReviewList> getList() {
        return list;
    }

    public class FoodReviewList {
        @SerializedName("reviewId")
        private String reviewId;

        @SerializedName("nickname")
        private String nickname;

        @SerializedName("photo")
        private String photo;

        @SerializedName("context")
        private String context;

        @SerializedName("grade")
        private String grade;

        public String getReviewId() {
            return reviewId;
        }

        public String getNickname() {
            return nickname;
        }

        public String getPhoto() {
            return photo;
        }

        public String getContext() {
            return context;
        }

        public String getGrade() {
            return grade;
        }
    }
}
