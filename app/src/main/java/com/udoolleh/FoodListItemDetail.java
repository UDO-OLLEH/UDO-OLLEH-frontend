package com.udoolleh;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodListItemDetail extends AppCompatActivity {
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    RecyclerView foodMenuListView;
    FoodListItemDetailAdapter foodListItemDetailAdapter;
    Toolbar toolbar;
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_food_detail);

        //뒤로가기 버튼
        Button board_close = findViewById(R.id.board_close);
        board_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodListItemDetail.this);
                builder.setTitle("우도올레")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //LogoutResponse();
                                Toast.makeText(FoodListItemDetail.this, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);

        //Intent로 게시글 텍스트 가져오기
        TextView imagesUrlDetail = findViewById(R.id.imagesUrlDetail);
        TextView nameDetail = findViewById(R.id.nameDetail);
        TextView addressDetail = findViewById(R.id.addressDetail);
        TextView totalGradeDetail = findViewById(R.id.totalGradeDetail);

        Intent intent = getIntent();

        id = intent.getExtras().getString("id");
        String imagesUrl = intent.getExtras().getString("imagesUrl");
        String name = intent.getExtras().getString("name");
        String address = intent.getExtras().getString("address");
        String totalGrade = intent.getExtras().getString("totalGrade");

        imagesUrlDetail.setText(imagesUrl);
        nameDetail.setText(name);
        addressDetail.setText(address);
        totalGradeDetail.setText(totalGrade);

        //RecyclerView
        foodMenuListView = findViewById(R.id.foodMenuListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        foodMenuListView.setLayoutManager(linearLayoutManager);
        foodListItemDetailAdapter = new FoodListItemDetailAdapter();

        //Retrofit
        FoodListItemDetailResponse();
    }

    public void FoodListItemDetailResponse() {
        //Retrofit 생성
        retrofitClient = RetrofitClient.getInstance(null);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //FoodListItemDetailResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getFoodListItemDetailResponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getFoodListItemDetailResponse(id).enqueue(new Callback<FoodListItemDetailResponse>() {
            @Override
            public void onResponse(Call<FoodListItemDetailResponse> call, Response<FoodListItemDetailResponse> response) {
                Log.d("udoLog", "맛집 메뉴 목록 조회 body 내용 = " + response.body());
                Log.d("udoLog", "맛집 메뉴 목록 조회 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "맛집 메뉴 목록 조회 상태코드 = " + response.code());

                //통신 성공
                if(response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    FoodListItemDetailResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //게시판 댓글 조회 성공
                    int success = 200;

                    if(resultCode == success) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        String message = result.getMessage();
                        List<FoodListItemDetailResponse.FoodMenuList> foodMenuList = result.getList();

                        //게시판 댓글 조회 로그
                        Log.d("udoLog", "맛집 메뉴 목록 조회 성공 = \n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "message: " + message + "\n" +
                                "content" + foodMenuList
                        );

                        //Recycler View 레이아웃 설정
                        for(FoodListItemDetailResponse.FoodMenuList foodMenu : foodMenuList) {

                            //게시판 댓글 내용 조회 로그
                            Log.d("udoLog", "맛집 메뉴 목록 조회 리스트 = \n" +
                                    "name: " + foodMenu.getName() + "\n" +
                                    "photo: " + foodMenu.getPhoto() + "\n" +
                                    "price: " + foodMenu.getPrice() + "\n" +
                                    "description: " + foodMenu.getDescription() + "\n"
                            );
                            foodListItemDetailAdapter.addItem(new FoodListItemDetailListItem(foodMenu.getName(), foodMenu.getPhoto(), foodMenu.getPrice(), foodMenu.getDescription()));
                        }
                        foodMenuListView.setAdapter(foodListItemDetailAdapter);

                    } else {
                        //상태코드 != 200일 때
                        AlertDialog.Builder builder = new AlertDialog.Builder(FoodListItemDetail.this);
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
            public void onFailure(Call<FoodListItemDetailResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodListItemDetail.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
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
