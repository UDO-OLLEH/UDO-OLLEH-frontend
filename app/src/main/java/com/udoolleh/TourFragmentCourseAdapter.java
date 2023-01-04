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

public class TourFragmentCourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<TourFragmentCourseListItem> items = new ArrayList<TourFragmentCourseListItem>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tour_course_item, parent, false);
        return new TourFragmentCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TourFragmentCourseViewHolder)holder).onBind(items.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                Context context = v.getContext();
                if (pos != RecyclerView.NO_POSITION) {
                    Intent tourCourseDetail = new Intent(context, TourCourseDetail.class);
                    tourCourseDetail.putExtra("courseName", items.get(pos).getCourseName());
                    tourCourseDetail.putExtra("course", items.get(pos).getCourse());
                    tourCourseDetail.putExtra("title", items.get(pos).getDetail_type_title_context());
                    tourCourseDetail.putExtra("photo", items.get(pos).getDetail_type_photo_context());
                    tourCourseDetail.putExtra("text", items.get(pos).getDetail_type_text_context());
                    tourCourseDetail.putExtra("latitude", items.get(pos).getGps_latitude());
                    tourCourseDetail.putExtra("longitude", items.get(pos).getGps_longitude());
                    context.startActivity(tourCourseDetail);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(TourFragmentCourseListItem item) {
        items.add(item);
    }

    public class TourFragmentCourseViewHolder extends RecyclerView.ViewHolder {
        Context context;
        ImageView tour_coursePhoto;
        TextView tour_courseName, tour_course;

        public TourFragmentCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            tour_coursePhoto = itemView.findViewById(R.id.tour_coursePhoto);
            tour_courseName = itemView.findViewById(R.id.tour_courseName);
            tour_course = itemView.findViewById(R.id.tour_course);
        }

        public void onBind(TourFragmentCourseListItem tourFragmentCourseListItem) {
            Glide.with(context).load(tourFragmentCourseListItem.getDetail_type_photo_context()).into(tour_coursePhoto);
            tour_courseName.setText(tourFragmentCourseListItem.getCourseName());
            tour_course.setText(tourFragmentCourseListItem.getCourse());
        }
    }
}
