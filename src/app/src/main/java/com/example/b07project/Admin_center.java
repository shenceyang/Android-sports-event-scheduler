package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class Admin_center extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_center);

        MaterialButton addvenuebtn = (MaterialButton) findViewById(R.id.addvenuebtn);
        MaterialButton Venuelistbtn = (MaterialButton) findViewById(R.id.Venuelistbtn);
        MaterialButton quitbtn = (MaterialButton) findViewById(R.id.quitbtn);

        addvenuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin_center.this,  AddVenue.class);
                startActivity(intent);
            }
        });


        Venuelistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin_center.this,  VenueView.class);
                startActivity(intent);

            }
        });

        quitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }

}