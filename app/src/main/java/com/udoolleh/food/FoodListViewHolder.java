package com.udoolleh.food;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.udoolleh.R;

public class FoodListViewHolder extends RecyclerView.ViewHolder {
    ImageView foodImagesUrl;
    TextView foodName;
    TextView foodPlaceType;
    TextView foodCategory;
    TextView foodAddress;
    TextView foodTotalGrade;
    TextView foodXcoordinate;
    TextView foodYcoordinate;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FoodListViewHolder(@NonNull View itemView) {
        super(itemView);
        foodImagesUrl = itemView.findViewById(R.id.foodImagesUrl);
        foodImagesUrl.setClipToOutline(true);
        foodName = itemView.findViewById(R.id.foodName);
        //foodPlaceType = itemView.findViewById(R.id.foodPlaceType);
        //foodCategory = itemView.findViewById(R.id.foodCategory);
        foodAddress = itemView.findViewById(R.id.foodAddress);
        foodTotalGrade = itemView.findViewById(R.id.foodTotalGrade);
        //foodXcoordinate = itemView.findViewById(R.id.foodXcoordinate);
        //foodYcoordinate = itemView.findViewById(R.id.foodYcoordinate);

    }

    public void onBind(FoodListItem foodListItem) {
        //foodImagesUrl.setText(foodListItem.getImagesUrl());
        foodName.setText(foodListItem.getName());
        //foodPlaceType.setText(foodListItem.getPlaceType());
        //foodCategory.setText(foodListItem.getCategory());
        foodAddress.setText(foodListItem.getAddress());
        foodTotalGrade.setText(foodListItem.getTotalGrade());
        //foodXcoordinate.setText(foodListItem.getXcoordinate());
        //foodYcoordinate.setText(foodListItem.getYcoordinate());
    }
}
