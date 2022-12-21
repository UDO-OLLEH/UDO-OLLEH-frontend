package com.udoolleh;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BoardListItemDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<BoardListItemDetailListItem> items = new ArrayList<BoardListItemDetailListItem>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_board_detail_listview_item, parent, false);
        return new BoardListItemDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BoardListItemDetailViewHolder)holder).onBind(items.get(position));

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

    public void addItem(BoardListItemDetailListItem item) {
        items.add(item);
    }
}
