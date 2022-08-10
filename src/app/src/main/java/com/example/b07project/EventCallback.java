package com.example.b07project;

import java.util.List;

public interface EventCallback {
    interface GetEventCallback {
        void getEventCallback(Event event);
    }
    interface GetSortedListEventsCallback {
        void getSortedListEventsCallback(List<Event> sortedEvents);
    }
}
