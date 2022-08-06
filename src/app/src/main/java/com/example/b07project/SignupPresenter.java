package com.example.b07project;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class SignupPresenter {
    private DatabaseReference database;

    SignupPresenter(DatabaseReference database) {
        this.database = database;
    }

    public void checkDuplicateAdmin(String username, Context context, SignupCallback.CheckDuplicateAdminCallback checkDuplicateAdminCallback) {
        this.database.child("admins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean duplicate = false;
                for(DataSnapshot admin: snapshot.getChildren()) {
                    if(admin.child("username").getValue(String.class).equals(username)) {
                        duplicate = true;
                    }
                }
                if(!duplicate) {
                    checkDuplicateAdminCallback.checkDuplicateAdminCallback();
                }
                else {
                    Toast.makeText(context,"Username already exists",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void checkDuplicateUser(String username, Context context, SignupCallback.CheckDuplicateUserCallback checkDuplicateUserCallback) {
        this.database.child("customers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean duplicate = false;
                for(DataSnapshot customer: snapshot.getChildren()) {
                    Customer c = customer.getValue(Customer.class);
                    if(c.getUsername().equals(username)) {
                        duplicate = true;
                    }
                }
                if(!duplicate) {
                    checkDuplicateUserCallback.checkDuplicateUserCallback();
                }
                else {
                    Toast.makeText(context,"Username already exists",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
