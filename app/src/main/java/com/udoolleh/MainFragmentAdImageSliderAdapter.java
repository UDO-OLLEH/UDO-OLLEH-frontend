package com.udoolleh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainFragmentAdImageSliderAdapter extends RecyclerView.Adapter<MainFragmentAdImageSliderAdapter.AdMyViewHolder> {
    private ArrayList<MainFragmentAdImageSliderItem> ad_sliderImage = new ArrayList<>();

    @NonNull
    @Override
    public MainFragmentAdImageSliderAdapter.AdMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main_ad_slider_item, parent, false);
        return new AdMyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainFragmentAdImageSliderAdapter.AdMyViewHolder holder, int position) {
        holder.bindSliderImage(ad_sliderImage.get(position));
    }

    @Override
    public int getItemCount() {
        return ad_sliderImage.size();
    }

    public void addImage(MainFragmentAdImageSliderItem item) {
        ad_sliderImage.add(item);
    }

    public class AdMyViewHolder extends RecyclerView.ViewHolder {
        Context context;
        private ImageView ad_slider_image;

        public AdMyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            ad_slider_image = itemView.findViewById(R.id.ad_slider_image);
        }

        public void bindSliderImage(MainFragmentAdImageSliderItem imageURL) {
            Glide.with(context).load(imageURL.getPhoto()).into(ad_slider_image);
        }
    }
}
