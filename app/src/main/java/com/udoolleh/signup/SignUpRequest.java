package com.udoolleh.signup;

import com.google.gson.annotations.SerializedName;

public class SignUpRequest {
    @SerializedName("email")
    public String inputId;

    @SerializedName("password")
    public String inputPw;

    @SerializedName("nickname")
    public String nickName;

    public String getInputId() {
        return inputId;
    }

    public String getInputPw() {
        return inputPw;
    }

    public String getNickName() {
        return nickName;
    }

    public void setInputId(String inputId) {
        this.inputId = inputId;
    }

    public void setInputPw(String inputPw) {
        this.inputPw = inputPw;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public SignUpRequest(String inputId, String inputPw, String nickName) {
        this.inputId = inputId;
        this.inputPw = inputPw;
        this.nickName = nickName;
    }
}
