package com.example.b07project;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Venue {
    public static int totalVenues = 0;
    public static int nextVenueID = 1;

    int venueID;
    String venueName;
    List<Event> events = new ArrayList<Event>();
    List<String> availableSports;

    // For creating new Venues
    public Venue(String venueName, ArrayList<String> availableSports) {
        this.venueID = nextVenueID;
        nextVenueID++;
        this.venueName = venueName;
        this.availableSports = availableSports;
        totalVenues++;
    }

    // For pulling existing venues from DB and creating object
    public Venue(int venueID, String venueName, ArrayList<String> availableSports) {
        this.venueID = venueID;
        this.venueName = venueName;
        this.availableSports = availableSports;
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

    public List<Event> getEvents() {
        return events;
    }

    public List<String> getAvailableSports() {
        return availableSports;
    }
}
