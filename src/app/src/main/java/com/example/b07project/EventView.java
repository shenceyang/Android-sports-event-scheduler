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

public class EventView extends AppCompatActivity {
    private EventPresenter eventPresenter;
    private VenuePresenter venuePresenter;
    private SchedulePresenter schedulePresenter;
    private DatabaseReference database = FirebaseDatabase.getInstance("https://android-sport-app-default-rtdb.firebaseio.com/").getReference();

    private void addUpcomingEvents() {
        this.eventPresenter.getSortedListEvents(new EventCallback.GetSortedListEventsCallback() {
            @Override
            public void getSortedListEventsCallback(List<Event> sortedEvents) {
                LinearLayout eventList = (LinearLayout) findViewById(R.id.eventList);
                for(Event e: sortedEvents) {
//                    Log.d("sortedevents", String.valueOf(event.getEventID()));
                    LayoutInflater inflater = getLayoutInflater();
                    View newEvent = inflater.inflate(R.layout.upcoming_events_template, eventList, false);

                    TextView venueText = (TextView) newEvent.findViewById(R.id.eventVenue);
                    TextView dateText = (TextView) newEvent.findViewById(R.id.eventDate);
                    TextView sportText = (TextView) newEvent.findViewById(R.id.eventSport);
                    TextView startText = (TextView) newEvent.findViewById(R.id.eventStartTime);
                    TextView endText = (TextView) newEvent.findViewById(R.id.eventEndTime);
                    TextView joinedPlayersText = (TextView) newEvent.findViewById(R.id.eventJoinedPlayers);
                    TextView maxPlayersText = (TextView) newEvent.findViewById(R.id.eventMaxPlayers);
                    Button joinButton = (Button) newEvent.findViewById(R.id.joinButton);

                    venuePresenter.getVenue(e.getVenueID(), new VenueCallback() {
                        @Override
                        public void getVenueCallback(Venue venue) {
                            venueText.setText("Venue: " + venue.getVenueName());
                        }
                    });
                    eventPresenter.getEvent(e.getEventID(), new EventCallback.GetEventCallback() {
                        @Override
                        public void getEventCallback(Event event) {
                            dateText.setText("Date: " + event.getDay() + "/" + event.getMonth() + "/" + event.getYear());
                            sportText.setText("Sport: " + event.getSport());
                            startText.setText("Start: " + event.getStartHour() + ":" + String.format("%02d", event.getStartMin()));
                            endText.setText("End: " + event.getEndHour() + ":" + String.format("%02d", event.getEndMin()));
                            joinedPlayersText.setText("Players joined: " + event.getCurrPlayers());
                            maxPlayersText.setText("Max Players: " + event.getMaxPlayers());

//                            joinButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    Schedule s = new Schedule(event.getEventID(), customerID, venueID) // TODO change
//                                }
//                            });
                        }
                    });



                    eventList.addView(newEvent);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_events);

        this.eventPresenter = new EventPresenter(this.database);
        this.venuePresenter = new VenuePresenter(this.database); // TODO Change VenueView later
        this.schedulePresenter = new SchedulePresenter(new ScheduleView(), this.eventPresenter, this.database); // TODO change
        addUpcomingEvents();
    }


}
