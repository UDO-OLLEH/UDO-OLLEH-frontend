package com.udoolleh;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginInterface {
    //@통신 방식("통신 API명")
    @POST("/udo/login")
    Call<LoginResponse> getLoginResponse(@Body LoginRequest loginRequest);

    @POST("/udo/register")
    Call<SignUpResponse> getSignUpResponse(@Body SignUpRequest signUpRequest);
}
