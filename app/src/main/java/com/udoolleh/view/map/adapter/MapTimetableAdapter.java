package com.udoolleh.view.map.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udoolleh.view.map.item.MapTimetableItem;

public class MapTimetableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /*
    TODO: FoodDetailMenuAdapter 혹은 FoodDetailReviewAdapter 참고
     */

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MapTimetableViewHolder extends RecyclerView.ViewHolder {

        public MapTimetableViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void onBind(MapTimetableItem mapTimetableItem) {

        }
    }
}
