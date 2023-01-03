package com.udoolleh;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardDetail extends AppCompatActivity {
    Context context;
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    RecyclerView boardCommentListView;
    BoardDetailAdapter boardDetailAdapter;
    TextView nonBoardCommentText;
    private boolean isLoading = false;
    private boolean isLastLoading = false;
    Toolbar toolbar;
    String id, userIdValue, email;
    ImageView navigation_profile_image;
    TextView navigation_nickname;
    EditText boardCommentWriteEditText;
    Button boardCommentWriteButton;
    LinearLayout board_personal_layout;
    Button board_personal_edit, board_personal_delete;
    ArrayList<BoardDetailListItem> mArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_board_detail);
        context = getApplicationContext();

        //NavigationView
        navigation_profile_image = findViewById(R.id.navigation_profile_image);
        navigation_nickname = findViewById(R.id.navigation_nickname);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        Button logout = findViewById(R.id.logout);
        boardCommentListView = findViewById(R.id.boardCommentListView);
        nonBoardCommentText = findViewById(R.id.noneBoardCommentText);
        boardCommentWriteEditText = findViewById(R.id.boardCommentWriteEditText);
        boardCommentWriteButton = findViewById(R.id.boardCommentWriteButton);
        board_personal_layout = findViewById(R.id.board_personal_layout);
        board_personal_edit = findViewById(R.id.board_personal_edit);
        board_personal_delete = findViewById(R.id.board_personal_delete);
        TextView titleDetail = findViewById(R.id.titleDetail);
        TextView titleDetail2 = findViewById(R.id.titleDetail2);
        TextView contextDetail = findViewById(R.id.contextDetail);
        TextView createAtDetail = findViewById(R.id.createAtDetail);

        UserResponse();

        drawer.closeDrawer(GravityCompat.END);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BoardDetail.this);
                builder.setTitle("우도올레")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new LogoutResponse();
                                Toast.makeText(BoardDetail.this, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
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
        Intent intent = getIntent();

        userIdValue = intent.getExtras().getString("userIdValue");
        email = intent.getExtras().getString("email");
        id = intent.getExtras().getString("id");
        String title = intent.getExtras().getString("title");
        String context = intent.getExtras().getString("context");
        String createAt = intent.getExtras().getString("createAt");

        if(userIdValue.equals(email)) {
            board_personal_layout.setVisibility(View.VISIBLE);
        } else {
            board_personal_layout.setVisibility(View.GONE);
        }

        titleDetail.setText(title);
        titleDetail2.setText(title);
        contextDetail.setText(context);
        createAtDetail.setText(createAt);

        //RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        boardCommentListView.setLayoutManager(linearLayoutManager);
        boardDetailAdapter = new BoardDetailAdapter();

        //댓글 등록
        boardCommentWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hideKeyboard();
                String comment = boardCommentWriteEditText.getText().toString();

                //내용 미입력 시
                if(comment.trim().length() == 0 || comment == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BoardDetail.this);
                    builder.setTitle("알림")
                            .setMessage("내용을 입력하세요.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    BoardCommentWriteResponse();
                }
            }
        });

        //Retrofit
        BoardListItemDetailResponse();

        //게시글 수정
        board_personal_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //work
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
                            Glide.with(BoardDetail.this).load(profileImage).into(navigation_profile_image);
                        }

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BoardDetail.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(BoardDetail.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    //게시판 댓글 조회
    public void BoardListItemDetailResponse() {
        //토큰 가져오기
        SharedPreferences sp = getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String accToken = sp.getString("accToken", "");
        String userIdValue = sp.getString("UserIdValue", "");

        //Retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //BoardListItemDetailResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getBoardListItemDetailResponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getBoardListItemDetailResponse(id).enqueue(new Callback<BoardDetailResponse>() {
            @Override
            public void onResponse(Call<BoardDetailResponse> call, Response<BoardDetailResponse> response) {
                Log.d("udoLog", "게시판 댓글 조회 body 내용 = " + response.body());
                Log.d("udoLog", "게시판 댓글 조회 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "게시판 댓글 조회 상태코드 = " + response.code());

                //통신 성공
                if(response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    BoardDetailResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //게시판 댓글 조회 성공
                    int success = 200;

                    if(resultCode == success) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        Integer status = result.getStatus();
                        String message = result.getMessage();
                        List<BoardDetailResponse.BoardCommentList> boardCommentList = result.getList();

                        //게시판 댓글 조회 로그
                        Log.d("udoLog", "게시판 댓글 조회 성공 = \n" +
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
                            for(BoardDetailResponse.BoardCommentList boardComment : boardCommentList) {

                                //게시판 댓글 내용 조회 로그
                                Log.d("udoLog", "게시판 댓글 조회 리스트 = \n" +
                                        "email" + boardComment.getEmail() + "\n" +
                                        "id: " + boardComment.getId() + "\n" +
                                        "context: " + boardComment.getContext() + "\n" +
                                        "nickname: " + boardComment.getNickname() + "\n" +
                                        "photo: " + boardComment.getPhoto() + "\n" +
                                        "createAt: " + boardComment.getCreateAt() + "\n"
                                );
                                boardDetailAdapter.addItem(new BoardDetailListItem(userIdValue, boardComment.getEmail(), boardComment.getId(), boardComment.getContext(), boardComment.getNickname(), boardComment.getPhoto(), boardComment.getCreateAt()));
                                mArrayList.add(new BoardDetailListItem(userIdValue, boardComment.getEmail(), boardComment.getId(), boardComment.getContext(), boardComment.getNickname(), boardComment.getPhoto(), boardComment.getCreateAt()));
                            }
                            boardCommentListView.setAdapter(boardDetailAdapter);
                        }

                    } else {
                        //상태코드 != 200일 때
                        AlertDialog.Builder builder = new AlertDialog.Builder(BoardDetail.this);
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
            public void onFailure(Call<BoardDetailResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BoardDetail.this);
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
            case R.id.board_comment_item_menu_edit:
                final EditText dlgEdt = new EditText(getApplicationContext());
                AlertDialog.Builder dlg = new AlertDialog.Builder(BoardDetail.this);
                dlg.setTitle("댓글 수정");
                dlg.setView(dlgEdt);
                dlg.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (dlgEdt.getText().toString().equals("")) {
                            Toast.makeText(BoardDetail.this, "내용을 입력하세요." , Toast.LENGTH_SHORT).show();
                        } else {
                            String editContext = dlgEdt.getText().toString();
                            final int position = boardDetailAdapter.getPosition();
                            String commentId = mArrayList.get(position).getId();
                            BoardCommentEditResponse(commentId, editContext);
                            boardDetailAdapter.notifyDataSetChanged();
                        }
                    }
                });
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dlg.show();
                break;

            case R.id.board_comment_item_menu_delete:
                final int position = boardDetailAdapter.getPosition();
                String commentId = mArrayList.get(position).getId();
                BoardCommentDeleteResponse(commentId);
                mArrayList.remove(position);
                break;
        }
        return super.onContextItemSelected(item);
    }

    //게시판 댓글 등록
    public void BoardCommentWriteResponse() {
        //토큰 가져오기
        SharedPreferences sp = getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String accToken = sp.getString("accToken", "");

        //EditText에 적은 댓글 comment에 저장
        String comment = boardCommentWriteEditText.getText().toString().trim();

        //boardCommentWriteRequest에 게시물 id와 comment를 저장
        BoardCommentWriteRequest boardCommentWriteRequest = new BoardCommentWriteRequest(id, comment);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //loginRequest에 저장된 데이터와 함께 init에서 정의한 getLoginResponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getBoardCommentWriteResponse(boardCommentWriteRequest).enqueue(new Callback<BoardCommentWriteResponse>() {
            @Override
            public void onResponse(Call<BoardCommentWriteResponse> call, Response<BoardCommentWriteResponse> response) {
                Log.d("udoLog", "게시판 댓글 작성 body 내용 = " + response.body());
                Log.d("udoLog", "게시판 댓글 작성 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "게시판 댓글 작성 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    BoardCommentWriteResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //게시판 댓글 작성 성공
                    int success = 200;

                    if (resultCode == success) {
                        Toast.makeText(BoardDetail.this, "댓글이 등록되었습니다.", Toast.LENGTH_LONG).show();
                        finish();//인텐트 종료
                        overridePendingTransition(0, 0);//인텐트 효과 없애기
                        Intent intent = getIntent(); //인텐트
                        startActivity(intent); //액티비티 열기
                        overridePendingTransition(0, 0);//인텐트 효과 없애기

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BoardDetail.this);
                        builder.setTitle("알림")
                                .setMessage("댓글을 작성할 수 없습니다.\n 다시 시도해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();

                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<BoardCommentWriteResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BoardDetail.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    //댓글 수정 통신
    public void BoardCommentEditResponse(String commentId, String editContext) {
        //토큰 가져오기
        SharedPreferences sp = getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String accToken = sp.getString("accToken", "");

        //boardCommentWriteRequest에 게시물 id와 comment를 저장
        BoardCommentEditRequest boardCommentEditRequest = new BoardCommentEditRequest(commentId, editContext);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //loginRequest에 저장된 데이터와 함께 init에서 정의한 getLoginResponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getBoardCommentEditResponse(boardCommentEditRequest).enqueue(new Callback<BoardCommentEditResponse>() {
            @Override
            public void onResponse(Call<BoardCommentEditResponse> call, Response<BoardCommentEditResponse> response) {
                Log.d("udoLog", "게시판 댓글 수정 body 내용 = " + response.body());
                Log.d("udoLog", "게시판 댓글 수정 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "게시판 댓글 수정 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    BoardCommentEditResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //게시판 댓글 작성 성공
                    int success = 200;

                    if (resultCode == success) {
                        Toast.makeText(BoardDetail.this, "댓글이 수정되었습니다.", Toast.LENGTH_LONG).show();
                        finish();//인텐트 종료
                        overridePendingTransition(0, 0);//인텐트 효과 없애기
                        Intent intent = getIntent(); //인텐트
                        startActivity(intent); //액티비티 열기
                        overridePendingTransition(0, 0);//인텐트 효과 없애기

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BoardDetail.this);
                        builder.setTitle("알림")
                                .setMessage("댓글을 작성할 수 없습니다.\n 다시 시도해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();

                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<BoardCommentEditResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BoardDetail.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    //댓글 삭제 통신
    public void BoardCommentDeleteResponse(String commentId) {
        //토큰 가져오기
        SharedPreferences sp = getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String accToken = sp.getString("accToken", "");

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //loginRequest에 저장된 데이터와 함께 init에서 정의한 getLoginResponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getBoardCommentDeleteResponse(commentId).enqueue(new Callback<BoardCommentDeleteResponse>() {
            @Override
            public void onResponse(Call<BoardCommentDeleteResponse> call, Response<BoardCommentDeleteResponse> response) {
                Log.d("udoLog", "게시판 댓글 삭제 body 내용 = " + response.body());
                Log.d("udoLog", "게시판 댓글 삭제 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "게시판 댓글 삭제 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    BoardCommentDeleteResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //게시판 댓글 작성 성공
                    int success = 200;

                    if (resultCode == success) {
                        Toast.makeText(BoardDetail.this, "댓글이 삭제되었습니다.", Toast.LENGTH_LONG).show();
                        finish();//인텐트 종료
                        overridePendingTransition(0, 0);//인텐트 효과 없애기
                        Intent intent = getIntent(); //인텐트
                        startActivity(intent); //액티비티 열기
                        overridePendingTransition(0, 0);//인텐트 효과 없애기

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BoardDetail.this);
                        builder.setTitle("알림")
                                .setMessage("댓글을 삭제할 수 없습니다.\n 다시 시도해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();

                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<BoardCommentDeleteResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BoardDetail.this);
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

    //키보드 숨기기
    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(boardCommentWriteEditText.getWindowToken(), 0);
    }
}
