package com.udoolleh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amar.library.ui.StickyScrollView;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class Seongsan extends AppCompatActivity {

    StickyScrollView scrollView;
    Button btnRoute, btnTime, btnPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seongsan);

        scrollView = findViewById(R.id.scrollView);
        btnRoute = findViewById(R.id.btnRoute);
        btnTime = findViewById(R.id.btnTime);
        btnPrice = findViewById(R.id.btnPrice);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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


        //네비게이션 메뉴
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.END);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_appbar_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        switch (item.getItemId()){
            case R.id.drawer:
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_SHORT).show();
                } else if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
        }
        return super.onOptionsItemSelected(item);
    }
}