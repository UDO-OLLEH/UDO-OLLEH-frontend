package com.udoolleh.view.board.DTO;

import com.google.gson.annotations.SerializedName;

public class BoardCommentWriteRequest {
    @SerializedName("boardId")
    public String boardId;

    @SerializedName("context")
    public String context;

    public String getBoardId() {
        return boardId;
    }

    public String getContext() {
        return context;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public BoardCommentWriteRequest(String boardId, String context) {
        this.boardId = boardId;
        this.context = context;
    }
}
