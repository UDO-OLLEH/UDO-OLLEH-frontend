package com.udoolleh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Whitebus extends AppCompatActivity {

    NestedScrollView scrollView;
    Button btnRoute, btnTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whitebus);

        scrollView = findViewById(R.id.scrollView);
        btnRoute = findViewById(R.id.btnRoute);
        btnTime = findViewById(R.id.btnTime);

        btnRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.fullScroll(scrollView.FOCUS_UP);
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.fullScroll(scrollView.FOCUS_DOWN);
            }
        });

    }
}