package com.udoolleh;

public class BoardListItemDetailListItem {
    String id;
    String context;
    String nickname;
    String photo;
    String createAt;

    public BoardListItemDetailListItem(String id, String context, String nickname, String photo, String createAt) {
        this.id = id;
        this.context = context;
        this.nickname = nickname;
        this.photo = photo;
        this.createAt = createAt;
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
