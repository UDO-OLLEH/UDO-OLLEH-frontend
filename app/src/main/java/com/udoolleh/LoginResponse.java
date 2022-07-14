package com.udoolleh;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("status")
    public String statusCode;

    @SerializedName("accessToken")
    public String accToken;

    @SerializedName("refreshToken")
    public String refToken;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getAccToken() {
        return accToken;
    }

    public void setAccToken(String token) {
        this.accToken = accToken;
    }

    public String getRefToken() {
        return refToken;
    }

    public void setRefToken(String refToken) {
        this.refToken = refToken;
    }
}
