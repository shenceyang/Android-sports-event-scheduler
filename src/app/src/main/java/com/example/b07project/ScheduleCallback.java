package com.example.b07project;

public interface ScheduleCallback {
    interface GetScheduleCallback {
        void getScheduleCallback(Schedule schedule);
    }
    interface IsScheduledCallback {
        void isScheduledCallback();
    }
}
