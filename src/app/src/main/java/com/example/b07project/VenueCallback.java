package com.example.b07project;

public interface VenueCallback {
    interface GetVenueCallback {
        void getVenueCallback(Venue venue);
    }
    interface GetVenueIDFromNameCallback {
        void getVenueIDFromNameCallback(int venueID);
    }
    interface SportInAvailableSportsCallback {
        void sportInAvailableSportsCallback();
    }
}
