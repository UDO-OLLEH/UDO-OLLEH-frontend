package com.udoolleh.view.map.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amar.library.ui.StickyScrollView;
import com.bumptech.glide.Glide;
import com.udoolleh.view.login.activity.Login;
import com.udoolleh.view.drawer.DTO.LogoutResponse;
import com.udoolleh.R;
import com.udoolleh.retrofit.RetrofitClient;
import com.udoolleh.retrofit.RetrofitInterface;
import com.udoolleh.view.user.activity.UserEditProfile;
import com.udoolleh.view.drawer.DTO.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragmentWhitebus extends AppCompatActivity {
    StickyScrollView scrollView;
    Button btnRoute, btnTime;
    public static Context context;
    private RetrofitInterface retrofitInterface;
    private RetrofitClient retrofitClient;
    ImageView navigation_profile_image;
    String userNickname, userImage;
    TextView navigation_nickname;

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
        setContentView(R.layout.activity_whitebus);
        context = getApplicationContext();

        scrollView = findViewById(R.id.scrollView);
        btnRoute = findViewById(R.id.btnRoute);
        btnTime = findViewById(R.id.btnTime);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.fullScroll(scrollView.FOCUS_UP);
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.fullScroll(scrollView.FOCUS_DOWN);
            }
        });

        Button edit_profile = findViewById(R.id.edit_profile);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapFragmentWhitebus.this, UserEditProfile.class);
                intent.putExtra("userNickname", userNickname);
                intent.putExtra("userImage", userImage);
                startActivity(intent);
            }
        });

        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapFragmentWhitebus.this);
                builder.setTitle("우도올레")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new LogoutResponse();
                                Toast.makeText(MapFragmentWhitebus.this, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();

                            }
                        })
                        .setNegativeButton("취소", null)
                        .create()
                        .show();
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
                            Glide.with(MapFragmentWhitebus.this).load(profileImage).into(navigation_profile_image);
                        }

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MapFragmentWhitebus.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MapFragmentWhitebus.this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        switch (item.getItemId()){
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