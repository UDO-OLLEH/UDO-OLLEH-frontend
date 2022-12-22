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
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {
    /*
    @통신 방식("통신 API명")
    API 명세서 참고 후 작성
    */

    //유저 엑세스 토큰 갱신 통신
    @POST("/refreshToken")
    Call<RefreshResponse> getRefreshResponse(@Body RefreshRequest refreshRequest);

    //로그인 통신
    @POST("/login")
    Call<LoginResponse> getLoginResponse(@Body LoginRequest loginRequest);

    //로그아웃 통신
    @POST("/logout")
    Call<LogoutResponse> getLogoutResponse();

    //회원가입 통신
    @POST("/user")
    Call<SignUpResponse> getSignUpResponse(@Body SignUpRequest signUpRequest);

    //맛집 리스트 조회 통신
    @GET("/restaurant")
    Call<FoodResponse> getFoodResponse(@Query("page") Integer page);

    //맛집 메뉴 목록 조회 통신
    @GET("restaurant/{id}/menu")
    Call<FoodListItemDetailResponse> getFoodListItemDetailResponse(@Path("id") String id);

    //게시판 조회 Page / Size 설정시 사용
    @GET("/board/list")
    Call<BoardResponse> getBoardResponse(@Query("page") Integer page, @Query("size") Integer size);

    //게시판 작성 통신
    @Multipart
    @POST("/board")
    Call<BoardWriteResponse> getBoardWriteResponse(@Part MultipartBody.Part file, @Part("requestDto") RequestBody requestDto);

    //게시판 댓글 조회 통신
    @GET("/board/{id}/comment")
    Call<BoardListItemDetailResponse> getBoardListItemDetailResponse(@Path("id") String id);

    //게시판 댓글 등록 통신
    @POST("/board/comment")
    Call<BoardCommentWriteResponse> getBoardCommentWriteResponse(@Body BoardCommentWriteRequest boardCommentWriteRequest);
}
