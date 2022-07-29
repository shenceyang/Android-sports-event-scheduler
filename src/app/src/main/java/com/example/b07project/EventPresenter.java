package com.example.b07project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventPresenter {
    private EventView eventView;
    private DatabaseReference database;

    public EventPresenter(EventView eventView, DatabaseReference database) {
        this.eventView = eventView;
        this.database = database;
    }

    // TODO: Modify wrt to comments
    public void pushEvent(Event event) {
        // Pull venue
        // Add event to list of events in venue
        // Push Venue
        this.database.child("venues")
                .child(String.valueOf(event.getVenueID()))
                .child(String.valueOf(event.getEventID()))
                .setValue(event);
    }

    // TODO: add function to grab from database and create event object
    public void pullEvent(int eventID) {

    }
}
