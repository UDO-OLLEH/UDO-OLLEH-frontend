package com.udoolleh;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserEditProfile extends AppCompatActivity {
    Context context;
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    Button edit_profile_close, edit_profile_ok;
    FrameLayout profile_image;
    EditText profile_nickname;
    ImageView profile_image_resource;
    ContentResolver contentResolver;
    Uri URI;
    String cacheImageFileName = "udoolleh_cache_file_image";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_edit_profile);
        context = getApplicationContext();

        edit_profile_close = findViewById(R.id.edit_profile_close);
        edit_profile_ok = findViewById(R.id.edit_profile_ok);
        profile_image = findViewById(R.id.profile_image);
        profile_nickname = findViewById(R.id.profile_nickname);
        profile_image_resource = findViewById(R.id.profile_image_resource);

        //뒤로가기 버튼
        edit_profile_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //이미지 선택
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        //완료 버튼
        edit_profile_ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                hideKeyboard();

                String nickname = profile_nickname.getText().toString();

                //내용 미입력 시
                if (nickname.trim().length() == 0 || nickname == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(UserEditProfile.this);
                    builder.setTitle("알림")
                            .setMessage("내용을 입력하세요.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    //유저 정보 수정 통신
                    EditProfileResponse();

                    if(profile_image_resource.getDrawable() != getDrawable(R.drawable.ic_baseline_account_circle_24)) {
                        //유저 프로필 사진 등록 및 수정 통신
                        EditProfileImageResponse();
                    }

                    //임시 저장한 이미지 삭제
                    deleteBitmapImage(view);

                    Toast.makeText(UserEditProfile.this, "프로필 정보를 변경하였습니다.", Toast.LENGTH_LONG).show();

                }

                finish();
            }
        });

    }

    //갤러리에서 이미지 골라 표시
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri uriResource = data.getData();
                    ContentResolver resolver = getContentResolver();
                    try {
                        InputStream instream = resolver.openInputStream(uriResource);
                        Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                        imgBitmap = rotateImage(uriResource, imgBitmap);
                        profile_image_resource.setImageBitmap(imgBitmap);
                        URI = uriResource;
                        contentResolver = resolver;
                        instream.close();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "사진을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    //Bitmap Rotate
    @RequiresApi(api = Build.VERSION_CODES.N)
    private Bitmap rotateImage(Uri uri, Bitmap bitmap) throws IOException {
        InputStream in = getContentResolver().openInputStream(uri);
        ExifInterface exif = new ExifInterface(in);
        in.close();
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Matrix matrix = new Matrix();
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            matrix.postRotate(90);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            matrix.postRotate(180);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            matrix.postRotate(270);
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //Bitmap Resize
    public Bitmap resizeBitmapImage(Bitmap source, int maxResolution) {
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if(width > height) {
            if(maxResolution < width) {
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        } else {
            if(maxResolution < height) {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }
        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    //Bitmap Cache Delete
    public void deleteBitmapImage(View view) {    // 이미지 삭제
        try {
            File file = getCacheDir();  // 내부저장소 캐시 경로를 받아오기
            File[] flist = file.listFiles();
            for (int i = 0; i < flist.length; i++) {    // 배열의 크기만큼 반복
                if (flist[i].getName().equals(cacheImageFileName)) {   // 삭제하고자 하는 이름과 같은 파일명이 있으면 실행
                    flist[i].delete();  // 파일 삭제
                }
            }
        } catch (Exception e) {
        }
    }

    //Bitmap to Uri
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, cacheImageFileName, null);
        return Uri.parse(path);
    }

    //Uri to Multipart
    public static MultipartBody.Part uriToMultipart(final Uri uri, String name, final ContentResolver contentResolver) {
        final Cursor c = contentResolver.query(uri, null, null, null, null);
        if (c != null) {
            if(c.moveToNext()) {
                @SuppressLint("Range") final String displayName = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                RequestBody requestBody = new RequestBody() {
                    @Override
                    public MediaType contentType() {
                        return MediaType.parse(contentResolver.getType(uri));
                    }

                    @Override
                    public void writeTo(BufferedSink sink) {
                        try {
                            sink.writeAll(Okio.source(contentResolver.openInputStream(uri)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                c.close();
                return MultipartBody.Part.createFormData(name, displayName, requestBody);
            } else {
                c.close();
                return null;
            }
        } else {
            return null;
        }
    }

    //유저 정보 수정 (닉네임) 통신
    public void EditProfileResponse() {
        //토큰 가져오기
        String accToken = getPreferenceString("accToken");
        String nickname = getPreferenceString("UserNickNameValue");
        String id = getPreferenceString("UserIdValue");
        String password = getPreferenceString("UserPwValue");

        //EditText에 적은 nickname 저장
        String newNickname = profile_nickname.getText().toString().trim();

        //userEditProfileRequest에 새로운 닉네임과 비밀번호 저장
        UserEditProfileRequest userEditProfileRequest = new UserEditProfileRequest(newNickname, password);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //userEditProfileRequest에 저장된 데이터와 함께 interface에서 정의한 getUserEditProfileResponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getUserEditProfileResponse(userEditProfileRequest).enqueue(new Callback<UserEditProfileResponse>() {
            @Override
            public void onResponse(Call<UserEditProfileResponse> call, Response<UserEditProfileResponse> response) {
                Log.d("udoLog", "유저 정보 수정 body 내용 = " + response.body());
                Log.d("udoLog", "유저 정보 수정 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "유저 정보 수정 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    UserEditProfileResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //게시물 작성 성공
                    int success = 200;

                    if (resultCode == success) {
                        //실행 코드

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserEditProfile.this);
                        builder.setTitle("알림")
                                .setMessage("게시물을 등록할 수 없습니다.\n 다시 시도해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<UserEditProfileResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserEditProfile.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    //유저 프로필 사진 등록 및 수정 통신
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void EditProfileImageResponse() {
        //토큰 가져오기
        String accToken = getPreferenceString("accToken");

        MultipartBody.Part filePart;
        Bitmap bitmap = null;

        //Uri to bitmap
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), URI));
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), URI);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Bitmap Resize
        bitmap = resizeBitmapImage(bitmap, 1080);

        //Bitmap to Uri
        URI = getImageUri(context, bitmap);

        //Uri to Multipart
        filePart = uriToMultipart(URI, "file", contentResolver);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //Multipart image와 함께 getUserEditProfileImageResponse 함수를 실행한 후 응답을 받음
        retrofitInterface.getUserEditProfileImageResponse(filePart).enqueue(new Callback<UserEditProfileImageResponse>() {
            @Override
            public void onResponse(Call<UserEditProfileImageResponse> call, Response<UserEditProfileImageResponse> response) {
                Log.d("udoLog", "유저 프로필 사진 등록 및 수정 body 내용 = " + response.body());
                Log.d("udoLog", "유저 프로필 사진 등록 및 수정 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "유저 프로필 사진 등록 및 수정 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    UserEditProfileImageResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //게시물 작성 성공
                    int success = 200;

                    if (resultCode == success) {
                        //실행 코드

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserEditProfile.this);
                        builder.setTitle("알림")
                                .setMessage("게시물을 등록할 수 없습니다.\n 다시 시도해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<UserEditProfileImageResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserEditProfile.this);
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
        imm.hideSoftInputFromWindow(profile_nickname.getWindowToken(), 0);
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
