package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class login_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);
        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);
        MaterialButton signupbtn = (MaterialButton) findViewById(R.id.signupbtn);

        //admin and admin testing
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    //correct
                    Toast.makeText(login_page.this,"Login successful",Toast.LENGTH_SHORT).show();
                }
                else{
                    //incorrect
                    Toast.makeText(login_page.this,"Login failed",Toast.LENGTH_SHORT).show();
                }
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_page.this, signup_page.class);
                startActivity(intent);
            }
        });

        //doing the redirect




    }
}