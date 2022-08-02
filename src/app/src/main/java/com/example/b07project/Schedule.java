package com.example.b07project;

public class Schedule {
    private int eventID;
    private String customerID;
    private int venueID;

    public Schedule(int eventID, String customerID, int venueID) {
        this.eventID = eventID;
        this.customerID = customerID;
        this.venueID = venueID;
    }


                //getters
    public int getEventID() {
        return eventID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public int getVenueName() {
        return venueID;
    }


                     //setters
    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setVenueName(int venueID) {
        this.venueID = venueID;
    }
}
