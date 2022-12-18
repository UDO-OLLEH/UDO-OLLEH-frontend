package com.udoolleh;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.amar.library.ui.StickyScrollView;

public class Jongdal extends AppCompatActivity {

    StickyScrollView scrollView;
    Button btnRoute, btnTime, btnPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jongdal);

        scrollView = findViewById(R.id.scrollView);
        btnRoute = findViewById(R.id.btnRoute);
        btnTime = findViewById(R.id.btnTime);
        btnPrice = findViewById(R.id.btnPrice);

        btnRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.fullScroll(scrollView.FOCUS_UP);
            }
        });
        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.fullScroll(scrollView.FOCUS_DOWN);
            }
        });
    }
}