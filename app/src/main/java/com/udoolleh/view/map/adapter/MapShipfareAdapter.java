package com.udoolleh.view.map.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udoolleh.R;
import com.udoolleh.view.map.item.MapShipfareItem;

import java.util.ArrayList;

public class MapShipfareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MapShipfareItem> items = new ArrayList<MapShipfareItem>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_map_harbor_shipfare_item, parent, false);
        return new MapShipfareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MapShipfareViewHolder) holder).onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(MapShipfareItem item) {
        items.add(item);
    }

    public class MapShipfareViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView ageGroup;
        TextView roundTrip;
        TextView enterIsland;
        TextView leaveIsland;

        public MapShipfareViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            ageGroup = itemView.findViewById(R.id.ageGroup);
            roundTrip = itemView.findViewById(R.id.roundTrip);
            enterIsland = itemView.findViewById(R.id.enterIsland);
            leaveIsland = itemView.findViewById(R.id.leaveIsland);
        }

        public void onBind(MapShipfareItem mapShipfareItem) {
            ageGroup.setText(mapShipfareItem.getAgeGroup());
            String roundTripSt = String.valueOf(mapShipfareItem.getRoundTrip());
            roundTrip.setText(roundTripSt+"원");
            String enterIslandSt = String.valueOf(mapShipfareItem.getEnterIsland());
            enterIsland.setText(enterIslandSt+"원");
            String leaveIslandSt = String.valueOf(mapShipfareItem.getLeaveIsland());
            leaveIsland.setText(leaveIslandSt+"원");

            /*roundTrip.setText(mapShipfareItem.getRoundTrip());
            enterIsland.setText(mapShipfareItem.getEnterIsland());
            leaveIsland.setText(mapShipfareItem.getLeaveIsland());*/
        }
    }
}
