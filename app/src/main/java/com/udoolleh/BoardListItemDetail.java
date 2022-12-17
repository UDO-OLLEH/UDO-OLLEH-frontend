package com.udoolleh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardListItemDetail extends AppCompatActivity {
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

        TextView titleDetail = findViewById(R.id.titleDetail);
        TextView contextDetail = findViewById(R.id.contextDetail);
        TextView createAtDetail = findViewById(R.id.createAtDetail);

        Intent intent = getIntent();

        id = intent.getExtras().getString("id");
        String title = intent.getExtras().getString("title");
        String context = intent.getExtras().getString("context");
        String createAt = intent.getExtras().getString("createAt");

        titleDetail.setText(title);
        contextDetail.setText(context);
        createAtDetail.setText(createAt);

        boardCommentListView = findViewById(R.id.boardCommentListView);
        nonBoardCommentText = findViewById(R.id.noneBoardCommentText);

        //RecyclerView
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
}
