package com.udoolleh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;
    BackPressCloseHandler backPressCloseHandler;
    EditText idLogin;
    EditText pwLogin;
    Button login;
    CheckBox autoLogin;
    TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //뒤로 가기 버튼 2번 클릭시 종료
        backPressCloseHandler = new BackPressCloseHandler(this);

        idLogin = findViewById(R.id.id_login);
        pwLogin = findViewById(R.id.pw_login);
        login = findViewById(R.id.login);
        autoLogin = findViewById(R.id.auto_login);
        signup = findViewById(R.id.signup);

        //자동 로그인을 선택한 유저
        if (!getPreferenceString("autoLoginId").equals("") && !getPreferenceString("autoLoginPw").equals("")) {
            autoLogin.setChecked(true);
            checkAutoLogin(getPreferenceString("autoLoginId"));
        }

        //로그인 버튼
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = idLogin.getText().toString();
                String pw = pwLogin.getText().toString();
                hideKeyboard();

                //로그인 정보 미입력 시
                if (id.trim().length() == 0 || pw.trim().length() == 0 || id == null || pw == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setTitle("알림")
                            .setMessage("로그인 정보를 입력바랍니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    //로그인 통신
                    LoginResponse();
                }
            }
        });

        //회원가입 버튼
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    public void LoginResponse() {
        String userID = idLogin.getText().toString().trim();
        String userPassword = pwLogin.getText().toString().trim();

        //loginRequest에 사용자가 입력한 id와 pw를 저장
        LoginRequest loginRequest = new LoginRequest(userID, userPassword);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        //loginRequest에 저장된 데이터와 함께 init에서 정의한 getLoginResponse 함수를 실행한 후 응답을 받음
        initMyApi.getLoginResponse(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                Log.d("retrofit", "Data fetch success");
                Log.d("resultCode", "body 내용" + response.body());
                Log.d("resultCode", "상태코드" + response.isSuccessful());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    LoginResponse result = response.body();

                    //받은 코드 저장
                    String resultCode = result.getStatus();

                    //받은 토큰 저장
                    String token = result.getAccessToken();

                    String success = "200"; //로그인 성공
                    String errorTk = "403"; //토큰 유효x
                    String errorId = "500"; //아이디, 비밀번호 일치x



                    if (resultCode.equals(success)) {
                        String userID = idLogin.getText().toString();
                        String userPassword = pwLogin.getText().toString();

                        //다른 통신을 하기 위해 token 저장
                        setPreference(token,token);

                        //자동 로그인 여부
                        if (autoLogin.isChecked()) {
                            setPreference("autoLoginId", userID);
                            setPreference("autoLoginPw", userPassword);
                        } else {
                            setPreference("autoLoginId", "");
                            setPreference("autoLoginPw", "");
                        }

                        Toast.makeText(Login.this, userID + "님 환영합니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("userId", userID);
                        startActivity(intent);
                        Login.this.finish();

                    } else if (resultCode.equals(errorId)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("알림")
                                .setMessage("로그인 정보가 일치하지 않습니다.\n")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    } else if (resultCode.equals(errorTk)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("알림")
                                .setMessage("로그인 토큰이 유효하지 않습니다.\n 고객" + "센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();

                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
        SharedPreferences pref = getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    //내부 저장소에 저장된 데이터 가져오기
    public String getPreferenceString(String key) {
        SharedPreferences pref = getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        return pref.getString(key, "");
    }


    //키보드 숨기기
    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(idLogin.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(pwLogin.getWindowToken(), 0);
    }

    //화면 터치 시 키보드 내려감
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //자동 로그인 유저
    public void checkAutoLogin(String id){

        Toast.makeText(this, id + "님 환영합니다.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    //뒤로 가기 버튼 2번 클릭시 종료
    @Override public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

}
