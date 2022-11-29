package com.udoolleh.board;

public class BoardListItem {
    String id;
    String title;
    String context;
    String createAt;
    String countVisit;
    String countLikes;

    public BoardListItem(String id, String title, String context, String createAt, String countVisit, String countLikes) {
        this.id = id;
        this.title = title;
        this.context = context;
        this.createAt = createAt;
        this.countVisit = countVisit;
        this.countLikes = countLikes;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContext() {
        return context;
    }

    public String getCreateAt() {
        return createAt;
    }

    public String getCountVisit() {
        return countVisit;
    }

    public String getCountLikes() {
        return countLikes;
    }
}
