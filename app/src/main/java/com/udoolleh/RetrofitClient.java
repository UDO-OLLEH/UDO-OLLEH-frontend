package com.udoolleh;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    private static RetrofitInterface RetrofitInterface;
    //서버 BASE 주소
    private static String baseUrl = "http://ec2-54-241-190-224.us-west-1.compute.amazonaws.com/";
    private String accToken;

    //Token is Used
    public RetrofitClient(String accToken) {
        this.accToken = accToken;
        Log.d("Token", accToken);

        //로그를 보기 위한 Interceptor
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);

        //헤더를 보기 위한 Interceptor
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("key", accToken).build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(interceptor)
                .build();

        //retrofit 설정
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    //Token is Unused
    private RetrofitClient() {
        //로그를 보기 위한 Interceptor
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logger)
                .build();

        //retrofit 설정
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RetrofitInterface = retrofit.create(RetrofitInterface.class);
    }
/*
    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }
 */

    public static RetrofitClient getInstance(String accToken) {
        if (accToken == null) {
            instance = new RetrofitClient();
        } else {
            instance = new RetrofitClient(accToken);
        }
        return instance;
    }

    public static RetrofitInterface getRetrofitInterface() {
        return RetrofitInterface;
    }
}