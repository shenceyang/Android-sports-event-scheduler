package com.example.b07project;

public interface IDCallback {
    interface GetNextVenueIDCallback {
        void getNextVenueIDCallback(int nextID);
    }
    interface GetNextEventIDCallback {
        void getNextEventIDCallback(int nextID);
    }
    interface GetNextScheduleIDCallback {
        void getNextScheduleIDCallback(int nextID);
    }
}
