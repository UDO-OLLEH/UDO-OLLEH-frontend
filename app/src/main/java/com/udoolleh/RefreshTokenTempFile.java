package com.udoolleh;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import retrofit2.Call;
import retrofit2.Callback;

public class RefreshTokenTempFile extends Application {
    private static Context context;
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    public void RefreshResponse() {

        //토큰 가져오기
        String refToken = getPreferenceString("refToken");

        //refreshRequest refToken 저장
        RefreshRequest refreshRequest = new RefreshRequest(refToken);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(refToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //refreshRequest에 저장된 데이터와 함께 init에서 정의한 getRefreshResponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getRefreshResponse(refreshRequest).enqueue(new Callback<RefreshResponse>() {
            @Override
            public void onResponse(Call<RefreshResponse> call, retrofit2.Response<RefreshResponse> response) {

                Log.d("udoLog", "Data fetch success");
                Log.d("udoLog", "body 내용" + response.body());
                Log.d("udoLog", "상태코드" + response.isSuccessful());

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

                    Log.d("udoLog", accToken + refToken);
                    int success = 200; //로그인 성공
                    int errorTk = 403; //토큰 유효x
                    int errorId = 500; //아이디, 비밀번호 일치x

                    if (resultCode == success) {
                        //token 갱신
                        setPreference("accToken", accToken);
                        setPreference("refToken", refToken);

                    } else if (resultCode == errorId) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("알림")
                                .setMessage("로그인 정보가 일치하지 않습니다.\n")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    } else if (resultCode == errorTk) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("알림")
                                .setMessage("로그인 토큰이 유효하지 않습니다.\n 고객" + "센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();

                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("알림")
                            .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<RefreshResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
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
