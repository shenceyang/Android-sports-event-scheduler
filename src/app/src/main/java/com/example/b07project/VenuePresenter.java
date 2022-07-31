package com.example.b07project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VenuePresenter {
    private VenueView venueView;
    private DatabaseReference database;
//    private Venue pulledVenue;

    public VenuePresenter(VenueView venueView, DatabaseReference database) {
        this.venueView = venueView;
        this.database = database;
    }

    public void pushVenue(Venue venue) {
        this.database.child("venues")
                .child(String.valueOf(venue.getVenueID()))
                .setValue(venue);

        for(String sport: venue.availableSports) {
            DatabaseReference sportRef = this.database.child("venues")
                    .child(String.valueOf(venue.getVenueID()))
                    .child("availableSports")
                    .push();
            sportRef.setValue(sport);
        }

        for(Event e: venue.events) {
            // Push all existing events in Venue into allEvents
            this.database.child("allEvents")
                    .child(String.valueOf(e.getEventID()))
                    .setValue(e);

            // Push the list the eventID into Venue
            DatabaseReference eventRef = this.database.child("venues")
                    .child(String.valueOf(venue.getVenueID()))
                    .child("events")
                    .push();
            eventRef.setValue(e.getEventID());
        }
    }

    public void removeVenue(int venueID) {
        // Delete venue
        this.database.child("venues")
                .child(String.valueOf(venueID))
                .removeValue();

        // Delete venue's events from allEvents
        this.database.child("allEvents")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // For each event in allEvents
                        for(DataSnapshot event: snapshot.getChildren()) {
                            if(event.child("venueID").getValue().toString().equals(String.valueOf(venueID))) {
                                event.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    // TODO add needed queries for getting venue info from database (name, availableSports) maybe also query for all events in this venue?
    public void getName(int venueID, VenueView venueView) {
        this.database.child("venues").child(String.valueOf(venueID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot venue) {
                String name = venue.child("venueName").getValue(String.class);
                venueView.getNameCallback(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getAvailableSports(int venueID, VenueView venueView) {
        this.database.child("venues").child(String.valueOf(venueID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot venue) {
                List<String> availableSports = new ArrayList<String>();
                for(DataSnapshot sport: venue.child("availableSports").getChildren()) {
                    availableSports.add(sport.getValue(String.class));
                }
                venueView.getAvailableSportsCallback(availableSports);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
