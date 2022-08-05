package com.example.b07project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventPresenter {
    private DatabaseReference database;

    public EventPresenter(DatabaseReference database) {
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
        this.database.child("venues").addListenerForSingleValueEvent(new ValueEventListener() {
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
        // Remove from schedules
        this.database.child("schedules").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot schedule: snapshot.getChildren()) {
                    if(schedule.child("eventID").getValue().toString().equals(String.valueOf(eventID))) {
                        schedule.getRef().removeValue();
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
                .child("currPlayers")
                .setValue(ServerValue.increment(1));
    }

    public void removePlayer(int eventID) {
        this.database.child("allEvents")
                .child(String.valueOf(eventID))
                .child("currPlayers")
                .setValue(ServerValue.increment(-1));
    }

//    // TODO to not allow duplicate events?
//    public boolean isInDatabase(int eventID) {
//        return false;
//    }
//
//    // TODO check if sport valid? (BUT COULD JUST ONLY GIVE THE CORRECT OPTIONS)
//    public boolean isValidSport(int venueID, String sport) {
//        return false;
//    }

    // TODO functionality to modify event data in database?

    public void getEvent(int eventID, EventCallback.GetEventCallback eventCallback) {
        this.database.child("allEvents").child(String.valueOf(eventID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot event) {
                Event e = event.getValue(Event.class);
                eventCallback.getEventCallback(e);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getSortedListEvents(EventCallback.GetSortedListEventsCallback eventCallback) {
        List<Event> sortedEvents = new ArrayList<Event>();
        this.database.child("allEvents").orderByChild("sortKey").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sortedEvents.clear();
                for(DataSnapshot event: snapshot.getChildren()) {
                    Event toAdd = event.getValue(Event.class);
                    sortedEvents.add(toAdd);
                }
                eventCallback.getSortedListEventsCallback(sortedEvents);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
