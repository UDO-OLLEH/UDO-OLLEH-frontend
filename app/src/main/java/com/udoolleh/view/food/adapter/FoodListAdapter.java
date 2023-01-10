package com.udoolleh.view.food.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.udoolleh.R;
import com.udoolleh.view.food.item.FoodListItem;
import com.udoolleh.view.food.activity.FoodDetail;

import java.util.ArrayList;
import java.util.StringTokenizer;

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
                    Intent foodListItemDetail = new Intent(context, FoodDetail.class);
                    foodListItemDetail.putExtra("id", items.get(pos).getId());
                    foodListItemDetail.putExtra("imagesUrl", items.get(pos).getImagesUrl());
                    foodListItemDetail.putExtra("name", items.get(pos).getName());
                    foodListItemDetail.putExtra("address", items.get(pos).getAddress());
                    foodListItemDetail.putExtra("totalGrade", items.get(pos).getTotalGrade());
                    foodListItemDetail.putExtra("xcoordinate", items.get(pos).getXcoordinate());
                    foodListItemDetail.putExtra("ycoordinate", items.get(pos).getYcoordinate());
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

    public class FoodListViewHolder extends RecyclerView.ViewHolder {
        Context context;
        ImageView foodImagesUrl;
        TextView foodName;
        TextView foodPlaceType;
        TextView foodCategory;
        TextView foodAddress;
        RatingBar foodTotalGrade;
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
            foodTotalGrade.setRating((float)foodListItem.getTotalGrade());
            //foodXcoordinate.setText(foodListItem.getXcoordinate());
            //foodYcoordinate.setText(foodListItem.getYcoordinate());
        }
    }

    public class FoodListLoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        public FoodListLoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.loadmore);
        }

        public void onBind(FoodListItem foodListItem) {

        }
    }
}
