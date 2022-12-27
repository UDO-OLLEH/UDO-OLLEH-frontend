package com.udoolleh;

public class FoodDetailReviewListItem {
    String reviewId;
    String nickname;
    String photo;
    String context;
    double grade;

    public FoodDetailReviewListItem(String reviewId, String nickname, String photo, String context, double grade) {
        this.reviewId = reviewId;
        this.nickname = nickname;
        this.photo = photo;
        this.context = context;
        this.grade = grade;
    }

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

    public double getGrade() {
        return grade;
    }
}
