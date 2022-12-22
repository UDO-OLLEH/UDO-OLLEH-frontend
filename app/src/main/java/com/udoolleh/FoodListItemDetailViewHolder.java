package com.udoolleh;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FoodListItemDetailViewHolder extends RecyclerView.ViewHolder {
    TextView foodMenuName;
    TextView foodMenuPhoto;
    TextView foodMenuPrice;
    TextView foodMenuDescription;

    public FoodListItemDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        foodMenuName = itemView.findViewById(R.id.foodMenuName);
        foodMenuPhoto = itemView.findViewById(R.id.foodMenuPhoto);
        foodMenuPrice = itemView.findViewById(R.id.foodMenuPrice);
        foodMenuDescription = itemView.findViewById(R.id.foodMenuDescription);
    }

    public void onBind(FoodListItemDetailListItem foodListItemDetailListItem) {
        foodMenuName.setText(foodListItemDetailListItem.getName());
        foodMenuPhoto.setText(foodListItemDetailListItem.getPhoto());
        foodMenuPrice.setText(foodListItemDetailListItem.getPrice());
        foodMenuDescription.setText(foodListItemDetailListItem.getDescription());
    }
}
