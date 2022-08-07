package com.example.b07project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
    private int Id;

    private void addSchedule() {
        Id++;
        this.schedulePresenter.getSchedule(Id, new ScheduleCallback.GetScheduleCallback(){
            @Override
            public void getScheduleCallback(Schedule schedule) {
                LinearLayout sch = (LinearLayout) findViewById(R.id.schedule);
                LayoutInflater inflater = getLayoutInflater();
                View newSchedule = inflater.inflate(R.layout.activity_schedule_template, sch, false);

                TextView venueText = (TextView) newSchedule.findViewById(R.id.schVenue);
                TextView dateText = (TextView) newSchedule.findViewById(R.id.schDate);
                TextView sportText = (TextView) newSchedule.findViewById(R.id.schEvent);

                venuePresenter.getVenue(schedule.getVenueID(), new VenueCallback.GetVenueCallback() {
                    @Override
                    public void getVenueCallback(Venue venue) {
                        venueText.setText("Venue: " + venue.getVenueName());
                    }
                });
                eventPresenter.getEvent(schedule.getEventID(), new EventCallback.GetEventCallback() {
                    @Override
                    public void getEventCallback(Event event) {
                        dateText.setText("Date: " + event.getDay() + "/" + event.getMonth() + "/" + event.getYear());
                        sportText.setText("Sport: " + event.getSport());
                    }
                });

            }

        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        this.schedulePresenter = new SchedulePresenter(this.eventPresenter, this.database);
        addSchedule();

    }
}
