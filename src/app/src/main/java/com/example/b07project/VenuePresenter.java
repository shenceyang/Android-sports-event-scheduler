package com.example.b07project;

import com.google.firebase.database.DatabaseReference;

public class VenuePresenter {
    private VenueView venueView;
    private DatabaseReference database;

    public VenuePresenter(VenueView venueView, DatabaseReference database) {
        this.venueView = venueView;
        this.database = database;
    }

    // TODO: add function to create and push to database
    public void pushVenue(Venue venue) {
        this.database.child("venues")
                .child(String.valueOf(venue.getVenueID()))
                .setValue(venue);
    }

    // TODO: add function to grab from database and create venue object
    public void pullVenue(int venueID) {

    }
}
