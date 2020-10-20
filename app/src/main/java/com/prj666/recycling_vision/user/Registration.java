package com.prj666.recycling_vision.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.prj666.recycling_vision.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!*@#$%^&+=/'?:,(){}~`_.-\\\\\\[\\]])(?=\\S+$).{8,}$";
    private static final String Date_pattern = "^[0-9]{4}([-])(((0[13578]|(10|12))\\1(0[1-9]|[1-2][0-9]|3[0-1]))|(02\\1(0[1-9]|[1-2][0-9]))|((0[469]|11)\\1(0[1-9]|[1-2][0-9]|30)))$";
    private static final String phone_pattern = "^(\\d{10})|(([\\(]?([0-9]{3})[\\)]?)?[ \\.\\-]?([0-9]{3})[ \\.\\-]([0-9]{4}))$\n";
    private TextView edtTxtUserName, edtTxtPassword, edtTxtRepeatPass,
            edtTxtEmail, edtTxtPhone, edtTxtPostalAddress, edtTxtYear,
            edtTxtMonth, edtTxtDay;
    private String validEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button btnRegister = findViewById(R.id.btnRegister);

        edtTxtUserName = findViewById(R.id.edtTxtUserName);
        edtTxtPassword = findViewById(R.id.edtTxtPassword);
        edtTxtRepeatPass = findViewById(R.id.edtTxtRepeatPass);
        edtTxtEmail = findViewById(R.id.edtTxtEmail);
        edtTxtPhone = findViewById(R.id.edtTxtPhone);
        edtTxtPostalAddress = findViewById(R.id.edtTxtPostalAddress);
        edtTxtYear = findViewById(R.id.edtTxtYear);
        edtTxtMonth = findViewById(R.id.edtTxtMonth);
        edtTxtDay = findViewById(R.id.edtTxtDay);

        RequestQueue queue = Volley.newRequestQueue(this);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String date = edtTxtYear.getText().toString() + "-" + edtTxtMonth.getText().toString() + "-" + edtTxtDay.getText().toString();
                Boolean valid = validateData(date);
                if (valid) {
                    try {
                        User user = new User(edtTxtUserName.getText().toString(), edtTxtPhone.getText().toString(), edtTxtEmail.getText().toString(),
                                edtTxtPassword.getText().toString(), edtTxtPostalAddress.getText().toString(),
                                date, false);


                        String url = "https://recycling-vision.herokuapp.com/exists";
                        Map<String, String> jsonData = new HashMap<>();
                        jsonData.put("email", edtTxtEmail.getText().toString());

                        JSONObject json = new JSONObject(jsonData);

                        JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.GET, url, json, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    validEmail = response.getString("status");
                                    if (validEmail.equals("available")) {
                                        Intent i = new Intent(getBaseContext(), ValidationEmail.class);
                                        i.putExtra("User", user);
                                        startActivity(i);

                                    } else {
                                        //Toast.makeText(getApplicationContext(), "A user has already used this email address", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(getApplicationContext(), "A user has already used this email address", Toast.LENGTH_LONG).show();



                            }
                        });
                        queue.add(request);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    //Toast.makeText(getApplicationContext(), "Missing Information", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    private boolean validateData(String dateString) {
        if (edtTxtUserName.getText().toString().isEmpty()  ) {
            Toast.makeText(getApplicationContext(), "Invalid Username", Toast.LENGTH_LONG).show();
            return false;
        }
        if (edtTxtPassword.getText().toString().isEmpty() ||
                edtTxtRepeatPass.getText().toString().isEmpty() ||
                !(edtTxtRepeatPass.getText().toString().matches(PASSWORD_PATTERN))) {
            Toast.makeText(getApplicationContext(), "Invalid Password, Must contain at least 1 digit, 1 lower case letter, 1 uppercase letter, 1 special character and" +
                            " be more than 8 characters",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if(!(edtTxtPassword.getText().toString().equals(edtTxtRepeatPass.getText().toString()))){
            Toast.makeText(getApplicationContext(), "Passwords don't match",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (edtTxtEmail.getText().toString().isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(edtTxtEmail.getText().toString()).matches())) {
            Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_LONG).show();

            return false;
        }
        if (edtTxtPhone.getText().toString().isEmpty() || !(edtTxtPhone.getText().toString().length() == 10)
         || !(edtTxtPhone.getText().toString().matches(phone_pattern)) ) {
            Toast.makeText(getApplicationContext(), "Invalid phone", Toast.LENGTH_LONG).show();
            return false;
        }

        if (edtTxtPostalAddress.getText().toString().isEmpty() || !(edtTxtPostalAddress.getText().toString().length() == 6)) {
            Toast.makeText(getApplicationContext(), "Invalid Postal Code", Toast.LENGTH_LONG).show();
            return false;
        }
        if(dateString.isEmpty() || !(dateString.matches(Date_pattern)) || dateString.length() != 10){
            Toast.makeText(getApplicationContext(), "Please enter a valid date", Toast.LENGTH_LONG);
            return false;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD", Locale.ENGLISH);
        Date date;
        int userDate;
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        try {

            date = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).parse(dateString);
            calendar.setTime(date);
            userDate = calendar.get(Calendar.YEAR);
            if( (currentYear - userDate) < 13 ){

                Toast.makeText(getApplicationContext(), "Must be 13 or older", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String getPasswordPattern()
    {
        return PASSWORD_PATTERN;
    }
}