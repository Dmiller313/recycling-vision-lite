package com.prj666.recycling_vision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.prj666.recycling_vision.recycling_references.RecyclingReference;
import com.prj666.recycling_vision.user.AccountSettings;
import com.prj666.recycling_vision.user.Login;
import com.prj666.recycling_vision.user.Settings;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Navigation extends AppCompatActivity {
    Boolean accepted = false;
    private final String SETTINGS_FILE = "tou.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme); //Display loading screen
        super.onCreate(savedInstanceState);

        File userSettingsFile = new File(this.getFilesDir(), SETTINGS_FILE);
        if(userSettingsFile.exists()){
            FileReader fr = null;
            try {
                fr = new FileReader(userSettingsFile);
                BufferedReader br = new BufferedReader(fr);
                String fileContents;
                while ((fileContents = br.readLine()) != null && !accepted) {
                    if(fileContents.equals("touAccepted=true")){
                        accepted = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(accepted){
            String url = "https://recycling-vision.herokuapp.com/recyclingmessage/single";
            String tfUrl = "https://rv-tensorflow.herokuapp.com/";
            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("connection established");
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("service unavailable");
                    System.out.println(error.toString());
                }
            });
            queue.add(request);

            JsonObjectRequest tfRequest = new JsonObjectRequest(
                    Request.Method.GET, tfUrl, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("connection established to TF");
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("TF service unavailable");
                    System.out.println(error.toString());
                }
            });
            queue.add(tfRequest);

            if(Login.isUserLoggedIn()){
                setContentView(R.layout.activity_navigation);
                Button takePhoto = findViewById(R.id.takephoto);
                Button settings = findViewById(R.id.settings);
                Button reference = findViewById(R.id.reference);
                Button matchHistory = findViewById(R.id.nav_match_history);

                takePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(Navigation.this, TakePhoto.class);
                        startActivity(i);
                    }
                });

                settings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Navigation.this, AccountSettings.class);
                        startActivity(i);
                    }
                });

                reference.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Navigation.this, RecyclingReference.class);
                        startActivity(i);
                    }
                });

                matchHistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Navigation.this, MatchHistory.class);
                        startActivity(i);
                    }
                });

            }
            else {
                Intent i = new Intent(Navigation.this, Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        }
        else{
            Intent tou = new Intent(Navigation.this, Terms.class);
            tou.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(tou);
            finish();
        }

    }
}
