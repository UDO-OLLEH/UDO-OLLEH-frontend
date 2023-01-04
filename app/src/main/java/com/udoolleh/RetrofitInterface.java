package com.udoolleh;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

    //유저 정보 조회 통신
    @GET("/user")
    Call<UserResponse> getUserResponse();

    //유저 정보 수정 통신
    @PUT("/user")
    Call<UserEditProfileResponse> getUserEditProfileResponse(@Body UserEditProfileRequest userEditProfileRequest);

    //유저 프로필 사진 등록 및 수정 통신
    @Multipart
    @POST("/user/image")
    Call<UserEditProfileImageResponse> getUserEditProfileImageResponse(@Part MultipartBody.Part file);

    //맛집 리스트 조회 통신
    @GET("/restaurant")
    Call<FoodResponse> getFoodResponse(@Query("page") Integer page);

    //맛집 메뉴 목록 조회 통신
    @GET("restaurant/{id}/menu")
    Call<FoodDetailMenuResponse> getFoodDetailMenuResponse(@Path("id") String id);

    //맛집 리뷰 목록 조회 통신
    @GET("restaurant/{name}/review")
    Call<FoodDetailReviewResponse> getFoodDetailReviewResponse(@Path("name") String name);

    //맛집 리뷰 작성 통신
    @Multipart
    @POST("/restaurant/review")
    Call<FoodDetailReviewWriteResponse> getFoodDetailReviewWriteResponse(@Part MultipartBody.Part file, @Part("requestDto") RequestBody requestDto);

    //맛집 리뷰 작성 통신
    @Multipart
    @POST("/restaurant/review/{id}")
    Call<FoodDetailReviewEditResponse> getFoodDetailReviewEditResponse(@Part MultipartBody.Part file, @Part("requestDto") RequestBody requestDto, @Path("id") String id);

    //맛집 리뷰 삭제 통신
    @DELETE("restaurant/review/{id}")
    Call<FoodDetailReviewDeleteResponse> getFoodDetailReviewDeleteResponse(@Path("id") String id);

    //게시판 조회 Page / Size 설정시 사용
    @GET("/board/list")
    Call<BoardResponse> getBoardResponse(@Query("page") Integer page, @Query("size") Integer size);

    //게시판 작성 통신
    @Multipart
    @POST("/board")
    Call<BoardWriteResponse> getBoardWriteResponse(@Part MultipartBody.Part file, @Part("requestDto") RequestBody requestDto);

    //게시판 수정 통신
    @Multipart
    @POST("/board/{id}")
    Call<BoardEditResponse> getBoardEditResponse(@Part MultipartBody.Part file, @Part("updateBoardDto") RequestBody updateBoardDto, @Path("id") String id);

    //게시판 삭제 통신
    @DELETE("board/{id}")
    Call<BoardDeleteResponse> getBoardDeleteResponse(@Path("id") String id);

    //게시판 댓글 조회 통신
    @GET("/board/{id}/comment")
    Call<BoardDetailResponse> getBoardListItemDetailResponse(@Path("id") String id);

    //게시판 댓글 등록 통신
    @POST("/board/comment")
    Call<BoardCommentWriteResponse> getBoardCommentWriteResponse(@Body BoardCommentWriteRequest boardCommentWriteRequest);

    //게시판 댓글 수정 통신
    @PUT("/board/comment")
    Call<BoardCommentEditResponse> getBoardCommentEditResponse(@Body BoardCommentEditRequest boardCommentEditRequest);

    //게시판 댓글 삭제 통신
    @DELETE("/board/comment/{id}")
    Call<BoardCommentDeleteResponse> getBoardCommentDeleteResponse(@Path("id") String id);

    //광고 전체 조회 통신
    @GET("/ad")
    Call<MainFragmentAdResponse> getADResponse();

}
