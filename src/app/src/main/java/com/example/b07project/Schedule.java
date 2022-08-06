package com.example.b07project;

public class Schedule {
//    public static int nextScheduleID = 1;

    private int eventID;
    private String userID;
    private int venueID;
    private int scheduleID;

    public Schedule() {}

    public Schedule(int scheduleID, int eventID, String userID, int venueID) {
        this.eventID = eventID;
        this.userID = userID;
        this.venueID = venueID;
        this.scheduleID = scheduleID;
//        nextScheduleID++;
    }


                //getters
    public int getEventID() {
        return eventID;
    }

    public String getUserID() {
        return userID;
    }

    public int getVenueID() {
        return venueID;
    }

    public int getScheduleID() {
        return scheduleID;
    }


                     //setters
    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }
}
