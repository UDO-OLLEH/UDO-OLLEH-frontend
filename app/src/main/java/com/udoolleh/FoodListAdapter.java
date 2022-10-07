package com.udoolleh;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /*
    //GridView
    ArrayList<FoodListItem> items = new ArrayList<FoodListItem>();
    Context context;

    public void addItem(FoodListItem item) {
        items.add(item);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        context = viewGroup.getContext();
        FoodListItem foodListItem = items.get(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_food_gridview_item, viewGroup, false);
        }

        ImageView foodImagesUrl = view.findViewById(R.id.foodImagesUrl);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            foodImagesUrl.setClipToOutline(true);
        }
        TextView foodName = view.findViewById(R.id.foodName);
        //TextView foodPlaceType = view.findViewById(R.id.foodPlaceType);
        //TextView foodCategory = view.findViewById(R.id.foodCategory);
        TextView foodAddress = view.findViewById(R.id.foodAddress);
        TextView foodTotalGrade = view.findViewById(R.id.foodTotalGrade);
        //TextView foodXcoordinate = view.findViewById(R.id.foodXcoordinate);
        //TextView foodYcoordinate = view.findViewById(R.id.foodYcoordinate);

        //foodImagesUrl.setText(foodListItem.getImagesUrl());
        foodName.setText(foodListItem.getName());
        //foodPlaceType.setText(foodListItem.getPlaceType());
        //foodCategory.setText(foodListItem.getCategory());
        foodAddress.setText(foodListItem.getAddress());
        foodTotalGrade.setText(foodListItem.getTotalGrade());
        //foodXcoordinate.setText(foodListItem.getXcoordinate());
        //foodYcoordinate.setText(foodListItem.getYcoordinate());

        return view;
    }
     */
}
