package com.prj666.recycling_vision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.prj666.recycling_vision.user.Settings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Terms extends AppCompatActivity {
    private final String SETTINGS_FILE = "tou.txt";
    private Settings userSettings;
    private File userSettingsFile;

    Button accept, decline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        accept = findViewById(R.id.accept);
        decline = findViewById(R.id.decline);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    userSettings = new Settings(null, null, null, null, true);
                    File f = new File(getApplicationContext().getFilesDir(), SETTINGS_FILE);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput(SETTINGS_FILE, Context.MODE_PRIVATE));
                    outputStreamWriter.write("touAccepted="+ userSettings.getTouAccepted());
                    outputStreamWriter.close();

                    Intent i = new Intent(Terms.this, Navigation.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    finishAffinity();
                }
                    catch (IOException e)
                {
                    e.getMessage();
                }
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        //TODO: set button to activate/appear when scrollview hits bottom (https://stackoverflow.com/questions/4953692/android-detecting-when-scrollview-hits-bottom)
        //TODO: create some method of local persistence to check if TOU accepted; see Login
        //TODO: change Navigation to accept TOU properly
        //TODO: create onClickListeners for the buttons, "Decline" closes the app, "Accept" persists the choice
        //TODO: set up onBackPressed()



    }

    private void createHistoryFile()
    {
        userSettings = new Settings(null, null, null, null, true);
        userSettingsFile = new File(this.getFilesDir(), SETTINGS_FILE);

        try {
            if (!userSettingsFile.exists()) {
                if (userSettingsFile.createNewFile()) {
                    System.out.println("file created successfully");

                    AssetManager userSettingsAsset = getAssets();
                    InputStream is = userSettingsAsset.open(SETTINGS_FILE);
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String fileContents = "";
                    FileWriter fw;
                    fw = new FileWriter(userSettingsFile);

                    while ((fileContents = br.readLine()) != null) {
                        fw.write(fileContents);
                    }

                    fw.close();
                    br.close();
                    is.close();
                    System.out.println("file written successfully");
                } else {
                    System.out.println("error creating the file");
                }
            }
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}