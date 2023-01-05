package com.udoolleh.view.user.DTO;

import com.google.gson.annotations.SerializedName;

public class UserEditProfileRequest {
    @SerializedName("nickname")
    public String nickname;

    @SerializedName("password")
    public String password;

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserEditProfileRequest(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }
}
