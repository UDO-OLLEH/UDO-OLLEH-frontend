package com.udoolleh.view.board.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

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
                    boardListItemDetail.putExtra("context", items.get(pos).getContext().replaceAll("/n", System.getProperty("line.separator")));
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

            String context = boardListItem.getContext().replaceAll("/n", System.getProperty("line.separator"));
            boardContext.setText(context);

            StringTokenizer st = new StringTokenizer(boardListItem.getCreateAt(), ".");
            String createAt = st.nextToken().replaceAll("T", " ");  //2023-01-10 07:55:46
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date = null;
            try {
                date = format.parse(createAt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String createAtTime = formatTimeString(date);
            boardCreateAt.setText(createAtTime);
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

    private static class TIME_MAXIMUM{
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }

    public static String formatTimeString(Date tempDate) {

        long curTime = System.currentTimeMillis();
        long regTime = tempDate.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String msg = null;
        if (diffTime < TIME_MAXIMUM.SEC) {
            // sec
            msg = "방금 전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            // min
            msg = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            // hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            // day
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            // day
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }

        return msg;
    }
}

