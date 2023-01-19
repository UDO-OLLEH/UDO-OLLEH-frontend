package com.udoolleh.view.map.activity;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.amar.library.ui.StickyScrollView;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.udoolleh.R;
import com.udoolleh.retrofit.RetrofitClient;
import com.udoolleh.retrofit.RetrofitInterface;
import com.udoolleh.view.drawer.DTO.LogoutResponse;
import com.udoolleh.view.drawer.DTO.UserResponse;
import com.udoolleh.view.map.DTO.MapFragmentTimetableResponse;
import com.udoolleh.view.map.adapter.MapTimetableAdapter;
import com.udoolleh.view.map.item.MapTimetableItem;
import com.udoolleh.view.user.activity.UserEditProfile;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragmentHarbor extends AppCompatActivity {

    StickyScrollView scrollView;
    Button btnRoute, btnTime, btnPrice;
    public static Context context;
    private RetrofitInterface retrofitInterface;
    private RetrofitClient retrofitClient;
    ImageView navigation_profile_image;
    String userNickname, userImage;
    TextView navigation_nickname, route_name1, route_name2, route_destination, period, operatingTime;
    MapTimetableAdapter mapTimetableAdapter;
    RecyclerView timetable_recyclerview, shipfare_recyclerview;
    ArrayList<MapTimetableItem> mapListItemArrayList = new ArrayList<>();
    int id;

    @Override
    protected void onResume() {
        super.onResume();

        //NavigationView
        navigation_profile_image = findViewById(R.id.navigation_profile_image);
        navigation_nickname = findViewById(R.id.navigation_nickname);
        UserResponse();

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.END);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map_harbor);
        context = getApplicationContext();

        scrollView = findViewById(R.id.scrollView);
        btnRoute = findViewById(R.id.btnRoute);
        btnTime = findViewById(R.id.btnTime);
        btnPrice = findViewById(R.id.btnPrice);
        Toolbar toolbar = findViewById(R.id.toolbar);
        route_name1 = findViewById(R.id.route_name1);
        route_name2 = findViewById(R.id.route_name2);
        route_destination = findViewById(R.id.route_destination);
        period = findViewById(R.id.period);
        operatingTime = findViewById(R.id.operatingTime);
        timetable_recyclerview = findViewById(R.id.timetable_recyclerview);
        shipfare_recyclerview = findViewById(R.id.shipfare_recyclerview);

        mapTimetableAdapter = new MapTimetableAdapter();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.map_toolbar_layout);
        toolBarLayout.setTitle("");
        toolBarLayout.setCollapsedTitleTextColor(Color.alpha(0));
        toolBarLayout.setExpandedTitleColor(Color.alpha(0));

        Intent intent = getIntent();
        id = intent.getExtras().getInt("id");
        route_name1.setText(intent.getExtras().getString("name"));
        route_name2.setText(intent.getExtras().getString("name"));

        //Drawer Layout
        Button edit_profile = findViewById(R.id.edit_profile);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapFragmentHarbor.this, UserEditProfile.class);
                intent.putExtra("userNickname", userNickname);
                intent.putExtra("userImage", userImage);
                startActivity(intent);
            }
        });

        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapFragmentHarbor.this);
                builder.setTitle("우도올레")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new LogoutResponse();
                                Toast.makeText(MapFragmentHarbor.this, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();

                            }
                        })
                        .setNegativeButton("취소", null)
                        .create()
                        .show();
            }
        });

        MapTimetableResponse();

        MapShipfareResponse();

        btnRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.fullScroll(scrollView.FOCUS_UP);
            }
        });

        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.fullScroll(scrollView.FOCUS_DOWN);
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
                        navigation_nickname.setText(nickname);
                        if (profileImage == null || profileImage == "null" || profileImage == "") {
                            navigation_profile_image.setImageResource(R.drawable.base_profile_image);
                        } else {
                            Glide.with(MapFragmentHarbor.this).load(profileImage).into(navigation_profile_image);
                        }

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MapFragmentHarbor.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MapFragmentHarbor.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }


    public void MapTimetableResponse() {
        /*
        TODO: 항구 시간표 조회 통신 코드 작성 후 (
            ... retrofitInterface.getMapFragmentTimetableResponse(id).enqueue(new Callback<FoodDetailMenuResponse>() ...
         ),
         통신 성공 시 destination textView에 넣기,
         시간표 period, operatingTime 어댑터에 addItem, setAdapter
         */

        SharedPreferences sp = context.getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String accToken = sp.getString("accToken", "");

        //Retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        retrofitInterface.getMapFragmentTimetableResponse(id).enqueue(new Callback<MapFragmentTimetableResponse>() {
            @Override
            public void onResponse(Call<MapFragmentTimetableResponse> call, Response<MapFragmentTimetableResponse> response) {

                Log.d("udoLog", "항구 시간표 조회 body 내용 = " + response.body());
                Log.d("udoLog", "항구 시간표 조회 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "항구 시간표 조회 상태코드 = " + response.code());

                if (response.isSuccessful() && response.body() != null) {

                    MapFragmentTimetableResponse result = response.body();

                    int resultCode = response.code();

                    int success = 200;

                    if (resultCode == success) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        String message = result.getMessage();
                        //List<MapFragmentTimetableResponse.timetableList> timetableList = result.getList();
                        //
                        MapFragmentTimetableResponse.timetableList timetableList = result.getList();

                        //항구 시간표 조회 로그
                        Log.d("udoLog", "항구 시간표 조회 성공 = \n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "message: " + message + "\n" +
                                "content" + timetableList + "\n"

                        );

                        Log.d("udoLog", "항구 시간표 조회 리스트 = \n" +
                                "destination" + timetableList.getDestination() + "\n" +
                                "timetableDtos: " + timetableList.getTimetableDtos() + "\n"
                        );

                        String destination = timetableList.getDestination();
                        route_destination.setText(destination);

                        for (MapFragmentTimetableResponse.timetableList.timetableDtos timetableDtos : timetableList.getTimetableDtos()) {
                            Log.d("udoLog", "항구 시간표 조회 리스트 = \n" +
                                    "id" + timetableDtos.getId() + "\n" +
                                    "period: " + timetableDtos.getPeriod() + "\n" +
                                    "operatingTime" + timetableDtos.getOperatingTime()
                            );

                            mapListItemArrayList.add(new MapTimetableItem(timetableDtos.getPeriod(), timetableDtos.getOperatingTime()));
                        }
                        timetable_recyclerview.setAdapter(mapTimetableAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<MapFragmentTimetableResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapFragmentHarbor.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                //항구 시간표 조회 로그.
                //t.printStackTrace();
            }
        });

    }

    public void MapShipfareResponse() {
        /*
        TODO: 배 요금 조회 통신 코드 작성 후 (
            ... retrofitInterface.getMapFragmentShipfareResponse(id).enqueue(new Callback<FoodDetailMenuResponse>() ...
         ),
         통신 성공 시 요금표 ageGroup, roundTrip, enterIsland, leaveIsland 어댑터에 addItem, setAdapter
         */
    }

    //드로어 메뉴 메뉴 목록
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_appbar_menu, menu);
        return true;
    }

    //드로어 메뉴 선택
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        switch (item.getItemId()) {
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;

            case R.id.drawer:
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_SHORT).show();
                } else if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
        }
        return super.onOptionsItemSelected(item);
    }
}