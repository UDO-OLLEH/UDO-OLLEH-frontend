package com.udoolleh;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.amar.library.ui.StickyScrollView;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class Redbus extends AppCompatActivity {

    StickyScrollView scrollView;
    Button btnRoute, btnTime;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redbus);

        scrollView = findViewById(R.id.scrollView);
        btnRoute = findViewById(R.id.btnRoute);
        btnTime = findViewById(R.id.btnTime);
        Toolbar toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        switch (item.getItemId()) {
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;

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