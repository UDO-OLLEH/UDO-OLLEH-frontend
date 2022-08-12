package com.udoolleh;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class MainFragment extends Fragment {
    TextView weather, weatherSub;
    Button recycle;
    LinearLayout weather_layout;
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

        new WeatherAsyncTask(weather).execute();
        new WeatherSubAsyncTask(weatherSub).execute();

        //한국표준시 기준 06~18시 밝은 이미지, 그 외 어두운 이미지
        TimeZone tz;
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("HH");
        tz = TimeZone.getTimeZone("Asia/Seoul");
        df.setTimeZone(tz);
        if (Integer.parseInt(df.format(date)) >= 6 && Integer.parseInt(df.format(date)) <= 18 ) {
            weather_layout.setBackgroundResource(R.drawable.main_weather_day);
        } else {
            weather_layout.setBackgroundResource(R.drawable.main_weather_night);
        }

        recycle = view.findViewById(R.id.recycle);
        recycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WeatherAsyncTask(weather).execute();
                new WeatherSubAsyncTask(weatherSub).execute();

                TimeZone tz;
                Date date = new Date();
                DateFormat df = new SimpleDateFormat("HH");
                tz = TimeZone.getTimeZone("Asia/Seoul");
                df.setTimeZone(tz);
                if (Integer.parseInt(df.format(date)) >= 6 && Integer.parseInt(df.format(date)) <= 18 ) {
                    weather_layout.setBackgroundResource(R.drawable.main_weather_day);
                } else {
                    weather_layout.setBackgroundResource(R.drawable.main_weather_night);
                }
            }
        });

        return view;
    }

    //우도 현재 날씨 크롤링
    class WeatherAsyncTask extends AsyncTask<String, Void, String> {
        TextView textView;

        public WeatherAsyncTask(TextView textView) {
            this.textView = textView;
        }

        @Override
        protected String doInBackground(String... strings) {
            String weather_result = "";
            String text = " °C 우도";

            try {
                Document document = Jsoup.connect(weatherLink).get();
                Elements elements = document.select("strong[class=current ]");
                for (Element element : elements) {
                    weather_result = weather_result + element.text();
                }
                return weather_result.substring(5, 9) + text;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            textView.setText(s);
        }
    }

    //우도 체감 날씨 크롤링
    class WeatherSubAsyncTask extends AsyncTask<String, Void, String> {
        TextView textView;

        public WeatherSubAsyncTask(TextView textView) {
            this.textView = textView;
        }

        @Override
        protected String doInBackground(String... strings) {
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
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            textView.setText(s);
        }
    }
}