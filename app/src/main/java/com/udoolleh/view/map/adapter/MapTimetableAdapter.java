package com.udoolleh.view.map.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udoolleh.R;
import com.udoolleh.view.map.item.MapListItem;
import com.udoolleh.view.map.item.MapTimetableItem;

import java.time.Period;
import java.util.ArrayList;

public class MapTimetableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<MapTimetableItem> items = new ArrayList<MapTimetableItem>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_map_harbor_timetable_item, parent, false);
        return new MapTimetableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MapTimetableViewHolder) holder).onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //notify
    public void addItem(MapTimetableItem item){
        items.add(item);
        notifyItemInserted(items.size());
    }

    public class MapTimetableViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView period;
        TextView operatingTime;

        public MapTimetableViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            period = itemView.findViewById(R.id.period);
            operatingTime = itemView.findViewById(R.id.operatingTime);
        }

        public void onBind(MapTimetableItem mapTimetableItem) {
            period.setText(mapTimetableItem.getPeriod());
            operatingTime.setText(mapTimetableItem.getOperatingTime());
        }
    }
}
