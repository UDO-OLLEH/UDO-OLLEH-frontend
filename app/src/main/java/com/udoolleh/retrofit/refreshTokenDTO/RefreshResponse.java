package com.udoolleh.retrofit.refreshTokenDTO;

import com.google.gson.annotations.SerializedName;

public class RefreshResponse {
    @SerializedName("id")
    public String id;

    @SerializedName("dateTime")
    public String dateTime;

    @SerializedName("message")
    public String message;

    @SerializedName("list")
    private TokenList list;

    public class TokenList{
        @SerializedName("accessToken")
        public String accessToken;

        @SerializedName("refreshToken")
        public String refreshToken;

        public String getAccessToken() {
            return accessToken;
        }
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
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

    public TokenList getList() {
        return list;
    }
}
