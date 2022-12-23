package com.udoolleh;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodFragment extends Fragment {
    Context context;
    private ViewPager2 viewpager_slider;
    private LinearLayout layout_indicator;
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    RecyclerView foodGridView;
    FoodListAdapter foodListAdapter;
    TextView noneFoodText;
    int itemPage = 0;
    private boolean isLoading = false;
    private boolean isLastLoading = false;

    //상단에 보여질 이미지 URL
    private String[] images = new String[]{
            "https://udo-photo-bucket.s3.ap-northeast-2.amazonaws.com/restaurant/b1dab7de-d124-4ce9-8c4a-1e192564f801%ED%95%B4%EB%85%80%EC%B4%8C%ED%95%B4%EC%82%B0%EB%AC%BC.png",
            "https://udo-photo-bucket.s3.ap-northeast-2.amazonaws.com/restaurant/a6cd7f6a-86f2-4771-a46a-125040da3327%ED%95%B4%EB%85%80%EC%B4%8C%ED%95%B4%EC%82%B0%EB%AC%BC2.png"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);
        context = container.getContext();

        //ViewPager
        viewpager_slider = view.findViewById(R.id.food_viewpager_slider);
        layout_indicator = view.findViewById(R.id.layout_indicators);

        viewpager_slider.setOffscreenPageLimit(1);
        viewpager_slider.setAdapter(new FoodImageSliderAdapter(context, images));
        viewpager_slider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
        setupIndicators(images.length);

        //RecyclerView
        foodGridView = view.findViewById(R.id.foodGridView);
        noneFoodText = view.findViewById(R.id.noneFoodText);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        foodGridView.setLayoutManager(gridLayoutManager);
        foodListAdapter = new FoodListAdapter();

        //Retrofit
        FoodResponse();

        //맛집 조회 추가 로딩
        foodGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int lastItem = layoutManager.findLastVisibleItemPosition();
                Log.d("lastitem", lastItem + "");

                if(!isLoading & !isLastLoading){
                    if(layoutManager != null && lastItem == foodListAdapter.getItemCount() - 1) {
                        isLoading = true;
                        FoodBackgroundTask();
                    }
                }
            }
        });

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

    //맛집 리스트 조회
    public void FoodResponse() {
        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(null);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //FoodResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getFoodSesponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getFoodResponse(itemPage).enqueue(new Callback<FoodResponse>() {
            @Override
            public void onResponse(Call<FoodResponse> call, Response<FoodResponse> response) {
                Log.d("udoLog", "맛집 조회 body 내용 = " + response.body());
                Log.d("udoLog", "맛집 조회 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "맛집 조회 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    FoodResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //맛집 조회 성공
                    int success = 200;

                    if (resultCode == success) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        String message = result.getMessage();
                        List<FoodResponse.FoodList.Content> foodList = result.getList().getContent();

                        //맛집 리스트 조회 로그
                        Log.d("udoLog", "맛집 조회 = \n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "message: " + message + "\n" +
                                "content" + foodList
                        );

                        if(foodList.toString() == "[]") {
                            foodGridView.setVisibility(View.INVISIBLE);
                            noneFoodText.setVisibility(View.VISIBLE);
                        } else {
                            foodGridView.setVisibility(View.VISIBLE);
                            noneFoodText.setVisibility(View.INVISIBLE);

                            //Recycler View 레이아웃 설정
                            for (FoodResponse.FoodList.Content food : foodList) {

                                //맛집 리스트 상세 조회 로그
                                Log.d("udoLog", "맛집 조회 리스트 = \n" +
                                        "id: " + food.getId() + "\n" +
                                        "name: " + food.getName() + "\n" +
                                        "placeType: " + food.getPlaceType() + "\n" +
                                        "category: " + food.getCategory() + "\n" +
                                        "address: " + food.getAddress() + "\n" +
                                        "imagesUrl: " + food.getImagesUrl() + "\n" +
                                        "totalGrade: " + food.getTotalGrade() + "\n" +
                                        "xcoordinate: " + food.getXcoordinate() + "\n" +
                                        "ycoordinate: " + food.getYcoordinate() + "\n"
                                );
                                foodListAdapter.addItem(new FoodListItem(food.getId(), food.getName(), food.getPlaceType(), food.getCategory(), food.getAddress(), food.getImagesUrl().toString(), food.getTotalGrade().toString(), food.getXcoordinate(), food.getYcoordinate()));
                            }
                            foodGridView.setAdapter(foodListAdapter);
                        }

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
            public void onFailure(Call<FoodResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    //맛집 조회 추가 로딩
    public void FoodLoadMoreResponse() {
        foodListAdapter.removeItem(foodListAdapter.getItemCount() - 1);
        int scrollPosition = foodListAdapter.getItemCount();
        foodListAdapter.notifyItemRemoved(scrollPosition);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(null);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //FoodResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getFoodSesponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getFoodResponse(itemPage).enqueue(new Callback<FoodResponse>() {
            @Override
            public void onResponse(Call<FoodResponse> call, Response<FoodResponse> response) {
                Log.d("udoLog", "맛집 조회 추가 로딩 body 내용 = " + response.body());
                Log.d("udoLog", "맛집 조회 추가 로딩 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "맛집 조회 추가 로딩 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    FoodResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //맛집 조회 성공
                    int success = 200;

                    if (resultCode == success) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        String message = result.getMessage();
                        List<FoodResponse.FoodList.Content> foodList = result.getList().getContent();

                        //맛집 리스트 조회 로그
                        Log.d("udoLog", "맛집 조회 추가 로딩 = \n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "message: " + message + "\n" +
                                "content" + foodList
                        );

                        //Recycler View 레이아웃 설정
                        for (FoodResponse.FoodList.Content food : foodList) {

                            //맛집 리스트 상세 조회 로그
                            Log.d("udoolleh", "맛집 조회 추가 로딩 리스트 = \n" +
                                    "id: " + food.getId() + "\n" +
                                    "name: " + food.getName() + "\n" +
                                    "placeType: " + food.getPlaceType() + "\n" +
                                    "category: " + food.getCategory() + "\n" +
                                    "address: " + food.getAddress() + "\n" +
                                    "imagesUrl: " + food.getImagesUrl() + "\n" +
                                    "totalGrade: " + food.getTotalGrade() + "\n" +
                                    "xcoordinate: " + food.getXcoordinate() + "\n" +
                                    "ycoordinate: " + food.getYcoordinate() + "\n"
                            );
                            foodListAdapter.addItem(new FoodListItem(food.getId(), food.getName(), food.getPlaceType(), food.getCategory(), food.getAddress(), food.getImagesUrl().toString(), food.getTotalGrade().toString(), food.getXcoordinate(), food.getYcoordinate()));
                        }
                        foodListAdapter.notifyDataSetChanged();
                        isLoading = false;

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
            public void onFailure(Call<FoodResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    private void FoodBackgroundTask() {
        foodListAdapter.addItem(null);
        foodListAdapter.notifyItemInserted(foodListAdapter.getItemCount() - 1);
        itemPage++;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FoodLoadMoreResponse();
            }
        }, 1000);
    }
}
