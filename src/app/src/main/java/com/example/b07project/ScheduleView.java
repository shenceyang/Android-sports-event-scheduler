package com.example.b07project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ScheduleView extends AppCompatActivity {
    private EventPresenter eventPresenter;
    private VenuePresenter venuePresenter;
    private SchedulePresenter schedulePresenter;
    private DatabaseReference database = FirebaseDatabase.getInstance("https://android-sport-app-default-rtdb.firebaseio.com/").getReference();
    private String userID;

    private void addUpcomingEvents() {
        this.eventPresenter.getSortedListEvents(new EventCallback.GetSortedListEventsCallback() {
            @Override
            public void getSortedListEventsCallback(List<Event> sortedEvents) {
                LinearLayout eventList = (LinearLayout) findViewById(R.id.eventList);
                for(Event event: sortedEvents) {
//                    Log.d("sortedevents", String.valueOf(event.getEventID()));
                    LayoutInflater inflater = getLayoutInflater();
                    View newEvent = inflater.inflate(R.layout.upcoming_events_template, eventList, false);

                    TextView venueText = (TextView) newEvent.findViewById(R.id.eventVenue);
                    TextView dateText = (TextView) newEvent.findViewById(R.id.eventDate);
                    TextView sportText = (TextView) newEvent.findViewById(R.id.eventSport);


                    venuePresenter.getVenue(event.getVenueID(), new VenueCallback.GetVenueCallback() {
                        @Override
                        public void getVenueCallback(Venue venue) {
                            venueText.setText("Venue: " + venue.getVenueName());
                        }
                    });
                    dateText.setText("Date: " + event.getDay() + "/" + event.getMonth() + "/" + event.getYear());
                    sportText.setText("Sport: " + event.getSport());


                    // Check if current user already joined event
                    schedulePresenter.isScheduled(userID, event.getEventID(), new ScheduleCallback.IsScheduledCallback() {
                        @Override
                        public void isScheduledCallback() {
                        }
                    });

                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venues);
    }}
