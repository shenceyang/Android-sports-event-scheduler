package com.example.b07project;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VenuePresenter {
    private DatabaseReference database;

    public VenuePresenter(DatabaseReference database) {
        this.database = database;
    }

    public void pushVenue(Venue venue) {
        this.database.child("venues")
                .child(String.valueOf(venue.getVenueID()))
                .setValue(venue);

        for(String sport: venue.availableSports) {
            DatabaseReference sportRef = this.database.child("venues")
                    .child(String.valueOf(venue.getVenueID()))
                    .child("availableSports")
                    .push();
            sportRef.setValue(sport);
        }

        for(Event e: venue.events) {
            // Push all existing events in Venue into allEvents
            this.database.child("allEvents")
                    .child(String.valueOf(e.getEventID()))
                    .setValue(e);

            // Push the list the eventID into Venue
            DatabaseReference eventRef = this.database.child("venues")
                    .child(String.valueOf(venue.getVenueID()))
                    .child("events")
                    .push();
            eventRef.setValue(e.getEventID());
        }
    }

    public void removeVenue(int venueID) {
        // Delete venue
        this.database.child("venues")
                .child(String.valueOf(venueID))
                .removeValue();

        // Delete venue's events from allEvents
        this.database.child("allEvents")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // For each event in allEvents
                        for(DataSnapshot event: snapshot.getChildren()) {
                            if(event.child("venueID").getValue().toString().equals(String.valueOf(venueID))) {
                                event.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    // TODO add needed queries for getting venue info from database (name, availableSports) maybe also query for all events in this venue?
    public void getVenue(int venueID, VenueCallback.GetVenueCallback venueCallback) {
        this.database.child("venues").child(String.valueOf(venueID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot venue) {
                Venue v = venue.getValue(Venue.class);
                venueCallback.getVenueCallback(v);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getVenueIDFromName(String venueName, Context context, VenueCallback.GetVenueIDFromNameCallback venueCallback) {
        this.database.child("venues").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot venues) {
                boolean gotVenueID = false;
                int venueID = -1;
                for(DataSnapshot venue: venues.getChildren()) {
                    // If find match
                    if(venue.child("venueName").getValue(String.class).equals(venueName)) {
                        Venue v = venue.getValue(Venue.class);
                        gotVenueID = true;
                        venueID = v.getVenueID();
                        break;
                    }
                }
                if(gotVenueID) {
                    venueCallback.getVenueIDFromNameCallback(venueID);
                }
                else {
                    Toast.makeText(context, "Invalid Venue", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void sportInAvailableSports(int venueID, String sport, Context context, VenueCallback.SportInAvailableSportsCallback sportInAvailableSportsCallback) {
        this.database.child("venues").child(String.valueOf(venueID)).child("availableSports").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot availableSports) {
                boolean inAvailableSports = false;
                for(DataSnapshot s: availableSports.getChildren()) {
                    if(s.getValue(String.class).equals(sport)) {
                        inAvailableSports = true;
                        break;
                    }
                }
                if(inAvailableSports) {
                    sportInAvailableSportsCallback.sportInAvailableSportsCallback();
                }
                else {
                    Toast.makeText(context, "Invalid Sport", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // ********** For NewEvent **********

    public void newEventSubmit(EditText eventVenue, EditText eventSport, EditText eventMaxPlayers, EditText datePicker, EditText startTimePicker, EditText endTimePicker, EventPresenter eventPresenter, Context context) {
        // Check if any field is empty
        if(TextUtils.isEmpty(eventVenue.getText().toString())) {
            eventVenue.setError("Venue cannot be empty");
            return;
        }
        if(TextUtils.isEmpty(eventSport.getText().toString())) {
            eventSport.setError("Sport cannot be empty");
            return;
        }
        if(TextUtils.isEmpty(eventMaxPlayers.getText().toString())) {
            eventMaxPlayers.setError("Max Players cannot be empty");
            return;
        }
        if(TextUtils.isEmpty(datePicker.getText().toString())) {
            datePicker.setError("Date cannot be empty");
            return;
        }
        if(TextUtils.isEmpty(startTimePicker.getText().toString())) {
            startTimePicker.setError("Start Time cannot be empty");
            return;
        }
        if(TextUtils.isEmpty(endTimePicker.getText().toString())) {
            endTimePicker.setError("End Time cannot be empty");
            return;
        }

        String[] dateArr = datePicker.getText().toString().split("/");
        String[] startArr = startTimePicker.getText().toString().split(":");
        String[] endArr = endTimePicker.getText().toString().split(":");

        int maxPlayers = Integer.parseInt(eventMaxPlayers.getText().toString());
        int day = Integer.parseInt(dateArr[0]);
        int month = Integer.parseInt(dateArr[1]);
        int year = Integer.parseInt(dateArr[2]);
        int startHour = Integer.parseInt(startArr[0]);
        int startMin = Integer.parseInt(startArr[1]);
        int endHour = Integer.parseInt(endArr[0]);
        int endMin = Integer.parseInt(endArr[1]);
        String sport = eventSport.getText().toString();
        String venueName = eventVenue.getText().toString();

        // Continue if venue is valid, else will show Toast saying invalid venue (from venuePresenter)
        this.getVenueIDFromName(venueName, context, new VenueCallback.GetVenueIDFromNameCallback() {
            @Override
            public void getVenueIDFromNameCallback(int venueID) {
                Log.d("neweventthing", "Venue Name: " + venueName + ", Venue ID: " + String.valueOf(venueID));
                // Continue if sport is valid, else will show Toast saying invalid venue (from venuePresenter)
                VenuePresenter.this.sportInAvailableSports(venueID, sport, context, new VenueCallback.SportInAvailableSportsCallback() {
                    @Override
                    public void sportInAvailableSportsCallback() {
                        Event newEvent = new Event(maxPlayers, day, month, year, startHour, startMin, endHour, endMin, sport, venueID);
//
                        // Check if time is valid, start time < end time
                        if(newEvent.isValidTime()) {
                            // TODO Check not overlapping with other events
                            eventPresenter.pushEvent(newEvent);
                        }
                        else {
                            Toast.makeText(context, "Invalid Time", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void getAllVenues(VenueCallback.GetAllVenuesCallback venueCallback) {
        List<Venue> allVenues = new ArrayList<Venue>();
        this.database.child("venues").orderByChild("venueID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allVenues.clear();
                for(DataSnapshot venue: snapshot.getChildren()) {
                    Venue toAdd = venue.getValue(Venue.class);
                    allVenues.add(toAdd);
                }
                venueCallback.getAllVenuesCallback(allVenues);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
