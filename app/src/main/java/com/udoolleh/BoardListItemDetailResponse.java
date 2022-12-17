package com.udoolleh;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BoardListItemDetailResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("status")
    private Integer status;

    @SerializedName("message")
    private String message;

    @SerializedName("list")
    private java.util.List<BoardCommentList> list = new ArrayList<>();

    public String getId() {
        return id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public java.util.List<BoardCommentList> getList() {
        return list;
    }

    public class BoardCommentList {
        @SerializedName("id")
        private String id;

        @SerializedName("context")
        private String context;

        @SerializedName("nickname")
        private String nickname;

        @SerializedName("photo")
        private String photo;

        @SerializedName("createAt")
        private String createAt;

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
}
