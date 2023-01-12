package com.udoolleh.view.main.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.udoolleh.R;
import com.udoolleh.retrofit.RetrofitClient;
import com.udoolleh.retrofit.RetrofitInterface;
import com.udoolleh.view.board.fragment.BoardFragment;
import com.udoolleh.view.board.activity.BoardWrite;
import com.udoolleh.view.drawer.DTO.LogoutResponse;
import com.udoolleh.view.drawer.DTO.UserResponse;
import com.udoolleh.view.food.fragment.FoodFragment;
import com.udoolleh.view.login.activity.Login;
import com.udoolleh.view.main.fragment.MainFragment;
import com.udoolleh.view.map.fragment.MapFragment;
import com.udoolleh.view.tour.fragment.TourFragment;
import com.udoolleh.view.user.activity.UserEditProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{
    public static Context context;
    Toolbar toolbar;
    ImageView navigation_profile_image, map_img, food_img, main_img, tour_img, board_img;
    TextView navigation_nickname;
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    FloatingActionButton addBoardBtn;
    private final int MapFragment = 1;
    private final int FoodFragment = 2;
    private final int MainFragment = 3;
    private final int TourFragment = 4;
    private final int BoardFragment = 5;
    String userNickname, userImage;

    @Override
    protected void onResume() {
        super.onResume();

        //NavigationView
        navigation_profile_image = findViewById(R.id.navigation_profile_image);
        navigation_nickname = findViewById(R.id.navigation_nickname);
        UserResponse();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        addBoardBtn = findViewById(R.id.addBoardBtn);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle("");
        toolBarLayout.setCollapsedTitleTextColor(Color.alpha(0));
        toolBarLayout.setExpandedTitleColor(Color.alpha(0));

        Button edit_profile = findViewById(R.id.edit_profile);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserEditProfile.class);
                intent.putExtra("userNickname", userNickname);
                intent.putExtra("userImage", userImage);
                startActivity(intent);
            }
        });

        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("우도올레")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                LogoutResponse();
                                Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();

                            }
                        })
                        .setNegativeButton("취소", null)
                        .create()
                        .show();
            }
        });

        map_img = findViewById(R.id.map_img);
        food_img = findViewById(R.id.food_img);
        main_img = findViewById(R.id.main_img);
        tour_img = findViewById(R.id.tour_img);
        board_img = findViewById(R.id.board_img);

        LinearLayout map_fragment = findViewById(R.id.map_fragment);
        LinearLayout food_fragment = findViewById(R.id.food_fragment);
        LinearLayout main_fragment = findViewById(R.id.main_fragment);
        LinearLayout tour_fragment = findViewById(R.id.tour_fragment);
        LinearLayout board_fragment = findViewById(R.id.board_fragment);

        FragmentView(MainFragment);
        map_img.setImageResource((R.drawable.category_map));
        food_img.setImageResource((R.drawable.category_food));
        main_img.setImageResource(R.drawable.category_main_selected);
        tour_img.setImageResource((R.drawable.category_tour));
        board_img.setImageResource((R.drawable.category_board));

        map_fragment.setOnClickListener(this::onClick);
        food_fragment.setOnClickListener(this::onClick);
        main_fragment.setOnClickListener(this::onClick);
        tour_fragment.setOnClickListener(this::onClick);
        board_fragment.setOnClickListener(this::onClick);

        //게시판 작성
        addBoardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BoardWrite.class);
                startActivity(intent);
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

                        userNickname = nickname;
                        userImage = profileImage;
                        Log.d("udoLog", profileImage + "");
                        navigation_nickname.setText(nickname);
                        if(profileImage == null || profileImage == "null" || profileImage == "") {
                            navigation_profile_image.setImageResource(R.drawable.base_profile_image);
                        } else {
                            Glide.with(MainActivity.this).load(profileImage).into(navigation_profile_image);
                        }

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    //로그아웃 통신
    public void LogoutResponse() {
        SharedPreferences sp = context.getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String refToken = sp.getString("refToken", "");

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(refToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //LogoutResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getLogoutSesponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getLogoutResponse().enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                Log.d("udoLog", "로그아웃 body 내용 = " + response.body());
                Log.d("udoLog", "로그아웃 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "로그아웃 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    LogoutResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //로그아웃 성공
                    int success = 200;

                    if (resultCode == success) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        String message = result.getMessage();

                        //로그아웃 로그
                        Log.d("udoLog", "로그아웃 = \n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "message: " + message + "\n"
                        );

                        setPreference("autoLoginId", "");
                        setPreference("autoLoginPw", "");
                        setPreference("accToken", "");
                        setPreference("refToken", "");
                        setPreference("UserIdValue", "");
                        setPreference("UserPwValue", "");
                        setPreference("UserNickNameValue", "");
                        setPreference("UserProfileImage", "");

                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

    //Fragment View 클릭 시 아이콘 처리
    private void onClick(View view){
        switch (view.getId()) {
            case R.id.map_fragment:
                FragmentView(MapFragment);
                map_img.setImageResource((R.drawable.category_map_selected));
                food_img.setImageResource((R.drawable.category_food));
                main_img.setImageResource(R.drawable.category_main);
                tour_img.setImageResource((R.drawable.category_tour));
                board_img.setImageResource((R.drawable.category_board));
                break;

            case R.id.food_fragment:
                FragmentView(FoodFragment);
                map_img.setImageResource((R.drawable.category_map));
                food_img.setImageResource((R.drawable.category_food_selected));
                main_img.setImageResource(R.drawable.category_main);
                tour_img.setImageResource((R.drawable.category_tour));
                board_img.setImageResource((R.drawable.category_board));
                break;

            case R.id.main_fragment:
                FragmentView(MainFragment);
                map_img.setImageResource((R.drawable.category_map));
                food_img.setImageResource((R.drawable.category_food));
                main_img.setImageResource(R.drawable.category_main_selected);
                tour_img.setImageResource((R.drawable.category_tour));
                board_img.setImageResource((R.drawable.category_board));
                break;

            case R.id.tour_fragment:
                FragmentView(TourFragment);
                map_img.setImageResource((R.drawable.category_map));
                food_img.setImageResource((R.drawable.category_food));
                main_img.setImageResource(R.drawable.category_main);
                tour_img.setImageResource((R.drawable.category_tour_selected));
                board_img.setImageResource((R.drawable.category_board));
                break;

            case R.id.board_fragment:
                FragmentView(BoardFragment);
                map_img.setImageResource((R.drawable.category_map));
                food_img.setImageResource((R.drawable.category_food));
                main_img.setImageResource(R.drawable.category_main);
                tour_img.setImageResource((R.drawable.category_tour));
                board_img.setImageResource((R.drawable.category_board_selected));
                break;
        }
    }

    //Fragment View 클릭시 fragment 전환
    private void FragmentView(int fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment) {
            case 1:
                addBoardBtn.setVisibility(View.GONE);
                com.udoolleh.view.map.fragment.MapFragment mapFragment = new MapFragment();
                transaction.replace(R.id.fragment_container, mapFragment);
                transaction.commit();
                break;

            case 2:
                addBoardBtn.setVisibility(View.GONE);
                com.udoolleh.view.food.fragment.FoodFragment foodFragment = new FoodFragment();
                transaction.replace(R.id.fragment_container, foodFragment);
                transaction.commit();
                break;

            case 3:
                addBoardBtn.setVisibility(View.GONE);
                com.udoolleh.view.main.fragment.MainFragment mainFragment = new MainFragment();
                transaction.replace(R.id.fragment_container, mainFragment);
                transaction.commit();
                break;

            case 4:
                addBoardBtn.setVisibility(View.GONE);
                com.udoolleh.view.tour.fragment.TourFragment tourFragment = new TourFragment();
                transaction.replace(R.id.fragment_container, tourFragment);
                transaction.commit();
                break;

            case 5:
                addBoardBtn.setVisibility(View.VISIBLE);
                com.udoolleh.view.board.fragment.BoardFragment boardFragment = new BoardFragment();
                transaction.replace(R.id.fragment_container, boardFragment);
                transaction.commit();
                break;
        }
    }

    //데이터를 내부 저장소에 저장하기
    public void setPreference(String key, String value){
        SharedPreferences pref = getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }
}