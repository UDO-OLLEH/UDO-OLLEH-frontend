package com.udoolleh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FoodDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<FoodDetailListItem> items = new ArrayList<FoodDetailListItem>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_food_detail_listview_item, parent, false);
        return new FoodDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FoodDetailViewHolder)holder).onBind(items.get(position));

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

    public void addItem(FoodDetailListItem item) {
        items.add(item);
    }

    public class FoodDetailViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView foodMenuName;
        ImageView foodMenuPhoto;
        TextView foodMenuPrice;
        TextView foodMenuDescription;

        public FoodDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            foodMenuName = itemView.findViewById(R.id.foodMenuName);
            foodMenuPhoto = itemView.findViewById(R.id.foodMenuPhoto);
            foodMenuPrice = itemView.findViewById(R.id.foodMenuPrice);
            foodMenuDescription = itemView.findViewById(R.id.foodMenuDescription);
        }

        public void onBind(FoodDetailListItem foodDetailListItem) {
            foodMenuName.setText(foodDetailListItem.getName());
            Glide.with(context).load(foodDetailListItem.getPhoto()).into(foodMenuPhoto);
            foodMenuPrice.setText(foodDetailListItem.getPrice());
            foodMenuDescription.setText(foodDetailListItem.getDescription());
        }
    }
}
