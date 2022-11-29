package com.udoolleh;

import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainFragment extends Fragment {
    TextView weather, weatherSub;
    Button recycle;
    ImageView weather_layout;
    String weatherLink = "https://weather.naver.com/today/14110330";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        weather = view.findViewById(R.id.weather);
        weatherSub = view.findViewById(R.id.weatherSub);
        weather_layout = view.findViewById(R.id.weather_layout);

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
        } else if (Integer.parseInt(df.format(date)) >= 5 && Integer.parseInt(df.format(date)) <= 6){
            weather_layout.setImageResource(R.drawable.main_weather_sunset);
        } else if (Integer.parseInt(df.format(date)) >= 17 && Integer.parseInt(df.format(date)) <= 18 )
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
                } else if (Integer.parseInt(df.format(date)) >= 5 && Integer.parseInt(df.format(date)) <= 6){
                    weather_layout.setImageResource(R.drawable.main_weather_sunset);
                } else if (Integer.parseInt(df.format(date)) >= 17 && Integer.parseInt(df.format(date)) <= 18 )
                    weather_layout.setImageResource(R.drawable.main_weather_sunset);
                else {
                    weather_layout.setImageResource(R.drawable.main_weather_night);
                }
            }
        });

        return view;
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