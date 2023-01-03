package com.udoolleh;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodDetail extends AppCompatActivity {
    Context context;
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    RecyclerView foodMenuListView;
    RecyclerView foodReviewListView;
    FoodDetailMenuAdapter foodDetailMenuAdapter;
    FoodDetailReviewAdapter foodDetailReviewAdapter;
    Toolbar toolbar;
    String id;
    private ViewPager2 food_detail_viewpager_slider;
    ImageView navigation_profile_image;
    TextView navigation_nickname;
    Button foodReviewButton;
    String imagesUrl;
    String name;
    String address;
    double totalGrade;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_food_detail);
        context = getApplicationContext();

        //NavigationView
        navigation_profile_image = findViewById(R.id.navigation_profile_image);
        navigation_nickname = findViewById(R.id.navigation_nickname);
        UserResponse();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodDetail.this);
                builder.setTitle("우도올레")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //LogoutResponse();
                                Toast.makeText(FoodDetail.this, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .create()
                        .show();
            }
        });

        //툴바 설정
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle("");
        toolBarLayout.setCollapsedTitleTextColor(Color.alpha(0));
        toolBarLayout.setExpandedTitleColor(Color.alpha(0));

        //Intent로 게시글 텍스트 가져오기
        food_detail_viewpager_slider = findViewById(R.id.food_detail_viewpager_slider);
        //ImageView imagesUrlDetail = findViewById(R.id.imagesUrlDetail);
        TextView nameDetail = findViewById(R.id.nameDetail);
        TextView addressDetail = findViewById(R.id.addressDetail);
        RatingBar totalGradeDetail = findViewById(R.id.totalGradeDetail);

        Intent intent = getIntent();

        id = intent.getExtras().getString("id");
        imagesUrl = intent.getExtras().getString("imagesUrl");
        name = intent.getExtras().getString("name");
        address = intent.getExtras().getString("address");
        totalGrade = intent.getDoubleExtra("totalGrade", 0);

        //이미지 URL을 뷰 페이저에 넣기 (리스트 형태의 String을 StringTokenizer로 URL만 분리 후 뷰 페이저에 넣기)
        if(!imagesUrl.equals("[]")) {
            StringTokenizer st = new StringTokenizer(imagesUrl, ",");
            String tempImage1 = st.nextToken();
            String tempImage2 = st.nextToken();

            //첫번째 이미지
            StringTokenizer st1 = new StringTokenizer(tempImage1, "[");
            String image1 = st1.nextToken();

            //두번째 이미지
            StringTokenizer st2 = new StringTokenizer(tempImage2, " ");
            String tempImage2_1 = st2.nextToken();
            StringTokenizer st3 = new StringTokenizer(tempImage2_1, "]");
            String image2 = st3.nextToken();

            String[] images = new String[] {image1, image2};
            food_detail_viewpager_slider.setAdapter(new FoodImageSliderAdapter(FoodDetail.this, images));

        } else {
            //URL에 아무것도 없을 때 샘플 이미지 출력
            String exampleImage = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.exampleimage).toString();

            String[] exampleImages = new String[] {exampleImage};
            food_detail_viewpager_slider.setAdapter(new FoodImageSliderAdapter(FoodDetail.this, exampleImages));
        }

        //ViewPager
        food_detail_viewpager_slider.setOffscreenPageLimit(1);
        food_detail_viewpager_slider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

        nameDetail.setText(name);
        addressDetail.setText(address);
        totalGradeDetail.setRating((float) totalGrade);

        //메뉴 조회 RecyclerView
        foodMenuListView = findViewById(R.id.foodMenuListView);
        LinearLayoutManager menuLinearLayoutManager = new LinearLayoutManager(this);
        foodMenuListView.setLayoutManager(menuLinearLayoutManager);
        foodDetailMenuAdapter = new FoodDetailMenuAdapter();

        //메뉴 조회 Retrofit
        FoodDetailMenuResponse();

        //리뷰 조회 RecyclerView
        foodReviewListView = findViewById(R.id.foodReviewListView);
        LinearLayoutManager reviewLinearLayoutManager = new LinearLayoutManager(this);
        foodReviewListView.setLayoutManager(reviewLinearLayoutManager);
        foodDetailReviewAdapter = new FoodDetailReviewAdapter();

        //리뷰 조회 Retrofit
        FoodDetailReviewResponse();

        //리뷰 작성으로 이동
        foodReviewButton = findViewById(R.id.foodReviewButton);
        foodReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent foodDetailReviewWrite = new Intent(context, FoodDetailReviewWrite.class);
                foodDetailReviewWrite.putExtra("name", name);
                startActivity(foodDetailReviewWrite);
            }
        });
    }

    //Navigation View User Profile
    public void UserResponse() {
        SharedPreferences sp = context.getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String accToken = sp.getString("accToken", "");

        //Retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //UserResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getUserResponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getUserResponse().enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Log.d("udoLog", "유저 정보 조회 body 내용 = " + response.body());
                Log.d("udoLog", "유저 정보 조회 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "유저 정보 조회 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    UserResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //로그아웃 성공
                    int success = 200;

                    if (resultCode == success) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        String message = result.getMessage();
                        String nickname = result.getList().getNickname();
                        String profileImage = result.getList().getProfileImage();

                        //유저 정보 조회 로그
                        Log.d("udoLog", "유저 정보 조회 = \n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "message: " + message + "\n" +
                                "nickname: " + nickname + "\n" +
                                "profileImage: " + profileImage + "\n"
                        );

                        navigation_nickname.setText(nickname);
                        if(profileImage != null) {
                            Glide.with(FoodDetail.this).load(profileImage).into(navigation_profile_image);
                        }

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FoodDetail.this);
                        builder.setTitle("알림")
                                .setMessage("로그아웃을 할 수 없습니다.\n 다시 시도해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodDetail.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    public void FoodDetailMenuResponse() {
        //Retrofit 생성
        retrofitClient = RetrofitClient.getInstance(null);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //FoodListItemDetailResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getFoodListItemDetailResponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getFoodDetailMenuResponse(id).enqueue(new Callback<FoodDetailMenuResponse>() {
            @Override
            public void onResponse(Call<FoodDetailMenuResponse> call, Response<FoodDetailMenuResponse> response) {
                Log.d("udoLog", "맛집 메뉴 목록 조회 body 내용 = " + response.body());
                Log.d("udoLog", "맛집 메뉴 목록 조회 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "맛집 메뉴 목록 조회 상태코드 = " + response.code());

                //통신 성공
                if(response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    FoodDetailMenuResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //게시판 댓글 조회 성공
                    int success = 200;

                    if(resultCode == success) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        String message = result.getMessage();
                        List<FoodDetailMenuResponse.FoodMenuList> foodMenuList = result.getList();

                        //게시판 댓글 조회 로그
                        Log.d("udoLog", "맛집 메뉴 목록 조회 성공 = \n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "message: " + message + "\n" +
                                "content" + foodMenuList
                        );

                        //Recycler View 레이아웃 설정
                        for(FoodDetailMenuResponse.FoodMenuList foodMenu : foodMenuList) {

                            //게시판 댓글 내용 조회 로그
                            Log.d("udoLog", "맛집 메뉴 목록 조회 리스트 = \n" +
                                    "name: " + foodMenu.getName() + "\n" +
                                    "photo: " + foodMenu.getPhoto() + "\n" +
                                    "price: " + foodMenu.getPrice() + "\n" +
                                    "description: " + foodMenu.getDescription() + "\n"
                            );

                            foodDetailMenuAdapter.addItem(new FoodDetailMenuListItem(foodMenu.getName(), foodMenu.getPhoto(), foodMenu.getPrice(), foodMenu.getDescription()));
                        }
                        foodMenuListView.setAdapter(foodDetailMenuAdapter);

                    } else {
                        //상태코드 != 200일 때
                        AlertDialog.Builder builder = new AlertDialog.Builder(FoodDetail.this);
                        builder.setTitle("알림")
                                .setMessage("댓글을 불러올 수 없습니다.\n 다시 시도해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<FoodDetailMenuResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodDetail.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    public void FoodDetailReviewResponse() {
        SharedPreferences sp = context.getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String userIdValue = sp.getString("UserIdValue", "");

        //Retrofit 생성
        retrofitClient = RetrofitClient.getInstance(null);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //FoodListItemDetailResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getFoodListItemDetailResponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getFoodDetailReviewResponse(name).enqueue(new Callback<FoodDetailReviewResponse>() {
            @Override
            public void onResponse(Call<FoodDetailReviewResponse> call, Response<FoodDetailReviewResponse> response) {
                Log.d("udoLog", "맛집 리뷰 목록 조회 body 내용 = " + response.body());
                Log.d("udoLog", "맛집 리뷰 목록 조회 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "맛집 리뷰 목록 조회 상태코드 = " + response.code());

                //통신 성공
                if(response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    FoodDetailReviewResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //식당 리뷰 조회 성공
                    int success = 200;

                    if(resultCode == success) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        String message = result.getMessage();
                        List<FoodDetailReviewResponse.FoodReviewList> foodReviewList = result.getList();

                        //식당 리뷰 조회 로그
                        Log.d("udoLog", "맛집 리뷰 목록 조회 성공 = \n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "message: " + message + "\n" +
                                "content" + foodReviewList
                        );

                        //Recycler View 레이아웃 설정
                        for(FoodDetailReviewResponse.FoodReviewList foodReview : foodReviewList) {

                            //식당 리뷰 내용 조회 로그
                            Log.d("udoLog", "맛집 메뉴 목록 조회 리스트 = \n" +
                                    "email" + foodReview.getEmail() + "\n" +
                                    "reviewId: " + foodReview.getReviewId() + "\n" +
                                    "nickname: " + foodReview.getNickname() + "\n" +
                                    "photo: " + foodReview.getPhoto() + "\n" +
                                    "context: " + foodReview.getContext() + "\n" +
                                    "grade: " + foodReview.getGrade() + "\n"
                            );

                            foodDetailReviewAdapter.addItem(new FoodDetailReviewListItem(userIdValue, foodReview.getEmail(), foodReview.getReviewId(), foodReview.getNickname(), foodReview.getPhoto(), foodReview.getContext(), foodReview.getGrade()));
                        }
                        foodReviewListView.setAdapter(foodDetailReviewAdapter);

                    } else {
                        //상태코드 != 200일 때
                        AlertDialog.Builder builder = new AlertDialog.Builder(FoodDetail.this);
                        builder.setTitle("알림")
                                .setMessage("댓글을 불러올 수 없습니다.\n 다시 시도해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<FoodDetailReviewResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodDetail.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.food_review_item_menu_edit:
                //edit api start
            case R.id.food_review_item_menu_delete:
                //delete api start
        }
        return super.onContextItemSelected(item);
    }

    //드로어 메뉴 메뉴 목록
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_appbar_menu, menu);
        return true;
    }

    //드로어 메뉴 선택
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        switch (item.getItemId()) {
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;

            case R.id.drawer:
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_SHORT).show();
                }
                else if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.openDrawer(GravityCompat.END);
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //드로어 메뉴 열린 상태에서 뒤로가기 버튼 눌렀을 때
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
}
