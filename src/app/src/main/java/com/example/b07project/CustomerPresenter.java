package com.example.b07project;

/*
responsible for handling action from view and updating UI as needed
 */

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerPresenter {
    private CustomerView cView;
    private DatabaseReference database;
    static int totalNumCustomer=0;
    public CustomerPresenter(CustomerView cView, DatabaseReference database) {
        this.cView = cView;
        this.database = database;
    }

    public void pushCustomer(Customer c){
        totalNumCustomer++;
        this.database.child("customers")
//                .child("customer"+String.valueOf(totalNumCustomer))
                .child(c.getUsername())
                .setValue(c);
    }


}
