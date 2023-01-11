package com.udoolleh.view.board.activity;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.udoolleh.R;
import com.udoolleh.retrofit.RetrofitClient;
import com.udoolleh.retrofit.RetrofitInterface;
import com.udoolleh.view.board.DTO.BoardEditResponse;

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

public class BoardEdit extends AppCompatActivity {
    private RetrofitClient retrofitClient;
    private RetrofitInterface retrofitInterface;
    Context context;
    Button board_edit_close, boardEdit_Posting, boardEdit_Image;
    ImageView board_edit_image;
    EditText boardEdit_Title, boardEdit_Hashtag, boardEdit_Context;
    String boardId;
    ContentResolver contentResolver;
    Uri URI;
    String cacheImageFileName = "udoolleh_cache_file_image";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_board_edit);

        context = BoardEdit.this;

        board_edit_image = findViewById(R.id.board_edit_image);
        boardEdit_Title = findViewById(R.id.boardEdit_Title);
        boardEdit_Hashtag = findViewById(R.id.boardEdit_Hashtag);
        boardEdit_Context = findViewById(R.id.boardEdit_Context);
        boardEdit_Posting = findViewById(R.id.boardEdit_Posting);
        boardEdit_Image = findViewById(R.id.boardEdit_Image);

        Intent boardEdit = getIntent();

        boardId = boardEdit.getExtras().getString("boardId");
        boardEdit_Title.setText(boardEdit.getExtras().getString("title"));
        //boardEdit_Hashtag.setText(boardEdit.getExtras().getString("hashtag"));
        boardEdit_Context.setText(boardEdit.getExtras().getString("context"));
        //String img = boardEdit.getExtras().getString("photo");
        //Glide.with(context).load(img).into(board_edit_image);

        //뒤로가기 버튼
        board_edit_close = findViewById(R.id.board_edit_close);
        board_edit_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //이미지 선택
        boardEdit_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        //게시 버튼
        boardEdit_Posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();

                String context = boardEdit_Context.getText().toString();

                //내용 미입력 시
                if (context.trim().length() == 0 || context == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(BoardEdit.this);
                    builder.setTitle("알림")
                            .setMessage("내용을 입력하세요.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    //게시판 등록 통신
                    BoardEditResponse();
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
                        board_edit_image.setImageBitmap(imgBitmap);
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
    public void BoardEditResponse() {
        //토큰 가져오기
        SharedPreferences sp = getSharedPreferences("DATA_STORE", MODE_PRIVATE);
        String accToken = sp.getString("accToken", "");

        MultipartBody.Part filePart;
        if (board_edit_image.getDrawable() != null) {
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
        String title = boardEdit_Title.getText().toString().trim();
        String hashtag = boardEdit_Hashtag.getText().toString().trim();
        String context_temp = boardEdit_Context.getText().toString().trim();
        String context = context_temp.replaceAll(System.getProperty("line.separator"), "/n");
        RequestBody updateBoardDto = RequestBody.create(MediaType.parse("application/json"), "{\"title\": \"" + title + "\", \"hashtag\": \"" + hashtag + "\", \"context\": \"" + context + "\"}");
        Log.d("udoLog", "게시판 작성 requestDto = {\"title\": \"" + title + "\", \"hashtag\": \"" + hashtag + "\", \"context\": \"" + context + "\"}");

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance(accToken);
        retrofitInterface = RetrofitClient.getRetrofitInterface();

        //accToken, files, map을 RequestParams와 RequestBody로 저장 후 getBoardWriteResponse로 함수를 실행한 후 응답을 받음
        retrofitInterface.getBoardEditResponse(filePart, updateBoardDto, boardId).enqueue(new Callback<BoardEditResponse>() {
            @Override
            public void onResponse(Call<BoardEditResponse> call, Response<BoardEditResponse> response) {
                Log.d("udoLog", "게시판 수정 body 내용 = " + response.body());
                Log.d("udoLog", "게시판 수정 성공여부 = " + response.isSuccessful());
                Log.d("udoLog", "게시판 수정 상태코드 = " + response.code());

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    BoardEditResponse result = response.body();

                    //받은 코드 저장
                    int resultCode = response.code();

                    //게시물 작성 성공
                    int success = 200;

                    if (resultCode == success) {
                        Toast.makeText(BoardEdit.this, "게시물을 등록하였습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BoardEdit.this);
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
            public void onFailure(Call<BoardEditResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BoardEdit.this);
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
        imm.hideSoftInputFromWindow(boardEdit_Context.getWindowToken(), 0);
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
