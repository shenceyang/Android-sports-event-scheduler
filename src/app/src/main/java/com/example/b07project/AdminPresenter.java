package com.example.b07project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminPresenter {
    private AdminView aView;
    private DatabaseReference database;
    static int totalNumAdmin=0;
    public AdminPresenter(AdminView aView, DatabaseReference database) {
        this.aView = aView;
        this.database = database;
    }

    public void pushAdmin(Admin a){
        totalNumAdmin++;
        this.database.child("admin")
                .child("admin"+String.valueOf(totalNumAdmin))
                .setValue(a);
    }
}
