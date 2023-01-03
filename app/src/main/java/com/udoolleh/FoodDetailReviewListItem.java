package com.udoolleh;

public class FoodDetailReviewListItem {
    String userIdValue;
    String email;
    String reviewId;
    String nickname;
    String photo;
    String context;
    double grade;

    public FoodDetailReviewListItem(String userIdValue, String email, String reviewId, String nickname, String photo, String context, double grade) {
        this.userIdValue = userIdValue;
        this.email = email;
        this.reviewId = reviewId;
        this.nickname = nickname;
        this.photo = photo;
        this.context = context;
        this.grade = grade;
    }

    public String getUserIdValue() {
        return userIdValue;
    }

    public String getEmail() {
        return email;
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
