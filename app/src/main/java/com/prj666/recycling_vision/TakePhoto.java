package com.prj666.recycling_vision;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TakePhoto extends AppCompatActivity implements ConfirmPictureFragment.ConfirmPictureListener {

    private byte[] img;
    private String filename;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap bmp;
    private ImageView previewImage;

    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        previewImage = findViewById(R.id.image_preview);
        Button takePhoto = findViewById(R.id.takephoto);
        final Button sendPhoto = findViewById(R.id.sendphoto);
        Button back = findViewById(R.id.back_takephoto);
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(TakePhoto.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                }
                else{
                    ActivityCompat.requestPermissions(TakePhoto.this, new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
                    if(ContextCompat.checkSelfPermission(TakePhoto.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        takePicture();
                    }
                    else{
                        Toast.makeText(TakePhoto.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        sendPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bmp != null){
                    pictureConfirmation();
                }
                else{
                    Toast.makeText(TakePhoto.this, "Please take a photo first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void takePicture(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(i.resolveActivity(getPackageManager()) != null){
            startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if(bmp != null){
                bmp.recycle();
            }
            Bundle extras = data.getExtras();
            bmp = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            previewImage.setImageBitmap(bmp);
            img = stream.toByteArray();
            File storage = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File image = File.createTempFile("rv-photo_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()), ".jpg", storage);
                filename = image.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendPhoto() throws IOException {

        ConstraintLayout take_photo_ll = findViewById(R.id.takephoto_view);
        LinearLayout processing_ll = findViewById(R.id.process_wait_view);

        //RequestQueue queue = Volley.newRequestQueue(this);
        take_photo_ll.setVisibility(View.GONE);
        processing_ll.setVisibility(View.VISIBLE);

        Runnable runnable = () -> {
            String url = "https://rv-tensorflow.herokuapp.com/upload";

            OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(60, TimeUnit.SECONDS).build();
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file",filename, RequestBody.create(MediaType.parse("image/jpeg"), img))
                     .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(response.isSuccessful()){
                runOnUiThread(()->{
                    processing_ll.setVisibility(View.GONE);
                });
                Intent resultOverlay = new Intent(TakePhoto.this, ResultOverlay.class);

                try {
                    String jsonData = response.body().string();
                    JSONObject json = new JSONObject(jsonData);
                    String object = json.getString("foundObject");
                    String percentage = json.getString("matchPercent");
                    resultOverlay.putExtra("matchPercent", percentage);
                    resultOverlay.putExtra("object", object);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                //resultOverlay.putExtra("filename", filename);
                resultOverlay.putExtra("picture", img);
                startActivity(resultOverlay);
            }
            else{
                    runOnUiThread(()->{
                        processing_ll.setVisibility(View.GONE);
                        take_photo_ll.setVisibility(View.VISIBLE);
                    });
                System.out.println("no response from server in time");
            }
        };

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        threadPool.execute(runnable);
    }

    private void pictureConfirmation(){
        FragmentManager fm = getSupportFragmentManager();
        ConfirmPictureFragment dialog = ConfirmPictureFragment.newInstance("Send photo");
        dialog.show(fm, "confirm_picture");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) throws IOException {
        sendPhoto();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length <= 0 || grantResults[0] != 0) {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(this, Navigation.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
