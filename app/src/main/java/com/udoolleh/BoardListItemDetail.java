package com.udoolleh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BoardListItemDetail extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_board_detail);

        TextView titleDetail = findViewById(R.id.titleDetail);
        TextView contextDetail = findViewById(R.id.contextDetail);
        TextView createAtDetail = findViewById(R.id.createAtDetail);

        Intent intent = getIntent();

        String title = intent.getExtras().getString("title");
        String context = intent.getExtras().getString("context");
        String createAt = intent.getExtras().getString("createAt");

        titleDetail.setText(title);
        contextDetail.setText(context);
        createAtDetail.setText(createAt);
    }
}
