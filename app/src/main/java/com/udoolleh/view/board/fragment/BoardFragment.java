package com.udoolleh.view.board.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.udoolleh.R;
import com.udoolleh.retrofit.RetrofitClient;
import com.udoolleh.retrofit.RetrofitInterface;
import com.udoolleh.view.board.item.BoardListItem;
import com.udoolleh.view.board.DTO.BoardResponse;
import com.udoolleh.view.board.adapter.BoardListAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardFragment extends Fragment {
    Context context;
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    RecyclerView boardGridView;
    BoardListAdapter boardListAdapter;
    TextView noneBoardText;
    int itemPage = 0;
    private boolean isLoading = false;
    private boolean isLastLoading = false;
    Spinner spinner;
    String[] spinner_items = {"최근순", "좋아요순", "조회순"};
    int size = 20;
    String sort_;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        context = container.getContext();

        //RecyclerView
        boardGridView = view.findViewById(R.id.boardGridView);
        noneBoardText = view.findViewById(R.id.noneBoardText);


        //Spinner
        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinner_items);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner_items[i] == "최근순") {
                    String sort = "createAt";
                    sort_ = "createAt";
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                    boardGridView.setLayoutManager(gridLayoutManager);
                    boardListAdapter = new BoardListAdapter();
                    itemPage = 0;

                    //Retrofit 게시판 최초 조회
                    BoardResponse(sort);
                    boardListAdapter.notifyDataSetChanged();

                } else if(spinner_items[i] == "좋아요순") {
                    String sort = "countLikes";
                    sort_ = "countLikes";
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                    boardGridView.setLayoutManager(gridLayoutManager);
                    boardListAdapter = new BoardListAdapter();
                    itemPage = 0;

                    //Retrofit 게시판 최초 조회
                    BoardResponse(sort);
                    boardListAdapter.notifyDataSetChanged();

                } else if(spinner_items[i] == "조회순") {
                    String sort = "countVisit";
                    sort_ = "countVisit";
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                    boardGridView.setLayoutManager(gridLayoutManager);
                    boardListAdapter = new BoardListAdapter();
                    itemPage = 0;

                    //Retrofit 게시판 최초 조회
                    BoardResponse(sort);
                    boardListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //게시판 스크롤 시 추가 로딩
        boardGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int lastItem = layoutManager.findLastVisibleItemPosition();
                Log.d("lastitem", lastItem + "");

                if(!isLoading & !isLastLoading){
                    if(layoutManager != null && lastItem == boardListAdapter.getItemCount() - 1) {
                        isLoading = true;
                        BoardBackgroundTask(sort_);
                    }
                }
            }
        });

        return view;
    }

    //게시판 리스트 조회
    public void BoardResponse(String sort) {
        //토큰 가져오기
        SharedPreferences sp = context.getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String accToken = sp.getString("accToken", "");
        String userIdValue = sp.getString("UserIdValue", "");

        //Retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //BoardResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getBoardResponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getBoardResponse(itemPage, sort, size).enqueue(new Callback<BoardResponse>() {
            @Override
            public void onResponse(Call<BoardResponse> call, Response<BoardResponse> response) {
                Log.d("udoLog", "게시판 조회 body 내용 = " + response.body());
                Log.d("udoLog", "게시판 조회 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "게시판 조회 상태코드 = " + response.code());

                //통신 성공
                if(response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    BoardResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //게시판 조회 성공
                    int success = 200;

                    if(resultCode == success) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        Integer status = result.getStatus();
                        String message = result.getMessage();
                        List<BoardResponse.BoardList.Content> boardList = result.getList().getContent();

                        //게시판 조회 로그
                        Log.d("udoLog", "게시판 조회 = \n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "status: " + status + "\n" +
                                "message: " + message + "\n" +
                                "content" + boardList
                        );

                        if(boardList.toString() == "[]") {
                            boardGridView.setVisibility(View.INVISIBLE);
                            noneBoardText.setVisibility(View.VISIBLE);
                        } else {
                            boardGridView.setVisibility(View.VISIBLE);
                            noneBoardText.setVisibility(View.INVISIBLE);

                            //Recycler View 레이아웃 설정
                            for(BoardResponse.BoardList.Content board : boardList) {

                                //게시판 내용 조회 로그
                                Log.d("udoLog", "게시판 조회 리스트 = \n" +
                                        "email" + board.getEmail() + "\n" +
                                        "id: " + board.getId() + "\n" +
                                        "title: " + board.getTitle() + "\n" +
                                        "context: " + board.getContext() + "\n" +
                                        "createAt: " + board.getCreateAt() + "\n" +
                                        "countVisit: " + board.getCountVisit() + "\n" +
                                        "countLikes: " + board.getCountLikes() + "\n"
                                );
                                boardListAdapter.addItem(new BoardListItem(userIdValue, board.getEmail(), board.getId(), board.getTitle(), board.getContext(), board.getCreateAt(), board.getCountVisit(), board.getCountLikes()));
                            }
                            boardGridView.setAdapter(boardListAdapter);
                        }

                    } else {
                        //상태코드 != 200일 때
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("알림")
                                .setMessage("맛집 조회를 할 수 없습니다.\n 다시 시도해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<BoardResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }


    public void BoardLoadMoreResponse(String sort) {
        //로딩 중 항목 삭제
        boardListAdapter.removeItem(boardListAdapter.getItemCount() - 1);
        int scrollPosition = boardListAdapter.getItemCount();
        boardListAdapter.notifyItemRemoved(scrollPosition);

        //토큰 가져오기
        SharedPreferences sp = context.getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String accToken = sp.getString("accToken", "");
        String userIdValue = sp.getString("UserIdValue", "");

        //Retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //BoardResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getBoardSesponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getBoardResponse(itemPage, sort, size).enqueue(new Callback<BoardResponse>() {
            @Override
            public void onResponse(Call<BoardResponse> call, Response<BoardResponse> response) {
                Log.d("udoLog", "게시판 조회 추가 로딩 body 내용 = " + response.body());
                Log.d("udoLog", "게시판 조회 추가 로딩 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "게시판 조회 추가 로딩 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    BoardResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //게시판 조회 성공
                    int success = 200;

                    if (resultCode == success) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        Integer status = result.getStatus();
                        String message = result.getMessage();
                        List<BoardResponse.BoardList.Content> boardList = result.getList().getContent();

                        //게시판 조회 로그
                        Log.d("udoLog", "게시판 조회 추가 로딩 성공 = \n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "status: " + status + "\n" +
                                "message: " + message + "\n" +
                                "content" + boardList
                        );

                        //Recycler View 레이아웃 설정
                        for (BoardResponse.BoardList.Content board : boardList) {

                            //게시판 내용 조회 로그
                            Log.d("udoLog", "게시판 조회 추가 로딩 리스트 = \n" +
                                    "email" + board.getEmail() + "\n" +
                                    "id: " + board.getId() + "\n" +
                                    "title: " + board.getTitle() + "\n" +
                                    "context: " + board.getContext() + "\n" +
                                    "createAt: " + board.getCreateAt() + "\n" +
                                    "countVisit: " + board.getCountVisit() + "\n" +
                                    "countLikes: " + board.getCountLikes() + "\n"
                            );
                            boardListAdapter.addItem(new BoardListItem(userIdValue, board.getEmail(), board.getId(), board.getTitle(), board.getContext(), board.getCreateAt(), board.getCountVisit(), board.getCountLikes()));
                        }
                        boardListAdapter.notifyDataSetChanged();
                        isLoading = false;

                    } else {
                        //상태코드 != 200일 때
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("알림")
                                .setMessage("맛집 조회를 할 수 없습니다.\n 다시 시도해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<BoardResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    //게시판 추가 로딩 비동기 처리
    private void BoardBackgroundTask(String sort) {
        boardListAdapter.addItem(null);
        boardListAdapter.notifyItemInserted(boardListAdapter.getItemCount()-1);
        itemPage++;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BoardLoadMoreResponse(sort);
            }
        },1000);
    }
}