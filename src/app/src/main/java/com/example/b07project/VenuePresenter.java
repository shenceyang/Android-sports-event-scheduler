package com.example.b07project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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


//    // TODO: add function to grab from database and create venue object
//    public void pullVenueFromDB(int venueID, VenueCallback venueCallback) {
//        this.database.child("venues").child(String.valueOf(venueID)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
//                }
//                else {
////                    Log.d("thingy", String.valueOf(task.getResult().getValue(Venue.class).getVenueName()));
//
//                    venueCallback.setVenue(task.getResult().getValue(Venue.class));
//
////                    vP.setPulledVenue(task.getResult().getValue(Venue.class));
////                    Log.d("thingy", vP.getPulledVenue().getVenueName());
////                    Log.d("thingy", pulledVenue.getVenueName());
////                    pulledVenue = task.getResult().getValue(Venue.class);
////                    Log.d("firebase", String.valueOf(pulledVenue.getVenueName()));
////                    setPulledVenue(task.getResult().getValue(Venue.class));
//                }
//            }
//        });
////        Log.d("venue", String.valueOf(venueID));
////        this.database.child("venues").child(String.valueOf(venueID)).addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                setPulledVenue(snapshot.getValue(Venue.class));
//////                Log.d("venue", pulledVenue.getVenueName());
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////                Log.d("firebase", error.toString());
////            }
////        });
//    }
//
//    public void pullVenue(int venueID)  {
//        this.pullVenueFromDB(venueID, new VenueCallback() {
//            @Override
//            public void setVenue(Venue venue) {
//
//                pulledVenue = venue;
//                Log.d("thingy", pulledVenue.getVenueName());
//            }
//        });
////        Log.d("thingy", this.getPulledVenue().getVenueName());
//    }

//    public Venue getPulledVenue() {
//        return pulledVenue;
//    }
//
//    public void setPulledVenue(Venue pulledVenue) {
//        this.pulledVenue = pulledVenue;
//    }
}
