package com.udoolleh;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainFragment extends Fragment {
    TextView weather, weatherSub;
    Button recycle;
    String weatherLink = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=%EC%9A%B0%EB%8F%84+%EB%82%A0%EC%94%A8";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        weather = view.findViewById(R.id.weather);
        weatherSub = view.findViewById(R.id.weatherSub);

        new WeatherAsyncTask(weather).execute();
        new WeatherSubAsyncTask(weatherSub).execute();

        recycle = view.findViewById(R.id.recycle);
        recycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WeatherAsyncTask(weather).execute();
                new WeatherSubAsyncTask(weatherSub).execute();
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
                Elements elements = document.select("div[class=_today]");
                for (Element element : elements) {
                    weather_result = weather_result + element.text();
                }
                return weather_result.substring(8, 12) + text;

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
            String text1 = "채감온도 ";
            String text2 = " °C";

            try {
                Document document = Jsoup.connect(weatherLink).get();
                Elements elements = document.select("div[class=_today]");
                for (Element element : elements) {
                    weatherSub_result = weatherSub_result + element.text();
                }
                return text1 + weatherSub_result.substring(32, 36) + text2;

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