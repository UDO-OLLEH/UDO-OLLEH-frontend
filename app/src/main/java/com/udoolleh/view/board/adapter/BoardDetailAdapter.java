package com.udoolleh.view.board.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.udoolleh.R;
import com.udoolleh.view.board.item.BoardDetailListItem;

import java.util.ArrayList;

public class BoardDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<BoardDetailListItem> items = new ArrayList<BoardDetailListItem>();

    public interface BoardDetailOnItemLongClickEventListener {
        void onItemLongClick(int position);
    }

    private int mPosition = RecyclerView.NO_POSITION;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_board_detail_listview_item, parent, false);
        BoardDetailOnItemLongClickEventListener listener = new BoardDetailOnItemLongClickEventListener() {
            @Override
            public void onItemLongClick(int position) {
                mPosition = position;
            }
        };
        return new BoardDetailViewHolder(view, listener);
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

    public int getPosition() {
        return mPosition;
    }

    public void addItem(BoardDetailListItem item) {
        items.add(item);
    }

    public class BoardDetailViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        Context context;
        TextView boardCommentNickname;
        TextView boardCommentCreateAt;
        ImageView boardCommentPhoto;
        TextView boardCommentContext;
        String email, userIdValue;

        public BoardDetailViewHolder(@NonNull View itemView, BoardDetailOnItemLongClickEventListener itemLongClickEventListener) {
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);

            context = itemView.getContext();
            boardCommentNickname = itemView.findViewById(R.id.boardCommentNickname);
            boardCommentCreateAt = itemView.findViewById(R.id.boardCommentCreateAt);
            boardCommentPhoto = itemView.findViewById(R.id.boardCommentPhoto);
            boardCommentContext = itemView.findViewById(R.id.boardCommentContext);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemLongClickEventListener.onItemLongClick(position);
                    }
                    return false;
                }
            });
        }

        public void onBind(BoardDetailListItem boardDetailListItem) {
            email = boardDetailListItem.getEmail();
            userIdValue = boardDetailListItem.getUserIdValue();
            boardCommentNickname.setText(boardDetailListItem.getNickname());
            boardCommentCreateAt.setText(boardDetailListItem.getCreateAt());
            Glide.with(context).load(boardDetailListItem.getProfile()).into(boardCommentPhoto);
            boardCommentContext.setText(boardDetailListItem.getContext());
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            if(email.equals(userIdValue)) {
                ((Activity) view.getContext()).getMenuInflater().inflate(R.menu.board_comment_item_menu_personal, contextMenu);
            } else {
                ((Activity) view.getContext()).getMenuInflater().inflate(R.menu.board_comment_item_menu_nonpersonal, contextMenu);
            }
        }
    }
}
