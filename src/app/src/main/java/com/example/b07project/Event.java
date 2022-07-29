package com.example.b07project;

//import android.app.DatePickerDialog;
//import java.util.Calendar;

public class Event {
    public static int totalEvents = 0;
    public static int nextEventID = 1;

    int eventID;
    int maxPlayers;
    int currPlayers = 0;
    int day;
    int month;
    int year;
    int startHour;
    int startMin;
    int endHour;
    int endMin;
    int venueID;

    public Event(int maxPlayers, int day, int month, int year, int startHour, int startMin, int endHour, int endMin, int venueID) {
        this.eventID = nextEventID;
        nextEventID++;
        this.maxPlayers = maxPlayers;
        this.day = day;
        this.month = month;
        this.year = year;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
        this.venueID = venueID;
        totalEvents++;
    }




    // TODO
    public boolean isValidTime(int startHour, int startMin, int endHour, int endMin) {
        return false;
    }

    public boolean playersCanJoin() {
        return this.currPlayers < this.maxPlayers;
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

    public int getVenueID() {
        return venueID;
    }
}
