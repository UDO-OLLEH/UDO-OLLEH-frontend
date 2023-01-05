package com.udoolleh.view.board.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.udoolleh.R;
import com.udoolleh.view.board.activity.BoardDetail;
import com.udoolleh.view.board.item.BoardListItem;

import java.util.ArrayList;

public class BoardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    ArrayList<BoardListItem> items = new ArrayList<BoardListItem>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_board_gridview_item, parent, false);
            return new BoardListViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loadmore_layout, parent,false);
            return new BoardListLoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_ITEM:
                ((BoardListViewHolder)holder).onBind(items.get(position));
                break;
            case VIEW_TYPE_LOADING:
                BoardListLoadingViewHolder boardListLoadingViewHolder = (BoardListLoadingViewHolder) holder;
                boardListLoadingViewHolder.progressBar.setIndeterminate(true);
                showLoadingView(boardListLoadingViewHolder);
                break;
        }


        //게시판 리스트 아이템 클릭시 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                Context context = view.getContext();
                if (pos != RecyclerView.NO_POSITION) {
                    Intent boardListItemDetail = new Intent(context, BoardDetail.class);
                    boardListItemDetail.putExtra("userIdValue", items.get(pos).getUserIdValue());
                    boardListItemDetail.putExtra("email", items.get(pos).getEmail());
                    boardListItemDetail.putExtra("id", items.get(pos).getId());
                    boardListItemDetail.putExtra("title", items.get(pos).getTitle());
                    boardListItemDetail.putExtra("context", items.get(pos).getContext());
                    boardListItemDetail.putExtra("createAt", items.get(pos).getCreateAt());
                    context.startActivity(boardListItemDetail);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(BoardListItem item) {
        items.add(item);
    }

    public void removeItem(int index){
        items.remove(index);
    }

    private void showLoadingView(BoardListLoadingViewHolder holder){
        holder.progressBar.setVisibility(View.VISIBLE);
    }

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

    public class BoardListLoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public BoardListLoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.loadmore);
        }

        public void onBind(BoardListItem boardListItem) {

        }
    }
}

