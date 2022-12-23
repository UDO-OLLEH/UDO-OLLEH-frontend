package com.udoolleh;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BoardDetailViewHolder extends RecyclerView.ViewHolder {
    TextView boardCommentNickname;
    TextView boardCommentCreateAt;
    TextView boardCommentPhoto;
    TextView boardCommentContext;

    public BoardDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        boardCommentNickname = itemView.findViewById(R.id.boardCommentNickname);
        boardCommentCreateAt = itemView.findViewById(R.id.boardCommentCreateAt);
        boardCommentPhoto = itemView.findViewById(R.id.boardCommentPhoto);
        boardCommentContext = itemView.findViewById(R.id.boardCommentContext);
    }

    public void onBind(BoardDetailListItem boardDetailListItem) {
        boardCommentNickname.setText(boardDetailListItem.getNickname());
        boardCommentCreateAt.setText(boardDetailListItem.getCreateAt());
        boardCommentPhoto.setText(boardDetailListItem.getPhoto());
        boardCommentContext.setText(boardDetailListItem.getContext());
    }
}
