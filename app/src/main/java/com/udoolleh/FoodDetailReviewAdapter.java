package com.udoolleh;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FoodDetailReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<FoodDetailReviewListItem> items = new ArrayList<FoodDetailReviewListItem>();

    public interface FoodOnItemLongClickEventListener {
        void onItemLongClick(int position);
    }

    private int mPosition = RecyclerView.NO_POSITION;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_food_detail_food_review_list_item, parent, false);
        FoodOnItemLongClickEventListener listener = new FoodOnItemLongClickEventListener() {
            @Override
            public void onItemLongClick(int position) {
                mPosition = position;
            }
        };
        return new FoodDetailReviewViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FoodDetailReviewAdapter.FoodDetailReviewViewHolder)holder).onBind(items.get(position));

        //맛집 메뉴 목록 아이템 클릭시 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                Context context = view.getContext();
                if (pos != RecyclerView.NO_POSITION) {
                    //게시판 댓글 목록 클릭시 이벤트 작성

                    /*Intent foodListItemDetail = new Intent(context, FoodListItemDetail.class);
                    foodListItemDetail.putExtra("name", items.get(pos).getName());
                    foodListItemDetail.putExtra("photo", items.get(pos).getPhoto());
                    foodListItemDetail.putExtra("price", items.get(pos).getPrice());
                    foodListItemDetail.putExtra("description", items.get(pos).getDescription());
                    context.startActivity(foodListItemDetail);*/
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(FoodDetailReviewListItem item) {
        items.add(item);
    }

    public class FoodDetailReviewViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        Context context;
        TextView foodReviewNickname;
        ImageView foodReviewPhoto;
        TextView foodReviewContext;
        RatingBar foodReviewGrade;
        String email, userIdValue;

        public FoodDetailReviewViewHolder(@NonNull View itemView, FoodOnItemLongClickEventListener itemLongClickEventListener) {
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);

            context = itemView.getContext();
            foodReviewNickname = itemView.findViewById(R.id.foodReviewNickname);
            foodReviewPhoto = itemView.findViewById(R.id.foodReviewPhoto);
            foodReviewContext = itemView.findViewById(R.id.foodReviewContext);
            foodReviewGrade = itemView.findViewById(R.id.foodReviewGrade);

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

        public void onBind(FoodDetailReviewListItem foodDetailListItem) {
            email = foodDetailListItem.getEmail();
            userIdValue = foodDetailListItem.getUserIdValue();
            foodReviewNickname.setText(foodDetailListItem.getNickname());
            Glide.with(context).load(foodDetailListItem.getPhoto()).into(foodReviewPhoto);
            foodReviewContext.setText(foodDetailListItem.getContext());
            foodReviewGrade.setRating((float) foodDetailListItem.getGrade());
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            if(email.equals(userIdValue)) {
                ((Activity) view.getContext()).getMenuInflater().inflate(R.menu.food_review_item_menu_personal, contextMenu);
            } else {
                ((Activity) view.getContext()).getMenuInflater().inflate(R.menu.food_review_item_menu_nonpersonal, contextMenu);
            }
        }
    }
}
