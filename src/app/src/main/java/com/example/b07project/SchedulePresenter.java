package com.example.b07project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SchedulePresenter {

    private ScheduleView sView;
    private EventPresenter eventPresenter;
    private DatabaseReference database;
    public static int totalNumSchedule = 0;

    public SchedulePresenter(ScheduleView sView, EventPresenter eventPresenter, DatabaseReference database) {
        this.sView = sView;
        this.eventPresenter = eventPresenter;
        this.database = database;
    }

    //add schedule
    public void pushSchedule(Schedule s){
        totalNumSchedule++;
        this.database.child("Schedules")
                .child("schedule" + String.valueOf(totalNumSchedule))
                .setValue(s);
        this.eventPresenter.addPlayer(s.getEventID());
    }





    //remove schedule



}
