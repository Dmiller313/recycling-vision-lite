package com.prj666.recycling_vision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.prj666.recycling_vision.user.Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MatchHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_history_recyclerview);

        ArrayList<MatchHistoryItem> matchHistoryItem = new ArrayList<MatchHistoryItem>();
        RequestQueue queue = Volley.newRequestQueue(this);
        RecyclerView rv = findViewById(R.id.match_history_recyclerview_container);
        TextView matchHistoryInfoMessage = findViewById(R.id.match_history_info_message);

        String url = "https://recycling-vision.herokuapp.com/matchhistoryitem";
        JSONArray jsonArray = new JSONArray();
        JSONObject userIDJson = new JSONObject();
        try {
            userIDJson.put("userID", Login.getUserId());
            jsonArray.put(userIDJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.POST, url, jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() > 0) {
                    matchHistoryInfoMessage.setVisibility(View.INVISIBLE);
                    rv.setVisibility(View.VISIBLE);

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject item = response.getJSONObject(i);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            Date matchDate = dateFormat.parse(item.getString("matchDateTime").substring(0, item.getString("matchDateTime").length() - 14));
                            byte[] array = Base64.decode(item.getString("objectImage"), Base64.DEFAULT);
                            Bitmap bm = BitmapFactory.decodeByteArray(array, 0, array.length);

                            matchHistoryItem.add(new MatchHistoryItem(
                                    item.getInt("historyItemID"),
                                    item.getInt("objectID"),
                                    item.getInt("userID"),
                                    item.getString("foundRecyclingInstruction"),
                                    matchDate
                            ));
                            matchHistoryItem.get(i).setObjectImageBitmap(bm);
                            matchHistoryItem.get(i).setObjectName(item.getString("objectName"));
                            matchHistoryItem.get(i).setProbabilityMatch(item.getDouble("probabilitymatch"));

                            runOnUiThread(() -> {
                                MatchHistoryRecyclerAdapter matchHistoryRecyclerAdapter = new MatchHistoryRecyclerAdapter(getApplication(), matchHistoryItem);
                                rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                rv.setAdapter(matchHistoryRecyclerAdapter);
                            });

                        }

                    } catch (JSONException | ParseException ex) {
                        ex.printStackTrace();
                    }
                }
                else
                {
                    rv.setVisibility(View.INVISIBLE);
                    matchHistoryInfoMessage.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });
        queue.add(request);

        ImageView backArrow = findViewById(R.id.left_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Navigation.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }
}
