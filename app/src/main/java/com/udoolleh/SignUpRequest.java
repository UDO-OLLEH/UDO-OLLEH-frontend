package com.udoolleh;

import com.google.gson.annotations.SerializedName;

public class SignUpRequest {
    @SerializedName("email")
    public String inputId;

    @SerializedName("password")
    public String inputPw;

    public String getInputId() {
        return inputId;
    }

    public String getInputPw() {
        return inputPw;
    }

    public void setInputId(String inputId) {
        this.inputId = inputId;
    }

    public void setInputPw(String inputPw) {
        this.inputPw = inputPw;
    }

    public SignUpRequest(String inputId, String inputPw) {
        this.inputId = inputId;
        this.inputPw = inputPw;
    }
}
