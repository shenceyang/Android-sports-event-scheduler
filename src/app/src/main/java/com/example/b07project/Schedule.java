package com.example.b07project;

public class Schedule {
    private String eventID;
    private String customerID;
    private String venueName;

    public Schedule(String eventID, String customerID, String venueName) {
        this.eventID = eventID;
        this.customerID = customerID;
        this.venueName = venueName;
    }


                //getters
    public String getEventID() {
        return eventID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getVenueName() {
        return venueName;
    }


                     //setters
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }
}
