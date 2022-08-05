package com.example.b07project;

import java.util.List;

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
    interface GetAllVenuesCallback {
        void getAllVenuesCallback(List<Venue> allVenues);
    }
    interface GetAvailableSportsCallback {
        void getAvailableSportsCallback(List<String> sports);
    }
}
