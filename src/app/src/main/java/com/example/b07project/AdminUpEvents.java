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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdminUpEvents extends AppCompatActivity {

    private List<String> venueSearch = new ArrayList<String>();
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

                for (Event e:sortedEvents) {
                    if (e.getVenueID() == venueID) {
                        LayoutInflater inflater = getLayoutInflater();
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
//                        eventDate.setText("Date: " + e.getDay() + "/" + e.getMonth());
//                        eventSport.setText("Sport: " + e.getSport());
//                        eventStart.setText("Start: " + e.getStartHour() + ":" + e.getStartMin());
//                        eventEnd.setText("End: " + e.getEndHour() + ":" + e.getEndMin());
//                        eventJoinedPlayers.setText("Joined Players: " + e.getCurrPlayers());
//                        eventMaxPlayers.setText("Max Players: " + e.getMaxPlayers());

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
//                                eventList.removeView(newEvent);
                                eventPresenter.removeEvent(e.getEventID());
                                eventList.removeAllViews();
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
                    Toast.makeText(AdminUpEvents.this, "No upcoming events for " + venueName, Toast.LENGTH_SHORT).show();
                    venueSearch.remove(venueName);
                }
            }
        });
    }

    public void findVenue(String venueName){

        if (!venueSearch.contains(venueName)) {
            venueSearch.add(venueName);
            this.venuePresenter = new VenuePresenter(this.database);
            this.venuePresenter.getAllVenues(new VenueCallback.GetAllVenuesCallback() {
                @Override
                public void getAllVenuesCallback(List<Venue> allVenues) {
                    int venueID = -1;
                    for (Venue v:allVenues) {
                        if (venueName.equals(v.getVenueName())) {
                            venueID = v.getVenueID();
                            if (venueID != -1) {
                                upcomingEvents(venueName, venueID);
                            }
                            break;
                        }
                    }
                    if (venueID == -1){
                        Toast.makeText(AdminUpEvents.this, "Venue " + venueName + " does not exist", Toast.LENGTH_SHORT).show();
                        venueSearch.remove(venueName);
                    }
                }
            });
        }
        else{
            Toast.makeText(AdminUpEvents.this, "Venue " + venueName + " has already been searched for", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_events_admin);

        Button searchButton = (Button) findViewById(R.id.button4);

        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName3);

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String venueName = editText.getText().toString();
                if (venueName.equals("")){
                    Toast.makeText(AdminUpEvents.this, "Please enter venue name", Toast.LENGTH_SHORT).show();
                }
                else {
                    findVenue(venueName);
                }
            }
        });
    }
}
