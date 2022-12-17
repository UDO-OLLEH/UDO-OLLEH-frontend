package com.udoolleh;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BoardListItemDetailViewHolder extends RecyclerView.ViewHolder {
    TextView boardCommentNickname;
    TextView boardCommentCreateAt;
    TextView boardCommentPhoto;
    TextView boardCommentContext;

    public BoardListItemDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        boardCommentNickname = itemView.findViewById(R.id.boardCommentNickname);
        boardCommentCreateAt = itemView.findViewById(R.id.boardCommentCreateAt);
        boardCommentPhoto = itemView.findViewById(R.id.boardCommentPhoto);
        boardCommentContext = itemView.findViewById(R.id.boardCommentContext);
    }

    public void onBind(BoardListItemDetailListItem boardListItemDetailListItem) {
        boardCommentNickname.setText(boardListItemDetailListItem.getNickname());
        boardCommentCreateAt.setText(boardListItemDetailListItem.getCreateAt());
        boardCommentPhoto.setText(boardListItemDetailListItem.getPhoto());
        boardCommentContext.setText(boardListItemDetailListItem.getContext());
    }
}
