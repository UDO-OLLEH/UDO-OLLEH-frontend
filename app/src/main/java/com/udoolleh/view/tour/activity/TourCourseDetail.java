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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.udoolleh.R;

public class TourCourseDetail extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    Context context;
    Toolbar tour_course_toolbar;
    TextView courseName, course, courseName2, detail_type_title_context, detail_type_text_context;
    ImageView detail_type_photo_context;
    double latitude, longitude;
    String _courseName, _course;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tour_course_detail);
        context = getApplicationContext();

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.tourCourseMapView);
        mapFragment.getMapAsync(this);

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
        detail_type_title_context = findViewById(R.id.detail_type_title_context);
        detail_type_photo_context = findViewById(R.id.detail_type_photo_context);
        detail_type_text_context = findViewById(R.id.detail_type_text_context);

        Intent intent = getIntent();

        courseName.setText(intent.getExtras().getString("courseName"));
        //courseName2.setText(intent.getExtras().getString("courseName"));
        course.setText(intent.getExtras().getString("course"));
        detail_type_title_context.setText(intent.getExtras().getString("title"));
        Glide.with(context).load(intent.getExtras().getString("photo")).into(detail_type_photo_context);
        detail_type_text_context.setText(intent.getExtras().getString("text"));

        latitude = intent.getExtras().getDouble("latitude");
        longitude = intent.getExtras().getDouble("longitude");
        _courseName = intent.getExtras().getString("courseName");
        _course = intent.getExtras().getString("course");
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        LatLng PLACE = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(PLACE);
        markerOptions.title(_courseName);
        markerOptions.snippet(_course);

        map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(PLACE, 15));
    }
}
