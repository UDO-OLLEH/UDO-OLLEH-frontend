package com.udoolleh;

public class BoardDetailListItem {
    String userIdValue;
    String email;
    String id;
    String context;
    String nickname;
    String photo;
    String createAt;

    public BoardDetailListItem(String userIdValue, String email, String id, String context, String nickname, String photo, String createAt) {
        this.userIdValue = userIdValue;
        this.email = email;
        this.id = id;
        this.context = context;
        this.nickname = nickname;
        this.photo = photo;
        this.createAt = createAt;
    }

    public String getUserIdValue() {
        return userIdValue;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getContext() {
        return context;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public String getCreateAt() {
        return createAt;
    }
}
