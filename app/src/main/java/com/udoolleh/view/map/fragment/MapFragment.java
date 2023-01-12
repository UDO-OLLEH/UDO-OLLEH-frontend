package com.udoolleh.view.map.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.udoolleh.R;
import com.udoolleh.view.map.activity.MapFragmentJongdal;
import com.udoolleh.view.map.activity.MapFragmentRedbus;
import com.udoolleh.view.map.activity.MapFragmentSeongsan;
import com.udoolleh.view.map.activity.MapFragmentWhitebus;

public class MapFragment extends Fragment {
    Button btnBus, btnBoat;
    ImageView ivRedbus, ivWhitebus, ivJongdal, ivSeongsan;
    CardView cvRedbus, cvWhitebus, cvJongboat, cvSeongboat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        btnBus = view.findViewById(R.id.btnBus);
        btnBoat = view.findViewById(R.id.btnBoat);
        ivRedbus = view.findViewById(R.id.ivRedbus);
        ivWhitebus = view.findViewById(R.id.ivWhitebus);
        ivJongdal = view.findViewById(R.id.ivJongdal);
        ivSeongsan = view.findViewById(R.id.ivSeongsan);

        cvRedbus = view.findViewById(R.id.cvRedbus);
        cvWhitebus = view.findViewById(R.id.cvWhitebus);
        cvJongboat = view.findViewById(R.id.cvJongboat);
        cvSeongboat = view.findViewById(R.id.cvSeongboat);

        btnBus.setSelected(true);

        btnBus.setOnClickListener(this::onClick);
        btnBoat.setOnClickListener(this::onClick);
        ivRedbus.setOnClickListener(this::onClick);
        ivWhitebus.setOnClickListener(this::onClick);
        ivJongdal.setOnClickListener(this::onClick);
        ivSeongsan.setOnClickListener(this::onClick);

        return view;
    }

    private void onClick(View view){

        //프래그먼트에서 뷰 보내기위함
        Context context = view.getContext();

        switch (view.getId()){
            case R.id.btnBus:
                btnBoat.setSelected(false);
                btnBus.setSelected(true);

                cvRedbus.setVisibility(View.VISIBLE);
                cvWhitebus.setVisibility(View.VISIBLE);

                cvJongboat.setVisibility(View.GONE);
                cvSeongboat.setVisibility(View.GONE);

                break;

            case R.id.btnBoat:
                btnBus.setSelected(false);
                btnBoat .setSelected(true);

                cvRedbus.setVisibility(View.GONE);
                cvWhitebus.setVisibility(View.GONE);

                cvJongboat.setVisibility(View.VISIBLE);
                cvSeongboat.setVisibility(View.VISIBLE);
                break;

            case R.id.ivRedbus:
                Intent intent = new Intent(context, MapFragmentRedbus.class);
                startActivity(intent);
                break;

            case R.id.ivWhitebus:
                Intent intent1 = new Intent(context, MapFragmentWhitebus.class);
                startActivity(intent1);
                break;

            case R.id.ivJongdal:
                Intent intent2 = new Intent(context, MapFragmentJongdal.class);
                startActivity(intent2);
                break;

            case R.id.ivSeongsan:
                Intent intent3 = new Intent(context, MapFragmentSeongsan.class);
                startActivity(intent3);
                break;

        }
    }
}