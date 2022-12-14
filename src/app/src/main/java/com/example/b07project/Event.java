package com.example.b07project;

//import android.app.DatePickerDialog;
//import java.util.Calendar;

import android.util.Log;

import com.google.firebase.database.Exclude;

public class Event {
//    public static int totalEvents = 0;
//    public static int nextEventID = 1;

    private int eventID;
    private int maxPlayers;
    private int currPlayers = 0;
    private int day;
    private int month;
    private int year;
    private int startHour;
    private int startMin;
    private int endHour;
    private int endMin;
    private String sport;
    private int venueID;
    private long sortKey;

    public Event() {}

    public Event(int eventID, int maxPlayers, int day, int month, int year, int startHour, int startMin, int endHour, int endMin, String sport, int venueID) {
        this.eventID = eventID;
//        nextEventID++;
        this.maxPlayers = maxPlayers;
        this.day = day;
        this.month = month;
        this.year = year;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
        this.sport = sport;
        this.venueID = venueID;
//        totalEvents++;
        String sortKeyStr = String.valueOf(year) + String.format("%02d", month) + String.format("%02d", day) + String.format("%02d", startHour) + String.format("%02d", startMin);
        this.sortKey = Long.parseLong(sortKeyStr);
    }

    // TODO add other logic for "valid" events
    @Exclude
    public boolean isValidTime() {
        if(startHour > endHour) return false;
        else if(startHour < endHour) return true;
        else { // if same hour
            if(startMin >= endMin) return false;
            else return true;
        }
    }

    @Exclude
    public boolean isValidMaxPlayers() {
        return maxPlayers > 0;
    }

    // ********** GETTERS **********
    public int getEventID() {
        return eventID;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getCurrPlayers() {
        return currPlayers;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMin() {
        return endMin;
    }

    public String getSport() {
        return sport;
    }

    public int getVenueID() {
        return venueID;
    }

    public long getSortKey() {
        return sortKey;
    }
}
