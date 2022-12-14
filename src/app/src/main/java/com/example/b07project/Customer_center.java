package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class Customer_center extends AppCompatActivity {
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_center);

        this.userID = getIntent().getStringExtra("userID");
        MaterialButton schedulebtn = (MaterialButton) findViewById(R.id.schedulebtn);
        MaterialButton bookbtn = (MaterialButton) findViewById(R.id.bookbtn);
        MaterialButton quitbtn = (MaterialButton) findViewById(R.id.quitbtn);
        MaterialButton addEventbtn = (MaterialButton) findViewById(R.id.addEventbtn);
        MaterialButton seeVenuebtn = (MaterialButton) findViewById(R.id.seeVenuebtn);

        schedulebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pass userID
                Intent intent = new Intent(Customer_center.this,  ScheduleView.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        addEventbtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Customer_center.this,  NewEvent.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        }));


        bookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Customer_center.this,  EventView.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        quitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        seeVenuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pass userID
                Intent intent = new Intent(Customer_center.this,  VenueView.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

    }



}