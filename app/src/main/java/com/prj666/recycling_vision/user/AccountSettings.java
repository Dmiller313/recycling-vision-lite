package com.prj666.recycling_vision.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.prj666.recycling_vision.LogoutDialogFragment;
import com.prj666.recycling_vision.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AccountSettings extends AppCompatActivity {
    private final String SETTINGS_FILE = "accountSettings.txt";

    Switch historySwitch;
    Button changePassword, changeLocation, saveButton, logoutButton;

    Settings userSettings;
    File userSettingsFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        historySwitch = findViewById(R.id.historySwitch);
        changePassword = findViewById(R.id.changePassword);
        saveButton = findViewById(R.id.saveButton);
        logoutButton = findViewById(R.id.logout_button);

        /*creates or updates a text file stored on the user's device that holds a boolean value
        indicating if they enabled or disabled object history tracking*/
        createOrUpdateObjectHistoryFile();

        //enable the user to reset their password
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reset = new Intent(AccountSettings.this, PasswordReset.class);
                AccountSettings.this.startActivity(reset);
            }
        });

        //enables/disables the option to track history of previously identified objects
        historySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(historySwitch.isChecked())
                {
                    userSettings.setObjectHistoryEnabled(true);
                }
                else
                {
                    userSettings.setObjectHistoryEnabled(false);
                }
            }
        });

        //updates the user's preference for object history tracking
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {

                    File f = new File(getApplicationContext().getFilesDir(), SETTINGS_FILE);
                    BufferedWriter output = new BufferedWriter(new FileWriter(f));

                    output.write("objectHistoryEnabled="+ userSettings.getObjectHistoryEnabled());
                    output.flush();
                    output.close();

                    Toast.makeText(getApplicationContext(), "Settings saved successfully!", Toast.LENGTH_SHORT).show();
                }
                catch (IOException e)
                {
                    e.getMessage();
                }
            }
        });

        //allows user to exit their current session and redirects to the login screen
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogoutDialogFragment().show(getSupportFragmentManager(), "logout_dialog");
            }
        });
    }

    private void createOrUpdateObjectHistoryFile()
    {
        userSettings = new Settings(null, null);
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
            } else {
                FileReader fr = new FileReader(userSettingsFile);
                BufferedReader br = new BufferedReader(fr);

                String loadContents = "", data = "";
                while ((loadContents = br.readLine()) != null) {
                    data = loadContents;
                }

                br.close();
                fr.close();
                String getBooleanValue = data.substring(data.indexOf("=") + 1);

                //System.out.println(getBooleanValue);
                userSettings.setObjectHistoryEnabled(Boolean.parseBoolean(getBooleanValue));
                historySwitch.setChecked(userSettings.getObjectHistoryEnabled());
            }
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

}
