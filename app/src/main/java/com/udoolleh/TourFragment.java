package com.udoolleh;

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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourFragment extends Fragment {
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    Context context;
    RecyclerView tourCourseRecyclerView;
    TextView noneTourText;
    TourFragmentCourseAdapter tourFragmentCourseAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tour, container, false);
        context = container.getContext();

        //RecyclerView
        tourCourseRecyclerView = view.findViewById(R.id.tourCourseRecyclerView);
        noneTourText = view.findViewById(R.id.noneTourText);
        LinearLayoutManager courseLinearLayoutManager = new LinearLayoutManager(context);
        tourCourseRecyclerView.setLayoutManager(courseLinearLayoutManager);
        tourFragmentCourseAdapter = new TourFragmentCourseAdapter();

        //Retrofit
        TourCourseResponse();

        return view;
    }

    public void TourCourseResponse() {
        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(null);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //TourCourseResponse에 저장된 데이터와 함께 RetrofitInterface에서 정의한 getCourseSesponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getFragmentTourCourseResponse().enqueue(new Callback<TourFragmentCourseResponse>() {
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
                        Log.d("udoLog", "맛집 조회 = \n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "message: " + message + "\n" +
                                "content" + courseList
                        );

                        if(courseList.toString() == "[]") {
                            tourCourseRecyclerView.setVisibility(View.INVISIBLE);
                            noneTourText.setVisibility(View.VISIBLE);
                        } else {
                            tourCourseRecyclerView.setVisibility(View.VISIBLE);
                            noneTourText.setVisibility(View.INVISIBLE);

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
                                .setMessage("맛집을 조회할 수 없습니다.\n 다시 시도해주세요.")
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