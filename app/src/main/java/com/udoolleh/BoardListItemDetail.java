package com.udoolleh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardListItemDetail extends AppCompatActivity {
    Toolbar toolbar;
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    RecyclerView boardCommentListView;
    BoardListItemDetailAdapter boardListItemDetailAdapter;
    TextView nonBoardCommentText;
    private boolean isLoading = false;
    private boolean isLastLoading = false;
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_board_detail);

        //뒤로가기 버튼
        Button board_close = findViewById(R.id.board_close);
        board_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //툴바 설정
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle("");
        toolBarLayout.setCollapsedTitleTextColor(Color.alpha(0));
        toolBarLayout.setExpandedTitleColor(Color.alpha(0));

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_SHORT).show();

                    case R.id.nav_setting:
                        Toast.makeText(getApplicationContext(), "setting", Toast.LENGTH_SHORT).show();

                    case R.id.nav_example:
                        Toast.makeText(getApplicationContext(), "example", Toast.LENGTH_SHORT).show();
                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.END);
                return true;
            }
        });

        //Intent로 게시글 텍스트 가져오기
        TextView titleDetail = findViewById(R.id.titleDetail);
        TextView titleDetail2 = findViewById(R.id.titleDetail2);
        TextView contextDetail = findViewById(R.id.contextDetail);
        TextView createAtDetail = findViewById(R.id.createAtDetail);

        Intent intent = getIntent();

        id = intent.getExtras().getString("id");
        String title = intent.getExtras().getString("title");
        String context = intent.getExtras().getString("context");
        String createAt = intent.getExtras().getString("createAt");

        titleDetail.setText(title);
        titleDetail2.setText(title);
        contextDetail.setText(context);
        createAtDetail.setText(createAt);

        //RecyclerView
        boardCommentListView = findViewById(R.id.boardCommentListView);
        nonBoardCommentText = findViewById(R.id.noneBoardCommentText);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        boardCommentListView.setLayoutManager(linearLayoutManager);
        boardListItemDetailAdapter = new BoardListItemDetailAdapter();

        //Retrofit
        BoardListItemDetailResponse();
    }

    //게시판 댓글 조회
    public void BoardListItemDetailResponse() {
        //토큰 가져오기
        SharedPreferences sp = getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String accToken = sp.getString("accToken", "");

        //Retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //BoardListItemDetailResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getBoardListItemDetailResponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getBoardListItemDetailResponse(id).enqueue(new Callback<BoardListItemDetailResponse>() {
            @Override
            public void onResponse(Call<BoardListItemDetailResponse> call, Response<BoardListItemDetailResponse> response) {
                Log.d("udoLog", "게시판 댓글 조회 Data fetch success");
                Log.d("udoLog", "게시판 댓글 조회 body 내용" + response.body());
                Log.d("udoLog", "게시판 댓글 조회 성공여부" + response.isSuccessful());
                Log.d("udoLog", "게시판 댓글 조회 상태코드" + response.code());

                //통신 성공
                if(response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    BoardListItemDetailResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //게시판 댓글 조회 성공
                    int success = 200;

                    if(resultCode == success) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        Integer status = result.getStatus();
                        String message = result.getMessage();
                        List<BoardListItemDetailResponse.BoardCommentList> boardCommentList = result.getList();

                        //게시판 댓글 조회 로그
                        Log.d("udoLog", "게시판 댓글 조회 성공\n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "status: " + status + "\n" +
                                "message: " + message + "\n" +
                                "content" + boardCommentList
                        );

                        if(boardCommentList.toString() == "[]") {
                            boardCommentListView.setVisibility(View.INVISIBLE);
                            nonBoardCommentText.setVisibility(View.VISIBLE);
                        } else {
                            boardCommentListView.setVisibility(View.VISIBLE);
                            nonBoardCommentText.setVisibility(View.INVISIBLE);

                            //Recycler View 레이아웃 설정
                            for(BoardListItemDetailResponse.BoardCommentList boardComment : boardCommentList) {

                                //게시판 댓글 내용 조회 로그
                                Log.d("udoLog", "게시판 댓글 리스트\n" +
                                        "id: " + boardComment.getId() + "\n" +
                                        "context: " + boardComment.getContext() + "\n" +
                                        "nickname: " + boardComment.getNickname() + "\n" +
                                        "photo: " + boardComment.getPhoto() + "\n" +
                                        "createAt: " + boardComment.getCreateAt() + "\n"
                                );
                                boardListItemDetailAdapter.addItem(new BoardListItemDetailListItem(boardComment.getId(), boardComment.getContext(), boardComment.getNickname(), boardComment.getPhoto(), boardComment.getCreateAt()));
                            }
                            boardCommentListView.setAdapter(boardListItemDetailAdapter);
                        }

                    } else {
                        //상태코드 != 200일 때
                        AlertDialog.Builder builder = new AlertDialog.Builder(BoardListItemDetail.this);
                        builder.setTitle("알림")
                                .setMessage("댓글 조회 예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<BoardListItemDetailResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BoardListItemDetail.this);
                builder.setTitle("알림")
                        .setMessage("통신실패 예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_appbar_menu, menu);
        return true;
    }

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
