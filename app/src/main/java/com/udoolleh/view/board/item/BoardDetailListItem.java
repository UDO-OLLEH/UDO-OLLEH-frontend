package com.udoolleh.view.board.item;

public class BoardDetailListItem {
    String userIdValue;
    String email;
    String id;
    String context;
    String nickname;
    String profile;
    String createAt;

    public BoardDetailListItem(String userIdValue, String email, String id, String context, String nickname, String profile, String createAt) {
        this.userIdValue = userIdValue;
        this.email = email;
        this.id = id;
        this.context = context;
        this.nickname = nickname;
        this.profile = profile;
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

    public String getProfile() {
        return profile;
    }

    public String getCreateAt() {
        return createAt;
    }
}
