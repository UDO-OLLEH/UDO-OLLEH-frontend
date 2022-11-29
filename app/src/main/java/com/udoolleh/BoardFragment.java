package com.udoolleh;

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
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardFragment extends Fragment {
    Context context;
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    RecyclerView boardGridView;
    BoardListAdapter boardListAdapter;
    TextView nonBoardText;
    int itemPage = 0;
    private boolean isLoading = false;
    private boolean isLastLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        context = container.getContext();
        boardGridView = view.findViewById(R.id.boardGridView);
        nonBoardText = view.findViewById(R.id.noneBoardText);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        boardGridView.setLayoutManager(gridLayoutManager);
        boardListAdapter = new BoardListAdapter();

        //Retrofit
        BoardResponse();
        /*
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
                    if(layoutManager != null && lastItem == boardListAdapter.getItemCount() - 1){
                        isLoading = true;
                        BoardBackgroundTask();
                    }
                }
            }
        });
         */
        return view;
    }

    //게시판 리스트 조회
    public void BoardResponse() {

        //토큰 가져오기
        SharedPreferences sp = context.getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String accToken = sp.getString("accToken", "");

        //Retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //BoardResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getBoardSesponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getBoardResponse(itemPage, 1000).enqueue(new Callback<BoardResponse>() {
            @Override
            public void onResponse(Call<BoardResponse> call, Response<BoardResponse> response) {
                Log.d("udoLog", "게시판 조회 Data fetch success");
                Log.d("udoLog", "게시판 조화 body 내용" + response.body());
                Log.d("udoLog", "게시판 조회 성공여부" + response.isSuccessful());
                Log.d("udoLog", "게시판 조회 상태코드" + response.code());

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
                        Log.d("udoLog", "게시판 조회 성공\n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "status: " + status + "\n" +
                                "message: " + message + "\n" +
                                "content" + boardList
                        );

                        if (boardList.toString() == "[]") {
                            boardGridView.setVisibility(View.INVISIBLE);
                            nonBoardText.setVisibility(View.VISIBLE);
                        } else {
                            boardGridView.setVisibility(View.VISIBLE);
                            nonBoardText.setVisibility(View.INVISIBLE);

                            //Recycler View 레이아웃 설정


                            for (BoardResponse.BoardList.Content board : boardList) {

                                //게시판 내용 조회 로그
                                Log.d("udoLog", "게시판 리스트\n" +
                                        "id: " + board.getId() + "\n" +
                                        "title: " + board.getTitle() + "\n" +
                                        "context: " + board.getContext() + "\n" +
                                        "createAt: " + board.getCreateAt() + "\n" +
                                        "countVisit: " + board.getCountVisit() + "\n" +
                                        "countLikes: " + board.getCountLikes() + "\n"
                                );
                                boardListAdapter.addItem(new BoardListItem(board.getId(), board.getTitle(), board.getContext(), board.getCreateAt(), board.getCountVisit(), board.getCountLikes()));
                            }
                            boardGridView.setAdapter(boardListAdapter);
                        }

                    } else {

                        //상태코드 != 200일 때
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("알림")
                                .setMessage("맛집 조회 예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
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
                        .setMessage("통신실패 예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    /*
    public void BoardLoadMoreResponse() {
        //토큰 가져오기
        SharedPreferences sp = context.getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String accToken = sp.getString("accToken", "");

        //Retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //BoardResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getBoardSesponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getBoardResponse(itemPage, 50).enqueue(new Callback<BoardResponse>() {
            @Override
            public void onResponse(Call<BoardResponse> call, Response<BoardResponse> response) {
                Log.d("udoLog", "게시판 조회 Data fetch success");
                Log.d("udoLog", "게시판 조화 body 내용" + response.body());
                Log.d("udoLog", "게시판 조회 성공여부" + response.isSuccessful());
                Log.d("udoLog", "게시판 조회 상태코드" + response.code());

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
                        Log.d("udoLog", "게시판 조회 성공\n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "status: " + status + "\n" +
                                "message: " + message + "\n" +
                                "content" + boardList
                        );

                        for (BoardResponse.BoardList.Content board : boardList) {

                            //게시판 내용 조회 로그
                            Log.d("udoLog", "게시판 리스트\n" +
                                    "id: " + board.getId() + "\n" +
                                    "title: " + board.getTitle() + "\n" +
                                    "context: " + board.getContext() + "\n" +
                                    "createAt: " + board.getCreateAt() + "\n" +
                                    "countVisit: " + board.getCountVisit() + "\n" +
                                    "countLikes: " + board.getCountLikes() + "\n"
                            );
                            boardListAdapter.addItem(new BoardListItem(board.getId(), board.getTitle(), board.getContext(), board.getCreateAt(), board.getCountVisit(), board.getCountLikes()));
                        }
                        boardListAdapter.notifyDataSetChanged();
                        isLoading = false;

                    } else {

                        //상태코드 != 200일 때
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("알림")
                                .setMessage("맛집 조회 예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
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
                        .setMessage("통신실패 예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    private void BoardBackgroundTask() {
        boardListAdapter.addItem(null);
        boardListAdapter.notifyItemInserted(boardListAdapter.getItemCount()-1);
        itemPage++;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BoardLoadMoreResponse();
            }
        },1000);
    }
    */
}