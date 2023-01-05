package com.udoolleh.view.tour.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.udoolleh.R;

public class TourCourseDetail extends AppCompatActivity {
    Context context;
    Toolbar tour_course_toolbar;
    TextView courseName, course, courseName2, course_latitude, course_longitude, detail_type_title_context, detail_type_text_context;
    ImageView detail_type_photo_context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tour_course_detail);
        context = getApplicationContext();

        //툴바 설정
        tour_course_toolbar = (Toolbar)findViewById(R.id.tour_course_toolbar);
        setSupportActionBar(tour_course_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout)findViewById(R.id.tour_course_toolbar_layout);
        toolBarLayout.setTitle("");
        toolBarLayout.setCollapsedTitleTextColor(Color.alpha(0));
        toolBarLayout.setExpandedTitleColor(Color.alpha(0));

        courseName = findViewById(R.id.courseName);
        course = findViewById(R.id.course);
        courseName2 = findViewById(R.id.courseName2);
        course_latitude = findViewById(R.id.course_latitude);
        course_longitude = findViewById(R.id.course_longitude);
        detail_type_title_context = findViewById(R.id.detail_type_title_context);
        detail_type_photo_context = findViewById(R.id.detail_type_photo_context);
        detail_type_text_context = findViewById(R.id.detail_type_text_context);

        Intent intent = getIntent();

        courseName.setText(intent.getExtras().getString("courseName"));
        //courseName2.setText(intent.getExtras().getString("courseName"));
        course.setText(intent.getExtras().getString("course"));
        course_latitude.setText(intent.getExtras().getString("latitude"));
        course_longitude.setText(intent.getExtras().getString("longitude"));
        detail_type_title_context.setText(intent.getExtras().getString("title"));
        Glide.with(context).load(intent.getExtras().getString("photo")).into(detail_type_photo_context);
        detail_type_text_context.setText(intent.getExtras().getString("text"));
    }

    //드로어 메뉴 선택
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        switch (item.getItemId()) {
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
