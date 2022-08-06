package com.example.b07project;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SchedulePresenter {

    private ScheduleView sView;
    private EventPresenter eventPresenter;
    private DatabaseReference database;

    public SchedulePresenter(ScheduleView sView, EventPresenter eventPresenter, DatabaseReference database) {
        this.sView = sView;
        this.eventPresenter = eventPresenter;
        this.database = database;
    }

    //add schedule
    public void pushSchedule(Schedule s){
        this.database.child("schedules")
                .child(String.valueOf(s.getScheduleID()))
                .setValue(s);
        this.eventPresenter.addPlayer(s.getEventID());
    }

    public void removeSchedule(int scheduleID) {
        this.database.child("schedules")
                .child(String.valueOf(scheduleID))
                .removeValue();
        this.getSchedule(scheduleID, new ScheduleCallback.GetScheduleCallback() {
            @Override
            public void getScheduleCallback(Schedule schedule) {
                eventPresenter.removePlayer(schedule.getEventID());
            }
        });
    }

    public void getSchedule(int scheduleID, ScheduleCallback.GetScheduleCallback getScheduleCallback) {
        this.database.child("schedules").child(String.valueOf(scheduleID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot schedule) {
                Schedule s = schedule.getValue(Schedule.class);
                getScheduleCallback.getScheduleCallback(s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void isScheduled(String userID, int eventID, ScheduleCallback.IsScheduledCallback isScheduledCallback) {
        this.database.child("schedules").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot schedule: snapshot.getChildren()) {
                    Schedule s = schedule.getValue(Schedule.class);
                    if(s.getEventID() == eventID && s.getUserID().equals(userID)) {
                        isScheduledCallback.isScheduledCallback();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }







    //remove schedule



}
