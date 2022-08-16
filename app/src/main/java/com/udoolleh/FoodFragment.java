package com.udoolleh;

import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FoodFragment extends Fragment {
    private ViewPager2 viewpager_slider;
    private LinearLayout layout_indicator;
    Context context;

    private String[] images = new String[]{
            "https://cdn.pixabay.com/photo/2019/12/26/10/44/horse-4720178_1280.jpg",
            "https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
            "https://cdn.pixabay.com/photo/2020/03/08/21/41/landscape-4913841_1280.jpg",
            "https://cdn.pixabay.com/photo/2020/09/02/18/03/girl-5539094_1280.jpg"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);
        context = container.getContext();

        viewpager_slider = view.findViewById(R.id.viewpager_slider);
        layout_indicator = view.findViewById(R.id.layout_indicators);

        //ViewPager
        viewpager_slider.setOffscreenPageLimit(1);
        viewpager_slider.setAdapter(new ImageSliderAdapter(context, images));
        viewpager_slider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
        setupIndicators(images.length);

        return view;
    }

    //인디케이터 설정
    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(context);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.layout_indicator_inactive));
            indicators[i].setLayoutParams(params);
            layout_indicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    //현재 인디케이터 설정
    private void setCurrentIndicator(int position) {
        int childCount = layout_indicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layout_indicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.layout_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.layout_indicator_inactive));
            }
        }
    }

}
