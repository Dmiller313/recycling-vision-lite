package com.prj666.recycling_vision.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.prj666.recycling_vision.Navigation;
import com.prj666.recycling_vision.R;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ValidationEmail extends AppCompatActivity {

    public void onBackPressed() {
        startActivity(new Intent(this, Login.class));
        finishAffinity();
    }

    private TextView userMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validationemail);
        Button backButton = findViewById(R.id.back);
        userMessage = findViewById(R.id.results);

        final User user;
        user = getIntent().getExtras().getParcelable("User");

        backButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(ValidationEmail.this, Login.class));
                finishAffinity();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://recycling-vision.herokuapp.com/emailer";
        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("username", user.getUserName());
        jsonData.put("email", user.getEmail());
        jsonData.put("password", user.getPassword());
        jsonData.put("phoneNum", user.getPhoneNum());
        jsonData.put("postalCode", user.getPostalCode());
        jsonData.put("dateOfBirth", (user.getDateOfBirth().getYear() + 1900) + "-0" +
                (user.getDateOfBirth().getMonth() + 1) + "-" + user.getDateOfBirth().getDate() );
        JSONObject json = new JSONObject(jsonData);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                userMessage.setText(R.string.vemail_success);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                userMessage.setText(R.string.vemail_error);
            }
        });
        queue.add(request);

    }
}
