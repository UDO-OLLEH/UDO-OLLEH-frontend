package com.udoolleh;

public class BoardListItem {
    String title;
    String context;
    String createAt;

    public BoardListItem(String title, String context, String createAt) {
        this.title = title;
        this.context = context;
        this.createAt = createAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
