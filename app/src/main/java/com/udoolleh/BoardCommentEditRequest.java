package com.udoolleh;

import com.google.gson.annotations.SerializedName;

public class BoardCommentEditRequest {
    @SerializedName("commentId")
    public String commentId;

    @SerializedName("context")
    public String context;

    public String getCommentId() {
        return commentId;
    }

    public String getContext() {
        return context;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public BoardCommentEditRequest(String commentId, String context) {
        this.commentId = commentId;
        this.context = context;
    }
}
