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