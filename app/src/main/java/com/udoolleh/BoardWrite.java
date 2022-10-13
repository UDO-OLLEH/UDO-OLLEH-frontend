package com.udoolleh;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardWrite extends AppCompatActivity {
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    Button board_write_close, boardWrite_Posting, boardWrite_Image;
    EditText boardWrite_Title, boardWrite_Hashtag, boardWrite_Context;
    ArrayList writePictures;
    Bitmap bitmap;
    ArrayList<Uri> uriListTmp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_board_write);

        //뒤로가기 버튼
        board_write_close = findViewById(R.id.board_write_close);
        board_write_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        boardWrite_Title = findViewById(R.id.boardWrite_Title);
        boardWrite_Hashtag = findViewById(R.id.boardWrite_Hashtag);
        boardWrite_Context = findViewById(R.id.boardWrite_Context);
        boardWrite_Posting = findViewById(R.id.boardWrite_Posting);
        boardWrite_Image = findViewById(R.id.boardWrite_Image);

        //이미지 선택
        boardWrite_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writePictures = new ArrayList<>();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivity(intent);
                //startActivityForResult(intent, Integer.parseInt("CODE_ALBUM_REQUEST"));
            }
        });

        //게시 버튼
        boardWrite_Posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();

                String title = boardWrite_Title.getText().toString();
                String hashtag = boardWrite_Hashtag.getText().toString();
                String context = boardWrite_Context.getText().toString();

                //내용 미입력 시
                if (title.trim().length() == 0 || hashtag.trim().length() == 0 || context.trim().length() == 0 || title == null || hashtag == null || context == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(BoardWrite.this);
                    builder.setTitle("알림")
                            .setMessage("내용을 입력하세요.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    //게시판 등록 통신
                    BoardWriteResponse();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String bitmapString = "";
        if(resultCode == RESULT_OK && data != null){
            ArrayList<Uri> uriList = new ArrayList<>();
            if(data.getClipData() != null){
                ClipData clipData = data.getClipData();
                if(clipData.getItemCount() > 3) {
                    Toast.makeText(BoardWrite.this, "사진은 3개까지 선택가능합니다", Toast.LENGTH_SHORT).show();
                    return;
                } else if(clipData.getItemCount() == 1){
                    Uri filePath = clipData.getItemAt(0).getUri();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        bitmapString = bitmapToString(bitmap);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    writePictures.add(bitmapString);
                    uriList.add(filePath);
                } else if(clipData.getItemCount() > 1 && clipData.getItemCount() <= 3){
                    for(int i = 0; i<clipData.getItemCount(); i++){
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), clipData.getItemAt(i).getUri());
                            bitmapString = bitmapToString(bitmap);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        writePictures.add(bitmapString);
                        uriList.add(clipData.getItemAt(i).getUri());
                    }
                }
            }
            uriListTmp = uriList;
            //imageAdapter adapter = new UriImageAdapter(uriList, BoardWrite.this);
            //recyclerView.setAdapter(adapter);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String bitmapToString(Bitmap bitmap){ // Bitmap을 String 으로 변환
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.getEncoder().encodeToString(imageBytes);
        return imageString;
    }

    public void BoardWriteResponse() {
        //토큰 가져오기
        SharedPreferences sp = getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String accToken = sp.getString("accToken", "");

        String title = boardWrite_Title.getText().toString().trim();
        String hashtag = boardWrite_Hashtag.getText().toString().trim();
        String context = boardWrite_Context.getText().toString().trim();

        //HashMap 형태로 postDto 내용 저장
        RequestBody mapTitle = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody mapContext = RequestBody.create(MediaType.parse("text/plain"), context);

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("title", mapTitle);
        map.put("context", mapContext);

        /*
        //이미지 파일 업로드 구현 후 MultiPart 구현
        // 여러 file들을 담아줄 ArrayList
        ArrayList<MultipartBody.Part> files = new ArrayList<>();

        // 파일 경로들을 가지고있는 `ArrayList<Uri> filePathList`가 있다고 칩시다...
        for (int i = 0; i < uriListTmp.size(); ++i) {
            // Uri 타입의 파일경로를 가지는 RequestBody 객체 생성
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), String.valueOf(uriListTmp.get(i)));

            // 사진 파일 이름
            String fileName = "photo" + i + ".jpg";
            // RequestBody로 Multipart.Part 객체 생성
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("photo", fileName, fileBody);

            // 추가
            files.add(filePart);
        }
         */

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //accToken, files, map을 RequestParams와 RequestBody로 저장 후 getBoardWriteRespons로 함수를 실행한 후 응답을 받음
        retrofitInterface.getBoardWriteResponse(accToken, map).enqueue(new Callback<BoardWriteResponse>() {
            @Override
            public void onResponse(Call<BoardWriteResponse> call, Response<BoardWriteResponse> response) {
                Log.d("udoolleh", "Data fetch success");
                Log.d("udoolleh", "body 내용" + response.body());
                Log.d("udoolleh", "상태코드" + response.isSuccessful());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    BoardWriteResponse result = response.body();

                    //받은 코드 저장
                    String resultCode = result.getStatus();

                    String success = "200"; //게시 성공
                    String errPm = "400"; //파라미터 유효x
                    String errTk = "403"; //토큰 에러, 사용자 에러
                    Log.d("udoolleh", "상태코드tete" + resultCode);

                    if (resultCode.equals(success)) {
                        Toast.makeText(BoardWrite.this, "게시물을 등록하였습니다.", Toast.LENGTH_LONG).show();
                    } else if (resultCode.equals(errPm)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BoardWrite.this);
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다.\n 잠시후 다시 시도해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else if (resultCode.equals(errTk)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BoardWrite.this);
                        builder.setTitle("알림")
                                .setMessage("로그인 정보가 유효하지 않습니다.\n 로그인 상태를 확인해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BoardWrite.this);
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BoardWriteResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BoardWrite.this);
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
        imm.hideSoftInputFromWindow(boardWrite_Title.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(boardWrite_Hashtag.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(boardWrite_Context.getWindowToken(), 0);
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
