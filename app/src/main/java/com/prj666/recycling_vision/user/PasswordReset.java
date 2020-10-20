package com.prj666.recycling_vision.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.prj666.recycling_vision.Navigation;
import com.prj666.recycling_vision.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PasswordReset extends AppCompatActivity {

    private TextView email, password, newPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        email = findViewById(R.id.edtTxtEmail);
        password = findViewById(R.id.edtTxtPassword);
        newPassword = findViewById(R.id.edtTxtNewPassword);
        Button reset = findViewById(R.id.btnReset);
        Button cancel = findViewById(R.id.cancel);



        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateData()) {
                    RequestQueue queue = Volley.newRequestQueue(getBaseContext());

                    String url = "https://recycling-vision.herokuapp.com/passwordreset";
                    Map<String, String> jsonData = new HashMap<>();
                    jsonData.put("email", email.getText().toString());
                    jsonData.put("password", password.getText().toString());
                    jsonData.put("newPassword", newPassword.getText().toString());

                    JSONObject json = new JSONObject(jsonData);

                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.POST, url, json, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String res = response.getString("status");
                                if (res.equals("success")) {
                                    Intent login = new Intent(PasswordReset.this, Login.class);
                                    PasswordReset.this.startActivity(login);
                                } else if (res.equals("unauthorized")) {
                                    Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "An error has occurred. Please try again", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "An error has occurred. Please try again", Toast.LENGTH_LONG).show();
                        }
                    });
                    queue.add(request);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nav = new Intent(PasswordReset.this, AccountSettings.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PasswordReset.this.startActivity(nav);


            }
        });
    }

    private boolean validateData() {
        if(password.getText().toString().isEmpty() ||
            newPassword.getText().toString().isEmpty() ||
            email.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please complete all fields", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!(newPassword.getText().toString().matches(Registration.getPasswordPattern()))) {
            Toast.makeText(getApplicationContext(), "Invalid Password, Must contain at least 1 digit, 1 lower case letter, 1 uppercase letter, 1 special character and" +
                    " be more than 8 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}