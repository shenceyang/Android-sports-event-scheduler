package com.example.b07project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdminUpEvents extends AppCompatActivity {

    private String lastSearched = "";
    private VenuePresenter venuePresenter;
    private EventPresenter eventPresenter;
    private DatabaseReference database = FirebaseDatabase.getInstance("https://android-sport-app-default-rtdb.firebaseio.com/").getReference();

    private void upcomingEvents(String venueName, int venueID){

        this.eventPresenter = new EventPresenter(this.database);
        this.eventPresenter.getSortedListEvents(new EventCallback.GetSortedListEventsCallback() {
            @Override
            public void getSortedListEventsCallback(List<Event> sortedEvents) {
                LinearLayout eventList = (LinearLayout) findViewById(R.id.eventListAdmin);
                eventList.removeAllViews();

                boolean hasEvent = false;
                LayoutInflater inflater = getLayoutInflater();

                for (Event e:sortedEvents) {

                    if (e.getVenueID() == venueID) {
                        View newEvent = inflater.inflate(R.layout.upcoming_events_admin_template, eventList, false);

                        hasEvent = true;

                        TextView eventVenue = newEvent.findViewById(R.id.eventVenueAdmin);
                        TextView eventDate = newEvent.findViewById(R.id.eventDateAdmin);
                        TextView eventSport = newEvent.findViewById(R.id.eventSportAdmin);
                        TextView eventStart = newEvent.findViewById(R.id.eventStartTimeAdmin);
                        TextView eventEnd = newEvent.findViewById(R.id.eventEndTimeAdmin);
                        TextView eventJoinedPlayers = newEvent.findViewById(R.id.eventJoinedPlayersAdmin);
                        TextView eventMaxPlayers = newEvent.findViewById(R.id.eventMaxPlayersAdmin);
                        Button deleteEventButton = newEvent.findViewById(R.id.button7);
                        Button deleteVenueButton = newEvent.findViewById(R.id.button8);

                        eventVenue.setText("Venue: " + venueName);
                        eventDate.setText("Date: " + e.getDay() + "/" + e.getMonth() + "/" + e.getYear());
                        eventSport.setText("Sport: " + e.getSport());
                        eventStart.setText("Start: " + e.getStartHour() + ":" + String.format("%02d", e.getStartMin()));
                        eventEnd.setText("End: " + e.getEndHour() + ":" + String.format("%02d", e.getEndMin()));
                        eventJoinedPlayers.setText("Joined Players: " + e.getCurrPlayers());
                        eventMaxPlayers.setText("Max Players: " + e.getMaxPlayers());

                        eventList.addView(newEvent);

                        deleteEventButton.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                Toast.makeText(AdminUpEvents.this, "Deleting event " + e.getEventID(), Toast.LENGTH_SHORT).show();
                                eventList.removeView(newEvent);
                                eventPresenter.removeEvent(e.getEventID());
                                upcomingEvents(venueName, venueID);
                            }
                        });

                        deleteVenueButton.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                Toast.makeText(AdminUpEvents.this, "Deleting venue " + venueName, Toast.LENGTH_SHORT).show();
                                eventList.removeAllViews();
                                venuePresenter.removeVenue(e.getVenueID());
                            }
                        });

                    }
                }
                if(!hasEvent){

                    View newNoEvent = inflater.inflate(R.layout.no_events_admin_template, eventList, false);
                    TextView noEventVenue = newNoEvent.findViewById(R.id.noEventVenueAdmin);
                    Button deleteVenueButton = newNoEvent.findViewById(R.id.button9);
                    noEventVenue.setText("Venue: " + venueName);

                    eventList.addView(newNoEvent);

                    deleteVenueButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            Toast.makeText(AdminUpEvents.this, "Deleting venue " + venueName, Toast.LENGTH_SHORT).show();
                            eventList.removeAllViews();
                            venuePresenter.removeVenue(venueID);
                        }
                    });

                    lastSearched = "";
                }
            }
        });
    }

    public void findVenue(String venueName){

        if (venueName != lastSearched) {
            this.venuePresenter = new VenuePresenter(this.database);
            this.venuePresenter.getAllVenues(new VenueCallback.GetAllVenuesCallback() {
                @Override
                public void getAllVenuesCallback(List<Venue> allVenues) {
                    int venueID = -1;
                    for (Venue v:allVenues) {
                        if (venueName.equals(v.getVenueName())) {
                            venueID = v.getVenueID();
                            if (venueID != -1) {
                                lastSearched = venueName;
                                upcomingEvents(venueName, venueID);
                            }
                            break;
                        }
                    }
                    if (venueID == -1){
                        Toast.makeText(AdminUpEvents.this, "Venue " + venueName + " does not exist", Toast.LENGTH_SHORT).show();
                        //venueSearch.remove(venueName);
                    }
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_events_admin);

        Button searchButton = (Button) findViewById(R.id.button4);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar7);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName3);

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String venueName = editText.getText().toString();
                if (venueName.equals("")){
                    Toast.makeText(AdminUpEvents.this, "Please enter venue name", Toast.LENGTH_SHORT).show();
                }
                else {
                    editText.getText().clear();
                    findVenue(venueName);
                }
            }
        });
    }
}
