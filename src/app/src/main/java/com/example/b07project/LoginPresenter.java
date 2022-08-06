package com.example.b07project;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginPresenter {
    private DatabaseReference database;

    LoginPresenter(DatabaseReference database) {
        this.database = database;
    }

    public void authenticateAdmin(String username, String password, Context context, LoginCallback.AuthenticateAdminCallback authenticateAdminCallback) {
        this.database.child("admins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                boolean gotAdmin = false;
                for(DataSnapshot admin: snapshot.getChildren()) {
                    String adminUsername = admin.child("username").getValue(String.class);
                    String adminPassword = admin.child("password").getValue(String.class);
                    if(adminUsername.equals(username) && adminPassword.equals(password)) {
//                        gotAdmin = true;
                        authenticateAdminCallback.authenticateAdminCallback();
                    }
                }
//                if(!gotAdmin) {
//                    Toast.makeText(context,"Login failed",Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void authenticateUser(String username, String password, Context context, LoginCallback.AuthenticateUserCallback authenticateUserCallback) {
        this.database.child("customers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean gotUser = false;
                for(DataSnapshot customer: snapshot.getChildren()) {
                    Customer c = customer.getValue(Customer.class);
                    if(c.getUsername().equals(username) && c.getPassword().equals(password)) {
                        gotUser = true;
                        authenticateUserCallback.authenticateUserCallback();
                    }
                }
                if(!gotUser) {
                    Toast.makeText(context,"Login failed",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
