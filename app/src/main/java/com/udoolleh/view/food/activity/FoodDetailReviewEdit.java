package com.udoolleh.view.food.activity;

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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.udoolleh.R;
import com.udoolleh.retrofit.RetrofitClient;
import com.udoolleh.retrofit.RetrofitInterface;
import com.udoolleh.view.food.DTO.FoodDetailReviewEditResponse;

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

public class FoodDetailReviewEdit extends AppCompatActivity {
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    Context context;
    Button food_edit_close, foodEdit_Posting, foodEdit_Image;
    ImageView food_edit_image;
    EditText foodEdit_Context;
    RatingBar foodEdit_Grade;
    ContentResolver contentResolver;
    Uri URI;
    String cacheImageFileName = "udoolleh_cache_file_image";
    String reviewId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_food_detail_review_edit);

        context = FoodDetailReviewEdit.this;

        food_edit_image = findViewById(R.id.food_edit_image);
        foodEdit_Context = findViewById(R.id.foodEdit_Context);
        foodEdit_Grade = findViewById(R.id.foodEdit_Grade);
        foodEdit_Posting = findViewById(R.id.foodEdit_Posting);
        foodEdit_Image = findViewById(R.id.foodEdit_Image);

        Intent reviewEdit = getIntent();

        reviewId = reviewEdit.getExtras().getString("reviewId");
        String img = reviewEdit.getExtras().getString("photo");
        Glide.with(context).load(img).into(food_edit_image);
        foodEdit_Context.setText(reviewEdit.getExtras().getString("context"));
        foodEdit_Grade.setRating((float) reviewEdit.getExtras().getDouble("grade"));

        //뒤로가기 버튼
        food_edit_close = findViewById(R.id.food_edit_close);
        food_edit_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //이미지 선택
        foodEdit_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        //게시 버튼
        foodEdit_Posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();

                String context = foodEdit_Context.getText().toString();

                //내용 미입력 시
                if (context.trim().length() == 0 || context == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(FoodDetailReviewEdit.this);
                    builder.setTitle("알림")
                            .setMessage("내용을 입력하세요.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    //게시판 등록 통신
                    FoodEditResponse();
                    //임시 저장한 이미지 삭제
                    deleteBitmapImage(view);
                }
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
                        food_edit_image.setImageBitmap(imgBitmap);
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

    //게시글 등록 레트로핏 통신
    public void FoodEditResponse() {
        //토큰 가져오기
        SharedPreferences sp = getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String accToken = sp.getString("accToken", "");

        MultipartBody.Part filePart;
        if (food_edit_image.getDrawable() != null) {
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
        } else {
            filePart = null;
        }

        //requestDto
        String context_temp = foodEdit_Context.getText().toString().trim();
        String context = context_temp.replaceAll(System.getProperty("line.separator"), "/n");
        float grade = foodEdit_Grade.getRating();

        RequestBody requestDto = RequestBody.create(MediaType.parse("application/json"), "{\"context\": \"" + context + "\", \"grade\": " + grade + "}");
        Log.d("udoLog", "리뷰 작성 requestDto = {\"context\": \"" + context + "\", \"grade\": " + grade + "}");

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //accToken, files, map을 RequestParams와 RequestBody로 저장 후 getFoodWriteResponse로 함수를 실행한 후 응답을 받음
        retrofitInterface.getFoodDetailReviewEditResponse(filePart, requestDto, reviewId).enqueue(new Callback<FoodDetailReviewEditResponse>() {
            @Override
            public void onResponse(Call<FoodDetailReviewEditResponse> call, Response<FoodDetailReviewEditResponse> response) {
                Log.d("udoLog", "맛집 리뷰 작성 body 내용 = " + response.body());
                Log.d("udoLog", "맛집 리뷰 작성 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "맛집 리뷰 작성 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    FoodDetailReviewEditResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //게시물 작성 성공
                    int success = 200;

                    if (resultCode == success) {
                        Toast.makeText(FoodDetailReviewEdit.this, "리뷰를 등록하였습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FoodDetailReviewEdit.this);
                        builder.setTitle("알림")
                                .setMessage("리뷰를 등록할 수 없습니다.\n 다시 시도해주세요.")
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
            public void onFailure(Call<FoodDetailReviewEditResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodDetailReviewEdit.this);
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
        imm.hideSoftInputFromWindow(foodEdit_Context.getWindowToken(), 0);
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
