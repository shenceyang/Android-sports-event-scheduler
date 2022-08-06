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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VenuePresenter {
    private DatabaseReference database;
    private ID id;

    public VenuePresenter(DatabaseReference database) {
        this.database = database;
        this.id = new ID(database);
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

        // Delete from allEvents
        this.database.child("allEvents").addListenerForSingleValueEvent(new ValueEventListener() {
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
        // Delete from schedules
        this.database.child("schedules").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot schedule: snapshot.getChildren()) {
                    if(schedule.child("venueID").getValue().toString().equals(String.valueOf(venueID))) {
                        schedule.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

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

    public void checkDuplicateVenue(String venueName, Context context, VenueCallback.CheckDuplicateVenueCallbackTrue checkDuplicateVenueCallbackTrue, VenueCallback.CheckDuplicateVenueCallbackFalse checkDuplicateVenueCallbackFalse) {
        this.database.child("venues").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isDuplicate = false;
                for(DataSnapshot venue: snapshot.getChildren()) {
                    if(venue.child("venueName").getValue(String.class).equals(venueName)) {
                        isDuplicate = true;
                        break;
                    }
                }
                if(!isDuplicate) {
                    checkDuplicateVenueCallbackTrue.checkDuplicateVenueCallbackTrue();
                }
                else {
                    checkDuplicateVenueCallbackFalse.checkDuplicateVenueCallbackFalse();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void eventTimeNotOverlapping(Event event, Context context, VenueCallback.EventTimeNotOverlappingCallback eventTimeNotOverlappingCallback) {
        this.database.child("allEvents").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot allEvents) {
                boolean overlap = false;
                SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                String timeStartStr = String.format("%02d", event.getStartHour()) + ":" + String.format("%02d", event.getStartMin());
                String timeEndStr = String.format("%02d", event.getEndHour()) + ":" + String.format("%02d", event.getEndMin());
                try {
                    Date timeStart = parser.parse(timeStartStr);
                    Date timeEnd = parser.parse(timeEndStr);

                    for(DataSnapshot otherEventSnapshot: allEvents.getChildren()) {
                        Event otherEvent = otherEventSnapshot.getValue(Event.class);

                        // Check for overlap in events with same venue and sport
                        if((event.getVenueID() == otherEvent.getVenueID()) && (event.getSport().equals(otherEvent.getSport())) && (event.getYear() == otherEvent.getYear()) && (event.getMonth() == otherEvent.getMonth()) && (event.getDay() == otherEvent.getDay())) {
                            // TODO check mins
                            String otherTimeStartStr = String.format("%02d", otherEvent.getStartHour()) + ":" + String.format("%02d", otherEvent.getStartMin());
                            String otherTimeEndStr = String.format("%02d", otherEvent.getEndHour()) + ":" + String.format("%02d", otherEvent.getEndMin());
                            Log.d("timething", "timeStart: " + timeStartStr + " timeEnd: " + timeEndStr + " otherTimeStart: " + otherTimeStartStr + " otherTimeEnd: " + otherTimeEndStr);
                            try {
                                Date otherTimeStart = parser.parse(otherTimeStartStr);
                                Date otherTimeEnd = parser.parse(otherTimeEndStr);
                                if(timeStart.before(otherTimeStart) && timeEnd.after(otherTimeStart)) {
                                    Log.d("timething", "overlap1");
                                    overlap = true;
                                }
                                else if((timeStart.after(otherTimeStart) || timeStart.equals(otherTimeStart)) && timeStart.before(otherTimeEnd)) {
                                    Log.d("timething", "overlap2");
                                    overlap = true;
                                }
                                else if((timeEnd.before(otherTimeEnd) || timeEnd.equals(otherTimeEnd)) && timeEnd.after(otherTimeStart)) {
                                    Log.d("timething", "overlap3");
                                    overlap = true;
                                }
                            }
                            catch (ParseException e) {
                                Log.e("DateError", "Cannot parse Date");
                            }
                        }
                    }
                    if(!overlap) {
                        eventTimeNotOverlappingCallback.eventTimeNotOverlappingCallback();
                    }
                    else {
                        Log.d("timething", "overlap toast");
                        Toast.makeText(context, "Event overlap", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (ParseException e) {
                    Log.e("DateError", "Cannot parse Date");
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
//                Log.d("neweventthing", "Venue Name: " + venueName + ", Venue ID: " + String.valueOf(venueID));
                // Continue if sport is valid, else will show Toast saying invalid venue (from venuePresenter)
                VenuePresenter.this.sportInAvailableSports(venueID, sport, context, new VenueCallback.SportInAvailableSportsCallback() {
                    @Override
                    public void sportInAvailableSportsCallback() {
                        // Get next event ID
                        id.getNextEventID(new IDCallback.GetNextEventIDCallback() {
                            @Override
                            public void getNextEventIDCallback(int nextID) {
                                Event newEvent = new Event(nextID, maxPlayers, day, month, year, startHour, startMin, endHour, endMin, sport, venueID);

                                if(newEvent.isValidMaxPlayers()) {
                                    // Check if time is valid, start time < end time
                                    if(newEvent.isValidTime()) {

                                        // Check if time overlaps other events with same venue, sport, date
                                        eventTimeNotOverlapping(newEvent, context, new VenueCallback.EventTimeNotOverlappingCallback() {
                                            @Override
                                            public void eventTimeNotOverlappingCallback() {
                                                eventPresenter.pushEvent(newEvent);
                                            }
                                        });
                                    }
                                    else {
                                        Toast.makeText(context, "Invalid Time", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(context, "Invalid Max Players", Toast.LENGTH_SHORT).show();
                                }
                            };
                        });
                    };
                });
            };
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

    public void getAvailableSports(Venue v, VenueCallback.GetAvailableSportsCallback sportsCallback) {
        List<String> availableSports = new ArrayList<>();

        database.child("venues").child(String.valueOf(v.venueID)).child("availableSports").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                availableSports.clear();
                System.out.println(snapshot.getChildren());
                for (DataSnapshot sport: snapshot.getChildren()) {
                    String toAdd = sport.getValue(String.class);
                    availableSports.add(toAdd);
                }
                sportsCallback.getAvailableSportsCallback(availableSports);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
