package com.example.b07project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventPresenter {
    private EventView eventView;
    private DatabaseReference database;

    public EventPresenter(EventView eventView, DatabaseReference database) {
        this.eventView = eventView;
        this.database = database;
    }

    public void pushEvent(Event event) {
        // Push event to allEvents
        this.database.child("allEvents")
                .child(String.valueOf(event.getEventID()))
                .setValue(event);

        // Push event's eventID to its Venue
        DatabaseReference eventRef = this.database.child("venues")
                .child(String.valueOf(event.getVenueID()))
                .child("events")
                .push();
        eventRef.setValue(event.getEventID());
    }

    public void removeEvent(int eventID) {
        // Remove from allEvents
        this.database.child("allEvents")
                .child(String.valueOf(eventID))
                .removeValue();

        // Remove from venues
        this.database.child("venues")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // For each venue in venues
                        for(DataSnapshot venue: dataSnapshot.getChildren()) {
                            // For each event
                            for(DataSnapshot event: venue.child("events").getChildren()) {
                                // Delete if eventID matches
                                if(event.getValue().toString().equals(String.valueOf(eventID))) {
                                    event.getRef().removeValue();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void addPlayer(int eventID) {
        this.database.child("allEvents")
                .child(String.valueOf(eventID))
                // TODO finish
    }

//    // TODO: add function to grab from database and create event object
//    public void pullEvent(int eventID) {
//
//    }
}
