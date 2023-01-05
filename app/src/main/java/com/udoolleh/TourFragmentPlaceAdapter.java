package com.udoolleh;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TourFragmentPlaceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<TourFragmentPlaceListItem> items = new ArrayList<TourFragmentPlaceListItem>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tour_place_item, parent, false);
        return new TourFragmentPlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TourFragmentPlaceViewHolder)holder).onBind(items.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                Context context = v.getContext();
                if (pos != RecyclerView.NO_POSITION) {
                    Intent tourPlaceDetail = new Intent(context, TourPlaceDetail.class);
                    tourPlaceDetail.putExtra("id", items.get(pos).getId());
                    context.startActivity(tourPlaceDetail);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(TourFragmentPlaceListItem item) {
        items.add(item);
    }

    public class TourFragmentPlaceViewHolder extends RecyclerView.ViewHolder {
        Context context;
        ImageView tour_placePhoto;
        TextView tour_placeName, tour_intro;

        public TourFragmentPlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            tour_placePhoto = itemView.findViewById(R.id.tour_placePhoto);
            tour_placeName = itemView.findViewById(R.id.tour_placeName);
            tour_intro = itemView.findViewById(R.id.tour_intro);
        }

        public void onBind(TourFragmentPlaceListItem tourFragmentPlaceListItem) {
            Glide.with(context).load(tourFragmentPlaceListItem.getPhoto()).into(tour_placePhoto);
            tour_placeName.setText(tourFragmentPlaceListItem.getPlaceName());
            tour_intro.setText(tourFragmentPlaceListItem.getIntro());
        }
    }
}
