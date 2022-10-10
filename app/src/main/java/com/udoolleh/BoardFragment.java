package com.udoolleh;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BoardFragment extends Fragment {
    Context context;
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        context = container.getContext();

        //Retrofit
        BoardResponse();

        return view;
    }

    //게시판 리스트 조회
    public void BoardResponse() {
        //Retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        retrofitInterface = RetrofitClient.getRetrofitInterface();


    }
}