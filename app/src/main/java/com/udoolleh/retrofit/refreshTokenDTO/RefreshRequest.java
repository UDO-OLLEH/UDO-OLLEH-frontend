package com.udoolleh.retrofit.refreshTokenDTO;

import com.google.gson.annotations.SerializedName;

public class RefreshRequest {
    @SerializedName("refreshToken")
    public String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public RefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
