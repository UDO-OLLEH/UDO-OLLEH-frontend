package com.udoolleh;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardFragment extends Fragment {
    Context context;
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    RecyclerView boardGridView;
    TextView nonBoardText;

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

        //Retrofit
        BoardResponse();

        return view;
    }

    //게시판 리스트 조회
    public void BoardResponse() {

        SharedPreferences sp = context.getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String accToken = sp.getString("accToken", "");

        //Retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //BoardResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getBoardSesponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getBoardResponse().enqueue(new Callback<BoardResponse>() {
            @Override
            public void onResponse(Call<BoardResponse> call, Response<BoardResponse> response) {

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    BoardResponse result = response.body();

                    //받은 코드 저장
                    String resultCode = result.getStatus().toString();

                    //맛집 조회 성공
                    String success = "0";

                    if (resultCode.equals(success)) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        Integer status = result.getStatus();
                        String message = result.getMessage();
                        List<BoardResponse.BoardList.Content> boardList = result.getList().getContent();

                        Log.d("board", "게시판 리스트\n" +
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

                            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                            boardGridView.setLayoutManager(gridLayoutManager);

                            BoardListAdapter boardListAdapter = new BoardListAdapter();
                            for (BoardResponse.BoardList.Content board : boardList) {
                                Log.d("food", "맛집 리스트\n" +
                                        "title: " + board.getTitle() + "\n" +
                                        "context: " + board.getContext() + "\n" +
                                        "createAt: " + board.getCreateAt() + "\n"
                                );
                                boardListAdapter.addItem(new BoardListItem(board.getTitle(), board.getContext(), board.getCreateAt()));
                            }
                            boardGridView.setAdapter(boardListAdapter);
                        }

                    } else {
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
}