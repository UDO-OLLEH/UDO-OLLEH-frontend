package com.udoolleh.view.tour.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udoolleh.R;
import com.udoolleh.retrofit.RetrofitClient;
import com.udoolleh.retrofit.RetrofitInterface;
import com.udoolleh.view.tour.DTO.TourFragmentCourseResponse;
import com.udoolleh.view.tour.DTO.TourFragmentPlaceResponse;
import com.udoolleh.view.tour.adapter.TourFragmentCourseAdapter;
import com.udoolleh.view.tour.adapter.TourFragmentPlaceAdapter;
import com.udoolleh.view.tour.item.TourFragmentCourseListItem;
import com.udoolleh.view.tour.item.TourFragmentPlaceListItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourFragment extends Fragment {
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    Context context;
    RecyclerView tourCourseRecyclerView, tourPlaceRecyclerView;
    TextView noneTourCourseText, noneTourPlaceText;
    TourFragmentCourseAdapter tourFragmentCourseAdapter;
    TourFragmentPlaceAdapter tourFragmentPlaceAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tour, container, false);
        context = container.getContext();

        //PlaceRecyclerView
        tourPlaceRecyclerView = view.findViewById(R.id.tourPlaceRecyclerView);
        noneTourPlaceText = view.findViewById(R.id.noneTourPlaceText);
        LinearLayoutManager placeLinearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        tourPlaceRecyclerView.setLayoutManager(placeLinearLayoutManager);
        tourFragmentPlaceAdapter = new TourFragmentPlaceAdapter();

        //PlaceRetrofit
        TourPlaceResponse();

        //CourseRecyclerView
        tourCourseRecyclerView = view.findViewById(R.id.tourCourseRecyclerView);
        noneTourCourseText = view.findViewById(R.id.noneTourCourseText);
        LinearLayoutManager courseLinearLayoutManager = new LinearLayoutManager(context);
        tourCourseRecyclerView.setLayoutManager(courseLinearLayoutManager);
        tourFragmentCourseAdapter = new TourFragmentCourseAdapter();

        //CourseRetrofit
        TourCourseResponse();

        return view;
    }

    public void TourPlaceResponse() {
        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(null);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //TourCourseResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getCourseSesponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getTourFragmentPlaceResponse().enqueue(new Callback<TourFragmentPlaceResponse>() {
            @Override
            public void onResponse(Call<TourFragmentPlaceResponse> call, Response<TourFragmentPlaceResponse> response) {
                Log.d("udoLog", "추천 관광지 조회 body 내용 = " + response.body());
                Log.d("udoLog", "추천 관광지 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "추천 관광지 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    TourFragmentPlaceResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //맛집 조회 성공
                    int success = 200;

                    if (resultCode == success) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        String message = result.getMessage();
                        List<TourFragmentPlaceResponse.PlaceList> placeList = result.getPlaceList();

                        //여행지 코스 전체 목록 조회 로그
                        Log.d("udoLog", "추천 관광지 조회 = \n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "message: " + message + "\n" +
                                "content" + placeList
                        );

                        if(placeList.toString() == "[]") {
                            tourPlaceRecyclerView.setVisibility(View.INVISIBLE);
                            noneTourPlaceText.setVisibility(View.VISIBLE);
                        } else {
                            tourPlaceRecyclerView.setVisibility(View.VISIBLE);
                            noneTourPlaceText.setVisibility(View.INVISIBLE);

                            //Recycler View 레이아웃 설정
                            for (TourFragmentPlaceResponse.PlaceList place : placeList) {

                                //맛집 리스트 상세 조회 로그
                                Log.d("udoLog", "추천 관광지 리스트 = \n" +
                                        "id: " + place.getId() + "\n" +
                                        "placeName: " + place.getPlaceName() + "\n" +
                                        "intro: " + place.getIntro() + "\n" +
                                        "context: " + place.getContext() + "\n" +
                                        "photo: " + place.getPhoto() + "\n"
                                );
                                tourFragmentPlaceAdapter.addItem(new TourFragmentPlaceListItem(place.getId(), place.getPlaceName(), place.getIntro(), place.getPhoto()));
                            }
                            tourPlaceRecyclerView.setAdapter(tourFragmentPlaceAdapter);
                        }

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("알림")
                                .setMessage("추천 관광지를 조회할 수 없습니다.\n 다시 시도해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<TourFragmentPlaceResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    public void TourCourseResponse() {
        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(null);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //TourCourseResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getCourseSesponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getTourFragmentCourseResponse().enqueue(new Callback<TourFragmentCourseResponse>() {
            @Override
            public void onResponse(Call<TourFragmentCourseResponse> call, Response<TourFragmentCourseResponse> response) {
                Log.d("udoLog", "여행지 코스 전체 목록 조회 body 내용 = " + response.body());
                Log.d("udoLog", "여행지 코스 전체 목록 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "여행지 코스 전체 목록 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    TourFragmentCourseResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //맛집 조회 성공
                    int success = 200;

                    if (resultCode == success) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        String message = result.getMessage();
                        List<TourFragmentCourseResponse.CourseList> courseList = result.getCourseList();

                        //여행지 코스 전체 목록 조회 로그
                        Log.d("udoLog", "여행지 코스 전테 목록 조회 = \n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "message: " + message + "\n" +
                                "content" + courseList
                        );

                        if(courseList.toString() == "[]") {
                            tourCourseRecyclerView.setVisibility(View.INVISIBLE);
                            noneTourCourseText.setVisibility(View.VISIBLE);
                        } else {
                            tourCourseRecyclerView.setVisibility(View.VISIBLE);
                            noneTourCourseText.setVisibility(View.INVISIBLE);

                            //Recycler View 레이아웃 설정
                            for (TourFragmentCourseResponse.CourseList course : courseList) {

                                //맛집 리스트 상세 조회 로그
                                Log.d("udoLog", "여행지 코스 전체 목록 리스트 = \n" +
                                        "id: " + course.getId() + "\n" +
                                        "courseName: " + course.getCourseName() + "\n" +
                                        "course: " + course.getCourse() + "\n" +
                                        "detail: " + course.getDetail() + "\n" +
                                        "gps: " + course.getGps() + "\n"
                                );
                                tourFragmentCourseAdapter.addItem(new TourFragmentCourseListItem(course.getId(), course.getCourseName(), course.getCourse(), course.getDetail().get(0).getType(), course.getDetail().get(0).getContext(), course.getDetail().get(1).getType(), course.getDetail().get(1).getContext(), course.getDetail().get(2).getType(), course.getDetail().get(2).getContext(), course.getGps().get(0).getLatitude(), course.getGps().get(0).getLongitude()));
                            }
                            tourCourseRecyclerView.setAdapter(tourFragmentCourseAdapter);
                        }

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("알림")
                                .setMessage("여행지 코스 전체 목록을 조회할 수 없습니다.\n 다시 시도해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<TourFragmentCourseResponse> call, Throwable t) {
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