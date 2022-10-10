package com.udoolleh;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class BoardListViewHolder extends RecyclerView.ViewHolder {
    TextView boardTitle;
    TextView boardContext;
    TextView boardCreateAt;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BoardListViewHolder(@NonNull View itemView) {
        super(itemView);
        boardTitle = itemView.findViewById(R.id.boardTitle);
        boardContext = itemView.findViewById(R.id.boardContext);
        boardCreateAt = itemView.findViewById(R.id.boardCreateAt);
    }

    public void onBind(BoardListItem boardListItem) {
        boardTitle.setText(boardListItem.getTitle());
        boardContext.setText(boardListItem.getContext());
        boardCreateAt.setText(boardListItem.getCreateAt());
    }
}
