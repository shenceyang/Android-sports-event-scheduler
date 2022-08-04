package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class login_page extends AppCompatActivity {
    private LoginPresenter loginPresenter;
    private DatabaseReference database = FirebaseDatabase.getInstance("https://android-sport-app-default-rtdb.firebaseio.com/").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        this.loginPresenter = new LoginPresenter(database);
        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);
        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);
        MaterialButton signupbtn = (MaterialButton) findViewById(R.id.signupbtn);

        //admin and admin testing
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO check if empty and set error (copy from signup_page)

                // Login Authentication
                if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    //correct
                    Toast.makeText(login_page.this,"Login successful",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(login_page.this,  Admin_center.class);
                    intent.putExtra("userID", "admin");
                    startActivity(intent);
                }
                // If not admin, check for user login
                // Will show Toast for incorrect login
                else{
                    loginPresenter.authenticateUser(username.getText().toString(), password.getText().toString(), login_page.this, new LoginCallback.AuthenticateUserCallback() {
                        @Override
                        public void authenticateUserCallback() {
                            Intent intent = new Intent(login_page.this,  Customer_center.class);
                            intent.putExtra("userID", username.getText().toString());
                            startActivity(intent);
                        }
                    });
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