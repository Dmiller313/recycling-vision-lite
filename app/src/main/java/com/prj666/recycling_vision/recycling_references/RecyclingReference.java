package com.prj666.recycling_vision.recycling_references;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.prj666.recycling_vision.Navigation;
import com.prj666.recycling_vision.R;

public class RecyclingReference extends AppCompatActivity {
    final String RECYCLING_REFERENCE_LINK = "https://www.toronto.ca/servicespayments/recycling-organics-garbage/houses/what-goes-in-my-blue-bin/";



    ImageView referenceLink;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycling_reference);

        referenceLink = findViewById(R.id.referenceLink);
        back = findViewById(R.id.back);



        referenceLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent openLink = new Intent(Intent.ACTION_VIEW, Uri.parse(RECYCLING_REFERENCE_LINK));
                startActivity(openLink);

            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nav = new Intent(RecyclingReference.this, Navigation.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                RecyclingReference.this.startActivity(nav);
            }
        });
    }
}
