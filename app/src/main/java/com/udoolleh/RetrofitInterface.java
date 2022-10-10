package com.udoolleh;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitInterface {
    //@통신 방식("통신 API명")
    @POST("/login")
    Call<LoginResponse> getLoginResponse(@Body LoginRequest loginRequest);

    @POST("/user")
    Call<SignUpResponse> getSignUpResponse(@Body SignUpRequest signUpRequest);

    @GET("/restaurant")
    Call<FoodResponse> getFoodResponse(@Query("status") Integer status);

    @GET("/board/list")
    Call<BoardResponse> getBoardResponse(@Query("page") Integer page, @Query("size") Integer size);
}
