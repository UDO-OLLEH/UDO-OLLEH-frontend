package com.udoolleh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ADImageSliderAdapter extends RecyclerView.Adapter<ADImageSliderAdapter.ADMyViewHolder> {
    private Context ad_context;
    private String[] ad_sliderImage;

    public ADImageSliderAdapter(Context context, String[] sliderImage) {
        this.ad_context = context;
        this.ad_sliderImage = sliderImage;
    }

    @NonNull
    @Override
    public ADImageSliderAdapter.ADMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_ad_slider_item, parent, false);
        return new ADMyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ADImageSliderAdapter.ADMyViewHolder holder, int position) {
        holder.bindSliderImage(ad_sliderImage[position]);
    }

    @Override
    public int getItemCount() {
        return ad_sliderImage.length;
    }

    public class ADMyViewHolder extends RecyclerView.ViewHolder {

        private ImageView ad_mImageView;

        public ADMyViewHolder(@NonNull View itemView) {
            super(itemView);
            ad_mImageView = itemView.findViewById(R.id.ad_slider_image);
        }

        public void bindSliderImage(String imageURL) {
            Glide.with(ad_context).load(imageURL).into(ad_mImageView);
        }
    }
}
