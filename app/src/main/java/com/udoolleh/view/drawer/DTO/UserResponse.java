package com.udoolleh.view.drawer.DTO;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("id")
    public String id;

    @SerializedName("dateTime")
    public String dateTime;

    @SerializedName("message")
    public String message;

    @SerializedName("list")
    private UserList list;

    public class UserList{
        @SerializedName("nickname")
        public String nickname;

        @SerializedName("profileImage")
        public String profileImage;

        public String getNickname() {
            return nickname;
        }
        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getProfileImage() {
            return profileImage;
        }
        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public UserList getList() {
        return list;
    }
}
