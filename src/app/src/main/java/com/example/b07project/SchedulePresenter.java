package com.example.b07project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SchedulePresenter {

    private ScheduleView sView;
    private DatabaseReference database;
    public static int totalNumSchedule = 0;

    public SchedulePresenter(ScheduleView sView, DatabaseReference database) {
        this.sView = sView;
        this.database = database;
    }

    //add schedule
    public void pushSchedule(Schedule s){
        totalNumSchedule++;
        this.database.child("Schedules")
                .child("schedule" + String.valueOf(totalNumSchedule))
                .setValue(s);
    }





    //remove schedule



}
