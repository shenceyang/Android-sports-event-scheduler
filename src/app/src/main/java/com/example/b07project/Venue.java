package com.example.b07project;

import com.google.firebase.database.Exclude;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Venue {
    public static int totalVenues = 0;
    public static int nextVenueID = 1;

    int venueID;
    String venueName;
    List<Event> events = new ArrayList<Event>(); // By eventID
    List<String> availableSports;

    public Venue() {}

    // For creating new Venues
    public Venue(String venueName, ArrayList<String> availableSports) {
        this.venueID = nextVenueID;
        nextVenueID++;
        this.venueName = venueName;
        this.availableSports = availableSports;
        totalVenues++;
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    // ********** GETTERS **********
    public int getVenueID() {
        return venueID;
    }

    public String getVenueName() {
        return venueName;
    }

    @Exclude
    public List<Event> getEvents() {
        return events;
    }

    @Exclude
    public List<String> getAvailableSports() {
        return availableSports;
    }
}
