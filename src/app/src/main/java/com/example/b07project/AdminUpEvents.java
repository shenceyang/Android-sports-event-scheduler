package com.example.b07project;

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

import java.util.List;

public class AdminUpEvents extends AppCompatActivity {

    private VenuePresenter venuePresenter;
    private DatabaseReference database = FirebaseDatabase.getInstance("https://android-sport-app-default-rtdb.firebaseio.com/").getReference();

    private void upcomingEvents(String venueName){

        this.venuePresenter.getAllVenues(new VenueCallback.GetAllVenuesCallback() {
            @Override
            public void getAllVenuesCallback(List<Venue> allVenues){
                LinearLayout eventList = (LinearLayout) findViewById(R.id.eventListAdmin);
                boolean venueFound = false;

                for(Venue v:allVenues){
                    if (v.getVenueName().equals(venueName)) {
                        venueFound = true;
                        if (v.events.isEmpty()){
                            Toast.makeText(AdminUpEvents.this, "Venue " + venueName + " has no upcoming events", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            for (Event e:v.events) {
                                LayoutInflater inflater = getLayoutInflater();
                                View newEvent = inflater.inflate(R.layout.upcoming_events_admin_template, eventList, false);

                                TextView eventVenue = newEvent.findViewById(R.id.eventVenueAdmin);
                                TextView eventDate = newEvent.findViewById(R.id.eventDateAdmin);
                                TextView eventSport = newEvent.findViewById(R.id.eventSportAdmin);
                                TextView eventStart = newEvent.findViewById(R.id.eventStartTimeAdmin);
                                TextView eventEnd = newEvent.findViewById(R.id.eventEndTimeAdmin);
                                TextView eventJoinedPlayers = newEvent.findViewById(R.id.eventJoinedPlayersAdmin);
                                TextView eventMaxPlayers = newEvent.findViewById(R.id.eventMaxPlayersAdmin);

                                eventVenue.setText("Venue: " + v.getVenueName());
                                eventDate.setText("Date: " + e.getDay() + "/" + e.getMonth());
                                eventSport.setText("Sport: " + e.getSport());
                                eventStart.setText("Start: " + e.getStartHour() + ":" + e.getStartMin());
                                eventEnd.setText("End: " + e.getEndHour() + ":" + e.getEndMin());
                                eventJoinedPlayers.setText("Joined Players: " + e.getCurrPlayers());
                                eventMaxPlayers.setText("Max Players: " + e.getMaxPlayers());


                                eventList.addView(newEvent);
                            }
                        }
                    }
                }
                if(!venueFound){
                    Toast.makeText(AdminUpEvents.this, "Venue " + venueName + " not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_events_admin);

        this.venuePresenter = new VenuePresenter(this.database);
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
                    upcomingEvents(venueName);
                }
            }
        });


    }
}
