package com.udoolleh;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BoardListLoadingViewHolder extends RecyclerView.ViewHolder{
    ProgressBar progressBar;
    public BoardListLoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.loadmore);
    }

    public void onBind(BoardListItem boardListItem) {

    }
}
