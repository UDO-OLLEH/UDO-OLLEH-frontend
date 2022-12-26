package com.udoolleh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BoardDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<BoardDetailListItem> items = new ArrayList<BoardDetailListItem>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_board_detail_listview_item, parent, false);
        return new BoardDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BoardDetailViewHolder)holder).onBind(items.get(position));

        //게시판 리스트 아이템 클릭시 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                Context context = view.getContext();
                if (pos != RecyclerView.NO_POSITION) {
                    //게시판 댓글 목록 클릭시 이벤트 작성

                    /*Intent boardListItemDetail = new Intent(context, BoardListItemDetail.class);
                    boardListItemDetail.putExtra("id", items.get(pos).getId());
                    boardListItemDetail.putExtra("title", items.get(pos).getTitle());
                    boardListItemDetail.putExtra("context", items.get(pos).getContext());
                    boardListItemDetail.putExtra("createAt", items.get(pos).getCreateAt());
                    context.startActivity(boardListItemDetail);*/
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(BoardDetailListItem item) {
        items.add(item);
    }

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
}
