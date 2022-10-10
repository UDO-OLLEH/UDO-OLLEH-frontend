package com.udoolleh;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BoardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<BoardListItem> items = new ArrayList<BoardListItem>();

    public void addItem(BoardListItem item) {
        items.add(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_board_gridview_item, parent, false);
        return new BoardListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BoardListViewHolder)holder).onBind(items.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                Context context = view.getContext();
                if (pos != RecyclerView.NO_POSITION) {
                    Log.d("viewholder","눌림" + pos);
                    Intent boardListItemDetail = new Intent(context, BoardListItemDetail.class);
                    boardListItemDetail.putExtra("title", items.get(pos).getTitle());
                    boardListItemDetail.putExtra("context", items.get(pos).getContext());
                    boardListItemDetail.putExtra("createAt", items.get(pos).getCreateAt());
                    context.startActivity(boardListItemDetail);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

