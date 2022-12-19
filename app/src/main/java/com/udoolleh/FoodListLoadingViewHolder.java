package com.udoolleh;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FoodListLoadingViewHolder extends RecyclerView.ViewHolder {
    ProgressBar progressBar;
    public FoodListLoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.loadmore);
    }

    public void onBind(FoodListItem foodListItem) {

    }
}
