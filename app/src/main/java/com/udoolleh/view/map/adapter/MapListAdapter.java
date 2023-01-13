package com.udoolleh.view.map.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udoolleh.R;
import com.udoolleh.view.map.activity.MapFragmentHarbor;
import com.udoolleh.view.map.item.MapListItem;

import java.util.ArrayList;

public class MapListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<MapListItem> items = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_map_listview_item, parent, false);
        return new MapListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MapListViewHolder)holder).onBind(items.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                Context context = v.getContext();
                if (pos != RecyclerView.NO_POSITION) {
                    Intent mapIntent = new Intent(context, MapFragmentHarbor.class);
                    mapIntent.putExtra("id", items.get(pos).getId());
                    mapIntent.putExtra("name", items.get(pos).getName());
                    context.startActivity(mapIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(MapListItem item) {
        items.add(item);
    }

    public class MapListViewHolder extends RecyclerView.ViewHolder {
        Context context;
        ImageView harbor_image;
        TextView harbor_name;

        public MapListViewHolder(@NonNull View itemView) {
            super(itemView);
            harbor_name = itemView.findViewById(R.id.harbor_name);
            harbor_image = itemView.findViewById(R.id.harbor_image);
        }

        public void onBind(MapListItem mapListItem) {
            harbor_name.setText(mapListItem.getName());
            String name = harbor_name.getText().toString();
            Log.d("udoLogImage", name);
            if(name.equals("성산항")) {
                harbor_image.setImageResource(R.drawable.map_seongsan);
            } else if (name.equals("종달항")) {
                harbor_image.setImageResource(R.drawable.map_jongdal);
            }
        }
    }
}
