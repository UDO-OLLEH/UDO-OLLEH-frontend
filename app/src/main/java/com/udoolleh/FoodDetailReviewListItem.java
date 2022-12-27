package com.udoolleh;

public class FoodDetailReviewListItem {
    String reviewId;
    String nickname;
    String photo;
    String context;
    String grade;

    public FoodDetailReviewListItem(String reviewId, String nickname, String photo, String context, String grade) {
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

    public String getGrade() {
        return grade;
    }
}
