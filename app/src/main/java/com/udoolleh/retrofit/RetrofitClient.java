package com.udoolleh.retrofit;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.udoolleh.retrofit.refreshTokenDTO.RefreshRequest;
import com.udoolleh.retrofit.refreshTokenDTO.RefreshResponse;
import com.udoolleh.view.main.activity.MainActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    Context context = MainActivity.context;
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    private static RetrofitClient instance = null;
    private static RetrofitInterface RetrofitInterface;
    //서버 BASE 주소
    private static String baseUrl = "http://ec2-54-241-190-224.us-west-1.compute.amazonaws.com/";
    private String interceptorAccToken;

    //Token is visible
    public RetrofitClient(String accToken) {

        //로그를 보기 위한 Interceptor
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);

        //헤더를 보기 위한 Interceptor
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                interceptorAccToken = getPreferenceString("accToken");
                Request newRequest = chain.request().newBuilder().addHeader("x-auth-token", accToken).build();
                Response response = chain.proceed(newRequest);

                if(response.code() == 401) {
                    RefreshResponse();
                    interceptorAccToken = getPreferenceString("accToken");
                    Request refRequest = chain.request().newBuilder().addHeader("x-auth-token", interceptorAccToken).build();
                    response = chain.proceed(refRequest);
                }

                return response;
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

    //Token is none
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

    //토큰 갱신
    public void RefreshResponse() {

        //토큰 가져오기
        String refToken = getPreferenceString("refToken");

        //refreshRequest refToken 저장
        RefreshRequest refreshRequest = new RefreshRequest(refToken);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(null);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //refreshRequest에 저장된 데이터와 함께 init에서 정의한 getRefreshResponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getRefreshResponse(refreshRequest).enqueue(new Callback<RefreshResponse>() {
            @Override
            public void onResponse(Call<RefreshResponse> call, retrofit2.Response<RefreshResponse> response) {
                Log.d("udoLog", "토큰 갱신 body 내용 = " + response.body());
                Log.d("udoLog", "토큰 갱신 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "토큰 갱신 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    RefreshResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //받은 토큰 저장
                    RefreshResponse.TokenList tokenList = result.getList();
                    String accToken = tokenList.getAccessToken();
                    String refToken = tokenList.getRefreshToken();

                    int success = 200; //로그인 성공
                    int errorTk = 403; //토큰 유효x
                    int errorId = 500; //아이디, 비밀번호 일치x

                    if (resultCode == success) {
                        //token 갱신
                        setPreference("accToken", accToken);
                        setPreference("refToken", refToken);

                    } else if (resultCode == errorId) {
                        Log.d("udoLog", "errorStatus: 500");

                    } else if (resultCode == errorTk) {
                        Log.d("udoLog", "errorStatus: 403");
                    } else {
                        Log.d("udoLog", "errorStatus: else");
                    }
                } else {
                    Log.d("udoLog", "errorStatus: ConnectionError");
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<RefreshResponse> call, Throwable t) {
                Log.d("udoLog", "errorStatus: ConnectionFailure");
            }
        });
    }

    //데이터를 내부 저장소에 저장하기
    public void setPreference(String key, String value){
        SharedPreferences pref = context.getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    //내부 저장소에 저장된 데이터 가져오기
    public String getPreferenceString(String key) {
        SharedPreferences pref = context.getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        return pref.getString(key, "");
    }

}