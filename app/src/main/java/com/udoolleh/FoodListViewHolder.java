package com.udoolleh;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.StringTokenizer;

public class FoodListViewHolder extends RecyclerView.ViewHolder {
    Context context;
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
        context = itemView.getContext();
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
        if(foodListItem.getImagesUrl() != "[]") {
            StringTokenizer st = new StringTokenizer(foodListItem.getImagesUrl(), ",");
            String tempImage1 = st.nextToken();
            StringTokenizer st1 = new StringTokenizer(tempImage1, "[");
            String image1 = st1.nextToken();

            Glide.with(context).load(image1).into(foodImagesUrl);
        } else {
            foodImagesUrl.setImageResource(R.drawable.exampleimage);
        }
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
