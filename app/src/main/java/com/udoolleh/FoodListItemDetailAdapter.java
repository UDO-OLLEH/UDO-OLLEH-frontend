package com.udoolleh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodListItemDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<FoodListItemDetailListItem> items = new ArrayList<FoodListItemDetailListItem>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_food_detail_listview_item, parent, false);
        return new FoodListItemDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FoodListItemDetailViewHolder)holder).onBind(items.get(position));

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

    public void addItem(FoodListItemDetailListItem item) {
        items.add(item);
    }
}
