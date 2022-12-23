package com.udoolleh;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
