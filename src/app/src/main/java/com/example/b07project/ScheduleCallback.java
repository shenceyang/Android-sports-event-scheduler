package com.example.b07project;

import java.util.List;

public interface ScheduleCallback {
    interface GetScheduleCallback {
        void getScheduleCallback(Schedule schedule);
    }
    interface IsScheduledCallback {
        void isScheduledCallback();
    }
    interface GetAllSchedulesCallback {
        void getAllSchedulesCallBack(List<Schedule> allSchedules);
    }
}
