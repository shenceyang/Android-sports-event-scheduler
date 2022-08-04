package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import com.google.android.material.button.MaterialButton;

public class Customer_center extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_center);

        MaterialButton schedulebtn = (MaterialButton) findViewById(R.id.schedulebtn);
        MaterialButton bookbtn = (MaterialButton) findViewById(R.id.bookbtn);
        MaterialButton quitbtn = (MaterialButton) findViewById(R.id.quitbtn);

        schedulebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        quitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Customer_center.this,  login_page.class);
                startActivity(intent);
            }
        });

    }



}