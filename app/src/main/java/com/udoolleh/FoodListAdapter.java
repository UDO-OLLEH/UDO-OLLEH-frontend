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

public class FoodListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<FoodListItem> items = new ArrayList<FoodListItem>();

    public void addItem(FoodListItem item) {
        items.add(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_food_gridview_item, parent, false);
        return new FoodListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FoodListViewHolder)holder).onBind(items.get(position));

        //리스트 아이템 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                Context context = view.getContext();
                if (pos != RecyclerView.NO_POSITION) {
                    Intent foodListItemDetail = new Intent(context, FoodListItemDetail.class);
                    foodListItemDetail.putExtra("imagesUrl", items.get(pos).getImagesUrl());
                    foodListItemDetail.putExtra("name", items.get(pos).getName());
                    foodListItemDetail.putExtra("address", items.get(pos).getAddress());
                    foodListItemDetail.putExtra("totalGrade", items.get(pos).getTotalGrade());
                    context.startActivity(foodListItemDetail);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
