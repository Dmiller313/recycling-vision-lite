package com.prj666.recycling_vision;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;

public class ResultOverlay extends AppCompatActivity {

    private final String SETTINGS_FILE = "accountSettings.txt";
    private TextView instructions;
    private String percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_overlay);

        Intent intent = getIntent();
        byte [] bytes = intent.getExtras().getByteArray("picture");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        instructions = findViewById(R.id.instructions);
        TextView objectName = findViewById(R.id.objectName);
        TextView matchProbability = findViewById(R.id.matchProbability);
        TextView matchFound = findViewById(R.id.matchResult);
        ImageView image = findViewById(R.id.objectImage);
        Button back = findViewById(R.id.back);

        double percentage = Double.parseDouble(intent.getStringExtra("matchPercent"));
        String displayPercent = new DecimalFormat("#.00").format(percentage) + "% match";
        matchProbability.setText(displayPercent);

        String object = intent.getStringExtra("object");
        objectName.setText(object);
        image.setImageBitmap(bmp);

        if(object.equals("none")){
            matchFound.setText("Match Not Found");
            instructions.setText("Please try again");
        }
        else{
            matchProbability.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);

            String url = "https://recycling-vision.herokuapp.com/item/single";
            Map<String, String> jsonData = new HashMap<>();
            jsonData.put("itemName", object);
            JSONObject json = new JSONObject(jsonData);

            final String[] result = {""};
            boolean history = true;
            File userSettingsFile = new File(this.getFilesDir(), SETTINGS_FILE);
            if(userSettingsFile.exists()){
                FileReader fr = null;
                try {
                    fr = new FileReader(userSettingsFile);
                    BufferedReader br = new BufferedReader(fr);
                    String fileContents;
                    while ((fileContents = br.readLine()) != null && history) {
                        if(fileContents.equals("objectHistoryEnabled=false")){
                            history = false;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //TODO: DELETE THIS?

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST, url, json, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            result[0] = response.getString("data");
                            instructions.setText(result[0]);

                        } else {
                            result[0] = "Error";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    result[0] = "error";
                }
            });
            queue.add(request);
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photo = new Intent(ResultOverlay.this, TakePhoto.class);
                ResultOverlay.this.startActivity(photo);
            }
        });
    }

}