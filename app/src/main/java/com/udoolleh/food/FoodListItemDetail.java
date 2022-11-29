package com.udoolleh.food;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.udoolleh.R;

public class FoodListItemDetail extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_food_detail);

        TextView imagesUrlDetail = findViewById(R.id.imagesUrlDetail);
        TextView nameDetail = findViewById(R.id.nameDetail);
        TextView addressDetail = findViewById(R.id.addressDetail);
        TextView totalGradeDetail = findViewById(R.id.totalGradeDetail);

        Intent intent = getIntent();

        String imagesUrl = intent.getExtras().getString("imagesUrl");
        String name = intent.getExtras().getString("name");
        String address = intent.getExtras().getString("address");
        String totalGrade = intent.getExtras().getString("totalGrade");

        imagesUrlDetail.setText(imagesUrl);
        nameDetail.setText(name);
        addressDetail.setText(address);
        totalGradeDetail.setText(totalGrade);
    }
}
