package com.udoolleh.view.food.activity;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.udoolleh.R;
import com.udoolleh.retrofit.RetrofitClient;
import com.udoolleh.retrofit.RetrofitInterface;
import com.udoolleh.view.drawer.DTO.UserResponse;
import com.udoolleh.view.food.DTO.FoodDetailMenuResponse;
import com.udoolleh.view.food.DTO.FoodDetailReviewDeleteResponse;
import com.udoolleh.view.food.DTO.FoodDetailReviewResponse;
import com.udoolleh.view.food.adapter.FoodImageSliderAdapter;
import com.udoolleh.view.food.adapter.FoodDetailMenuAdapter;
import com.udoolleh.view.food.item.FoodDetailMenuListItem;
import com.udoolleh.view.food.adapter.FoodDetailReviewAdapter;
import com.udoolleh.view.food.item.FoodDetailReviewListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodDetail extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    Context context;
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    FoodDetailMenuAdapter foodDetailMenuAdapter;
    FoodDetailReviewAdapter foodDetailReviewAdapter;
    RecyclerView foodMenuListView, foodReviewListView;
    Toolbar food_toolbar;
    String id, imagesUrl, name, address, _name, _address;
    private ViewPager2 food_detail_viewpager_slider;
    ImageView navigation_profile_image;
    TextView navigation_nickname;
    Button foodReviewButton;
    double totalGrade, latitude, longitude;
    ArrayList<FoodDetailReviewListItem> mArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_food_detail);
        context = getApplicationContext();

        //Google Maps API
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.foodMapView);
        mapFragment.getMapAsync(this);

        //NavigationView
        navigation_profile_image = findViewById(R.id.navigation_profile_image);
        navigation_nickname = findViewById(R.id.navigation_nickname);
        UserResponse();

        //Drawer Layout
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);

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
        food_toolbar = (Toolbar)findViewById(R.id.food_toolbar);
        setSupportActionBar(food_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout)findViewById(R.id.food_toolbar_layout);
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

        _name = intent.getExtras().getString("name");
        _address = intent.getExtras().getString("address");
        String xcoordinate = intent.getExtras().getString("xcoordinate");
        String ycoordinate = intent.getExtras().getString("ycoordinate");
        latitude = Double.parseDouble(ycoordinate);
        longitude = Double.parseDouble(xcoordinate);

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

    //맛집 상세 메뉴 조회 통신
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

    //맛집 상세 리뷰 조회 통신
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
                            mArrayList.add(new FoodDetailReviewListItem(userIdValue, foodReview.getEmail(), foodReview.getReviewId(), foodReview.getNickname(), foodReview.getPhoto(), foodReview.getContext(), foodReview.getGrade()));
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

    //맛집 상세 리뷰 삭제 통신
    public void FoodDetailReviewDeleteResponse(String reviewId) {
        //토큰 가져오기
        SharedPreferences sp = getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String accToken = sp.getString("accToken", "");

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        retrofitInterface.getFoodDetailReviewDeleteResponse(reviewId).enqueue(new Callback<FoodDetailReviewDeleteResponse>() {
            @Override
            public void onResponse(Call<FoodDetailReviewDeleteResponse> call, Response<FoodDetailReviewDeleteResponse> response) {
                Log.d("udoLog", "식당 리뷰 삭제 body 내용 = " + response.body());
                Log.d("udoLog", "식당 리뷰 삭제 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "식당 리뷰 삭제 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    FoodDetailReviewDeleteResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //게시판 댓글 작성 성공
                    int success = 200;

                    if (resultCode == success) {
                        Toast.makeText(FoodDetail.this, "리뷰가 삭제되었습니다.", Toast.LENGTH_LONG).show();
                        finish();//인텐트 종료
                        overridePendingTransition(0, 0);//인텐트 효과 없애기
                        Intent intent = getIntent(); //인텐트
                        startActivity(intent); //액티비티 열기
                        overridePendingTransition(0, 0);//인텐트 효과 없애기

                    } else {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FoodDetail.this);
                        builder.setTitle("알림")
                                .setMessage("리뷰를 삭제할 수 없습니다.\n 다시 시도해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();

                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<FoodDetailReviewDeleteResponse> call, Throwable t) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FoodDetail.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    //리뷰 롱클릭 이벤트
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.food_review_item_menu_edit:
                Intent reviewEdit = new Intent(FoodDetail.this, FoodDetailReviewEdit.class);
                final int positionEdit = foodDetailReviewAdapter.getPosition();
                reviewEdit.putExtra("reviewId", mArrayList.get(positionEdit).getReviewId());
                reviewEdit.putExtra("context", mArrayList.get(positionEdit).getContext());
                reviewEdit.putExtra("grade", mArrayList.get(positionEdit).getGrade());
                reviewEdit.putExtra("photo", mArrayList.get(positionEdit).getPhoto());
                FoodDetail.this.startActivity(reviewEdit);
                break;

            case R.id.food_review_item_menu_delete:
                final int positionDelete = foodDetailReviewAdapter.getPosition();
                String reviewId = mArrayList.get(positionDelete).getReviewId();
                FoodDetailReviewDeleteResponse(reviewId);
                mArrayList.remove(positionDelete);
                break;
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

    //Google Maps API 마커 표시
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        LatLng PLACE = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(PLACE);
        markerOptions.title(_name);
        markerOptions.snippet(_address);

        map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(PLACE, 15));
    }
}
