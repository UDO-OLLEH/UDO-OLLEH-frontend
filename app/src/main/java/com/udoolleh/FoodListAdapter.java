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
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    ArrayList<FoodListItem> items = new ArrayList<FoodListItem>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_food_gridview_item, parent, false);
            return new FoodListViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loadmore_layout, parent, false);
            return new FoodListLoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_ITEM:
                ((FoodListViewHolder)holder).onBind(items.get(position));
                break;
            case VIEW_TYPE_LOADING:
                FoodListLoadingViewHolder foodListLoadingViewHolder = (FoodListLoadingViewHolder) holder;
                foodListLoadingViewHolder.progressBar.setIndeterminate(true);
                showLoadingView(foodListLoadingViewHolder);
                break;
        }

        //리스트 아이템 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                Context context = view.getContext();
                if (pos != RecyclerView.NO_POSITION) {
                    Intent foodListItemDetail = new Intent(context, FoodListItemDetail.class);
                    foodListItemDetail.putExtra("id", items.get(pos).getId());
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
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(FoodListItem item) {
        items.add(item);
    }

    public void removeItem(int index){
        items.remove(index);
    }

    private void showLoadingView(FoodListLoadingViewHolder holder){
        holder.progressBar.setVisibility(View.VISIBLE);
    }
}
