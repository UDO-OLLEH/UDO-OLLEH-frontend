package com.udoolleh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {
    Context context;
    TextView weather, weatherSub;
    Button recycle;
    ImageView weather_layout;
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    String weatherLink = "https://weather.naver.com/today/14110330";
    private ViewPager2 ad_viewpager_slider;
    MainFragmentAdImageSliderAdapter adImageSliderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        context = container.getContext();

        weather = view.findViewById(R.id.weather);
        weatherSub = view.findViewById(R.id.weatherSub);
        weather_layout = view.findViewById(R.id.weather_layout);

        //ViewPager
        ad_viewpager_slider = view.findViewById(R.id.ad_viewpager_slider);
        ad_viewpager_slider.setOffscreenPageLimit(1);
        ad_viewpager_slider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
        adImageSliderAdapter = new MainFragmentAdImageSliderAdapter();
        AdResponse();

        WeatherBackgroundTask(weatherLink);
        WeatherSubBackgroundTask(weatherLink);

        //한국표준시 기준 07~17시 Day , 05~07시 Sunset, 17~19시 Sunset, 19~05시 Night 기준
        TimeZone tz;
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("HH");
        tz = TimeZone.getTimeZone("Asia/Seoul");
        df.setTimeZone(tz);
        if (Integer.parseInt(df.format(date)) >= 7 && Integer.parseInt(df.format(date)) <= 16) {
            weather_layout.setImageResource(R.drawable.main_weather_day);
        } else if (Integer.parseInt(df.format(date)) >= 5 && Integer.parseInt(df.format(date)) <= 6) {
            weather_layout.setImageResource(R.drawable.main_weather_sunset);
        } else if (Integer.parseInt(df.format(date)) >= 17 && Integer.parseInt(df.format(date)) <= 18)
            weather_layout.setImageResource(R.drawable.main_weather_sunset);
        else {
            weather_layout.setImageResource(R.drawable.main_weather_night);
        }

        recycle = view.findViewById(R.id.recycle);
        recycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeatherBackgroundTask(weatherLink);
                WeatherSubBackgroundTask(weatherLink);

                TimeZone tz;
                Date date = new Date();
                @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("HH");
                tz = TimeZone.getTimeZone("Asia/Seoul");
                df.setTimeZone(tz);
                if (Integer.parseInt(df.format(date)) >= 7 && Integer.parseInt(df.format(date)) <= 16) {
                    weather_layout.setImageResource(R.drawable.main_weather_day);
                } else if (Integer.parseInt(df.format(date)) >= 5 && Integer.parseInt(df.format(date)) <= 6) {
                    weather_layout.setImageResource(R.drawable.main_weather_sunset);
                } else if (Integer.parseInt(df.format(date)) >= 17 && Integer.parseInt(df.format(date)) <= 18)
                    weather_layout.setImageResource(R.drawable.main_weather_sunset);
                else {
                    weather_layout.setImageResource(R.drawable.main_weather_night);
                }
            }
        });

        return view;
    }

    //광고 조회
    public void AdResponse() {
        //retrofitclient 에서 instance 받아옴 광고에는 토큰 필요 없음 null 입력
        //token is none 부분 봐라
        //interface랑 clinet 연결
        retrofitClient = RetrofitClient.getInstance(null);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //interface 위에서 연결 후 불러옴
        retrofitInterface.getADResponse().enqueue(new Callback<MainFragmentAdResponse>() {
            //통신 성공
            @Override
            //통신 성고하면 response 저장
            public void onResponse(Call<MainFragmentAdResponse> call, Response<MainFragmentAdResponse> response) {
                Log.d("udoLog", "광고 정보 조회 body 내용 = " + response.body());
                Log.d("udoLog", "광고 정보 조회 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "광고 정보 조회 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body() 를 reuslt 에 저장
                    MainFragmentAdResponse result = response.body();

                    //받은 코드 저장 (200 얘기하는거임)
                    int resultCode = response.code();

                    //광고 조회 성공
                    int success = 200;

                    if (resultCode == success) {
                        String id = result.getId();
                        String dateTime = result.getDateTime();
                        String message = result.getMessage();
                        List<MainFragmentAdResponse.AdList> imglist = result.getList();

                        //광고 정보 조회 로그
                        Log.d("udoLog", "광고 정보 조회 = \n" +
                                "Id: " + id + "\n" +
                                "dateTime: " + dateTime + "\n" +
                                "message: " + message + "\n" +
                                "imglist" + imglist
                        );

                        //

                        for(MainFragmentAdResponse.AdList adList : imglist){

                            //광고 정보 상세 조회 로그
                            Log.d("udoLog", "광고 정보 조회 = \n" +
                                    "id: " + adList.getId() + "\n" +
                                    "photo: " + adList.getPhoto() + "\n"
                            );
                            adImageSliderAdapter.addImage(new MainFragmentAdImageSliderItem(adList.getPhoto()));
                            //str.add(adList.getPhoto());
                        }
                        ad_viewpager_slider.setAdapter(adImageSliderAdapter);
                    }

                }

            }

            //통신 실패
            @Override
            public void onFailure(Call<MainFragmentAdResponse> call, Throwable t) {

            }
        });
    }

    //우도 현재 날씨 크롤링
    Disposable weatherBackgroundTask;

    void WeatherBackgroundTask(String URLs) {
        //onPreExecute

        weatherBackgroundTask = Observable.fromCallable(() -> {
            //doInBackground
            String result;
            String weather_result = "";
            String text = " °C 우도";

            try {
                Document document = Jsoup.connect(URLs).get();
                Elements elements = document.select("strong[class=current]");
                for (Element element : elements) {
                    weather_result = weather_result + element.text();
                }
                result = weather_result.substring(5, 9) + text;
                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((result) -> {
            //onPostExecute
            weather.setText(result);
            weatherBackgroundTask.dispose();
        }, throwable -> System.out.println("Error"));
    }

    //우도 대기 상태 크롤링
    Disposable weatherSubBackgroundTask;

    void WeatherSubBackgroundTask(String URLs) {
        //onPreExecute

        weatherSubBackgroundTask = Observable.fromCallable(() -> {
            //doInBackground
            String weatherSub_result = "";

            try {
                Document document = Jsoup.connect(weatherLink).get();
                Elements elements = document.select("span[class=weather]");
                for (Element element : elements) {
                    weatherSub_result = weatherSub_result + element.text();
                }
                return weatherSub_result;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((weatherSub_result) -> {
            //onPostExecute
            weatherSub.setText(weatherSub_result);
            weatherSubBackgroundTask.dispose();
        }, throwable -> System.out.println("Error"));
    }

}