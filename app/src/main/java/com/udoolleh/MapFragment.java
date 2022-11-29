package com.udoolleh;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class MapFragment extends Fragment {
    Button btnBus, btnBoat;
    ImageView ivRedbus, ivWhitebus, ivJongdal, ivSeongsan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //프래그먼트에서 뷰 보내기위함
        Context context = view.getContext();

        btnBus = view.findViewById(R.id.btnBus);
        btnBoat = view.findViewById(R.id.btnBoat);
        ivRedbus = view.findViewById(R.id.ivRedbus);
        ivWhitebus = view.findViewById(R.id.ivWhitebus);
        ivJongdal = view.findViewById(R.id.ivJongdal);
        ivSeongsan = view.findViewById(R.id.ivSeongsan);

        btnBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnBoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ivRedbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Redbus.class);
                startActivity(intent);
            }
        });

        ivWhitebus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Whitebus.class);
                startActivity(intent);
            }
        });

        ivJongdal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Jongdal.class);
                startActivity(intent);
            }
        });

        ivSeongsan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Seongsan.class);
                startActivity(intent);
            }
        });

        return view;
    }
}