package com.udoolleh.signup;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.udoolleh.R;
import com.udoolleh.RetrofitClient;
import com.udoolleh.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp  extends AppCompatActivity {
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    Button signup_close;
    EditText id_signup, pw_signup, pw_signup_check, nickname;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup_close = findViewById(R.id.signup_close);
        id_signup = findViewById(R.id.id_signup);
        pw_signup = findViewById(R.id.pw_signup);
        pw_signup_check = findViewById(R.id.pw_signup_check);
        nickname = findViewById(R.id.nickname);
        signup = findViewById(R.id.signup);

        //x버튼 누르면 회원가입창 닫기
        signup_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = id_signup.getText().toString();
                String pw = pw_signup.getText().toString();
                String pw_ck = pw_signup_check.getText().toString();
                String nick = nickname.getText().toString();
                hideKeyboard();

                //회원가입 정보 미입력 시
                if (id.trim().length() == 0 || id == null || pw.trim().length() == 0 || pw == null || pw_ck.trim().length() == 0 || pw_ck == null || nick.trim().length() == 0 || nick == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    builder.setTitle("알림")
                            .setMessage("입력하지 않은 항목이 있습니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else if (pw.equals(pw_ck) == false) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    builder.setTitle("알림")
                            .setMessage("비밀번호가 일치하지 않습니다.\n" + pw +  " " + pw_ck)
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    pw_signup_check.setText("");

                } else {
                    //회원가입 통신
                    SignUpResponse();
                }
            }
        });
    }

    public void SignUpResponse() {
        String userID = id_signup.getText().toString().trim();
        String userPassword = pw_signup.getText().toString().trim();
        String userNickName = nickname.getText().toString().trim();

        //signUpRequest에 사용자가 입력한 id와 pw를 저장
        SignUpRequest signUpRequest = new SignUpRequest(userID, userPassword, userNickName);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(null);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //loginRequest에 저장된 데이터와 함께 init에서 정의한 getLoginResponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getSignUpResponse(signUpRequest).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {

                Log.d("udoolleh", "Data fetch success");
                Log.d("udoolleh", "body 내용" + response.body());
                Log.d("udoolleh", "상태코드" + response.isSuccessful());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    SignUpResponse result = response.body();

                    //받은 코드 저장
                    String resultCode = result.getStatus();

                    //받은 토큰 저장
                    //String token = result.getAccessToken();

                    String success = "200"; //로그인 성공
                    String errorTk = "403"; //토큰 유효x
                    String errorId = "500"; //아이디, 비밀번호 일치x



                    if (resultCode.equals(success)) {
                        String userID = id_signup.getText().toString();
                        String userPassword = pw_signup.getText().toString();

                        //다른 통신을 하기 위해 token 저장
                        //setPreference(token,token);

                        Toast.makeText(SignUp.this, "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
                        finish();

                    } else if (resultCode.equals(errorId)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                        builder.setTitle("알림")
                                .setMessage("사용중인 아이디입니다.\n")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    } else if (resultCode.equals(errorTk)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                        builder.setTitle("알림")
                                .setMessage("로그인 토큰이 유효하지 않습니다.\n 고객" + "센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();

                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    builder.setTitle("알림")
                            .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    //키보드 숨기기
    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(id_signup.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(pw_signup.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(pw_signup_check.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(nickname.getWindowToken(), 0);
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
}
