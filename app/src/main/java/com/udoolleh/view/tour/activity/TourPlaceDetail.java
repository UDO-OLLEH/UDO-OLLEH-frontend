package com.udoolleh.view.tour.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.udoolleh.R;
import com.udoolleh.retrofit.RetrofitClient;
import com.udoolleh.retrofit.RetrofitInterface;
import com.udoolleh.view.tour.DTO.TourFragmentPlaceDetailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourPlaceDetail extends AppCompatActivity {
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    int id;
    Context context;
    Toolbar tour_place_toolbar;
    TextView placeName, intro, placeName2, place_latitude, place_longitude, detail_place_context;
    ImageView detail_place_photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tour_place_detail);
        context = getApplicationContext();

        //툴바 설정
        tour_place_toolbar = (Toolbar)findViewById(R.id.tour_place_toolbar);
        setSupportActionBar(tour_place_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout)findViewById(R.id.tour_place_toolbar_layout);
        toolBarLayout.setTitle("");
        toolBarLayout.setCollapsedTitleTextColor(Color.alpha(0));
        toolBarLayout.setExpandedTitleColor(Color.alpha(0));

        placeName = findViewById(R.id.placeName);
        intro = findViewById(R.id.intro);
        placeName2 = findViewById(R.id.placeName2);
        place_latitude = findViewById(R.id.place_latitude);
        place_longitude = findViewById(R.id.place_longitude);
        detail_place_photo = findViewById(R.id.detail_place_photo);
        detail_place_context = findViewById(R.id.detail_place_context);

        Intent intent = getIntent();
        id = intent.getExtras().getInt("id");

        TourPlaceDetailResponse();
    }

    public void TourPlaceDetailResponse() {
        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(null);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //TourCourseResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getCourseSesponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getTourFragmentPlaceDetailResponse(id).enqueue(new Callback<TourFragmentPlaceDetailResponse>() {
            @Override
            public void onResponse(Call<TourFragmentPlaceDetailResponse> call, Response<TourFragmentPlaceDetailResponse> response) {
                Log.d("udoLog", "추천 관광지 상세 조회 body 내용 = " + response.body());
                Log.d("udoLog", "추천 관광지 상세 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "추천 관광지 상세 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    TourFragmentPlaceDetailResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //맛집 조회 성공
                    int success = 200;

                    if (resultCode == success) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        String message = result.getMessage();

                        //여행지 코스 전체 목록 조회 로그
                        Log.d("udoLog", "추천 관광지 상세 조회 = \n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "message: " + message + "\n" +
                                "placeId" + result.getPlaceDetailList().getId() + "\n" +
                                "placeName" + result.getPlaceDetailList().getPlaceName() + "\n" +
                                "intro" + result.getPlaceDetailList().getIntro() + "\n" +
                                "context" + result.getPlaceDetailList().getContext() + "\n" +
                                "photo" + result.getPlaceDetailList().getPhoto() + "\n" +
                                "GPS" + result.getPlaceDetailList().getGps() + "\n"
                        );

                        placeName.setText(result.getPlaceDetailList().getPlaceName());
                        //placeName2.setText(result.getPlaceDetailList().getPlaceName());
                        intro.setText(result.getPlaceDetailList().getIntro());
                        place_latitude.setText(result.getPlaceDetailList().getGps().get(0).getLatitude().toString());
                        place_longitude.setText(result.getPlaceDetailList().getGps().get(0).getLongitude().toString());
                        detail_place_context.setText(result.getPlaceDetailList().getContext());
                        Glide.with(context).load(result.getPlaceDetailList().getPhoto()).into(detail_place_photo);

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TourPlaceDetail.this);
                        builder.setTitle("알림")
                                .setMessage("추천 관광지를 조회할 수 없습니다.\n 다시 시도해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<TourFragmentPlaceDetailResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TourPlaceDetail.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    //드로어 메뉴 선택
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        switch (item.getItemId()) {
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
