package com.udoolleh.view.map.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.udoolleh.R;
import com.udoolleh.retrofit.RetrofitClient;
import com.udoolleh.retrofit.RetrofitInterface;
import com.udoolleh.view.food.DTO.FoodResponse;
import com.udoolleh.view.food.adapter.FoodListAdapter;
import com.udoolleh.view.food.item.FoodListItem;
import com.udoolleh.view.map.DTO.MapFragmentResponse;
import com.udoolleh.view.map.activity.MapFragmentJongdal;
import com.udoolleh.view.map.activity.MapFragmentRedbus;
import com.udoolleh.view.map.activity.MapFragmentSeongsan;
import com.udoolleh.view.map.activity.MapFragmentWhitebus;
import com.udoolleh.view.map.adapter.MapListAdapter;
import com.udoolleh.view.map.item.MapListItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment {
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    Context context;
    Button btnBus, btnBoat;
    ImageView ivRedbus, ivWhitebus;
    CardView cvRedbus, cvWhitebus;
    RecyclerView mapGridView;
    TextView noneMapText;
    MapListAdapter mapListAdapter;
    FrameLayout harborFrame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        context = container.getContext();

        btnBus = view.findViewById(R.id.btnBus);
        btnBoat = view.findViewById(R.id.btnBoat);
        ivRedbus = view.findViewById(R.id.ivRedbus);
        ivWhitebus = view.findViewById(R.id.ivWhitebus);

        cvRedbus = view.findViewById(R.id.cvRedbus);
        cvWhitebus = view.findViewById(R.id.cvWhitebus);

        mapGridView = view.findViewById(R.id.mapGridView);
        harborFrame = view.findViewById(R.id.harborFrame);

        btnBoat.setSelected(true);

        btnBus.setOnClickListener(this::onClick);
        btnBoat.setOnClickListener(this::onClick);
        ivRedbus.setOnClickListener(this::onClick);
        ivWhitebus.setOnClickListener(this::onClick);

        //RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mapGridView.setLayoutManager(linearLayoutManager);
        mapListAdapter = new MapListAdapter();

        MapResponse();

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

                harborFrame.setVisibility(View.GONE);
                mapGridView.setVisibility(View.GONE);

                break;

            case R.id.btnBoat:
                btnBus.setSelected(false);
                btnBoat .setSelected(true);

                cvRedbus.setVisibility(View.GONE);
                cvWhitebus.setVisibility(View.GONE);

                harborFrame.setVisibility(View.VISIBLE);
                mapGridView.setVisibility(View.VISIBLE);

                break;

            case R.id.ivRedbus:
                Intent intent = new Intent(context, MapFragmentRedbus.class);
                startActivity(intent);
                break;

            case R.id.ivWhitebus:
                Intent intent1 = new Intent(context, MapFragmentWhitebus.class);
                startActivity(intent1);
                break;
        }
    }

    //항구 리스트 조회
    public void MapResponse() {
        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(null);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        retrofitInterface.getMapFragmentResponse().enqueue(new Callback<MapFragmentResponse>() {
            @Override
            public void onResponse(Call<MapFragmentResponse> call, Response<MapFragmentResponse> response) {
                Log.d("udoLog", "맛집 조회 body 내용 = " + response.body());
                Log.d("udoLog", "맛집 조회 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "맛집 조회 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    MapFragmentResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //맛집 조회 성공
                    int success = 200;

                    if (resultCode == success) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        String message = result.getMessage();
                        List<MapFragmentResponse.harborList> harborList = result.getList();

                        //맛집 리스트 조회 로그
                        Log.d("udoLog", "항구 조회 = \n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "message: " + message + "\n" +
                                "content" + harborList
                        );

                        //Recycler View 레이아웃 설정
                        for (MapFragmentResponse.harborList harbor : harborList) {

                            //맛집 리스트 상세 조회 로그
                            Log.d("udoLog", "항구 조회 리스트 = \n" +
                                    "id: " + harbor.getId() + "\n" +
                                    "name: " + harbor.getName() + "\n"
                            );
                            mapListAdapter.addItem(new MapListItem(harbor.getId(), harbor.getName()));
                        }
                        mapGridView.setAdapter(mapListAdapter);

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("알림")
                                .setMessage("맛집을 조회할 수 없습니다.\n 다시 시도해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<MapFragmentResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }
}