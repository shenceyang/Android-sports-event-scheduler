package com.example.b07project;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class ID {
    private DatabaseReference database;

    public ID (DatabaseReference database) {
        this.database = database;
    }

    public void incrementVenueID() {
        this.database.child("nextIds").child("venue").setValue(ServerValue.increment(1));
    }

    public void incrementEventID() {
        this.database.child("nextIds").child("event").setValue(ServerValue.increment(1));
    }

    public void incrementScheduleID() {
        this.database.child("nextIds").child("schedule").setValue(ServerValue.increment(1));
    }

    public void getNextVenueID(IDCallback.GetNextVenueIDCallback getNextVenueIDCallback) {
        this.database.child("nextIds").child("venue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int nextID = snapshot.getValue(int.class);
                getNextVenueIDCallback.getNextVenueIDCallback(nextID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        incrementVenueID();
    }

    public void getNextEventID(IDCallback.GetNextEventIDCallback getNextEventIDCallback) {
        this.database.child("nextIds").child("event").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int nextID = snapshot.getValue(int.class);
                getNextEventIDCallback.getNextEventIDCallback(nextID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        incrementEventID();
    }

    public void getNextScheduleID(IDCallback.GetNextScheduleIDCallback getNextScheduleIDCallback) {
        this.database.child("nextIds").child("schedule").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int nextID = snapshot.getValue(int.class);
                getNextScheduleIDCallback.getNextScheduleIDCallback(nextID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        incrementScheduleID();
    }
}
