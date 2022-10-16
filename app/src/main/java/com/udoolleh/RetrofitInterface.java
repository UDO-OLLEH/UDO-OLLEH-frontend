package com.udoolleh;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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
    Call<BoardResponse> getBoardResponse();

    /* Size / Page 설정시 변경
    @GET("/board/list")
    Call<BoardResponse> getBoardResponse(@Query("page") Integer page, @Query("size") Integer size);
    */

    //이미지 파일 등록시 변경
    @Multipart
    @POST("/board")
    Call<BoardWriteResponse> getBoardWriteResponse(@Part MultipartBody.Part file, @Part("requestDto") RequestBody requestDto);
}
