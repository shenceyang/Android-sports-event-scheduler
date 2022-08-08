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
    private String userID;
    private DatabaseReference database = FirebaseDatabase.getInstance("https://android-sport-app-default-rtdb.firebaseio.com/").getReference();
    private void addSchedule() {
        this.schedulePresenter.getAllSchedules(new ScheduleCallback.GetAllSchedulesCallback(){
            @Override
            public void getAllSchedulesCallBack(List<Schedule> allSchedules) {
                LinearLayout schList = (LinearLayout) findViewById(R.id.schedule);
                for (Schedule schedule: allSchedules){
                    LayoutInflater inflater = getLayoutInflater();
                    View newSchedule = inflater.inflate(R.layout.activity_schedule_template, schList, false);

                    TextView venueText = (TextView) newSchedule.findViewById(R.id.schVenue);
                    TextView dateText = (TextView) newSchedule.findViewById(R.id.schDate);
                    TextView sportText = (TextView) newSchedule.findViewById(R.id.schEvent);

                    if (schedule.getUserID() == userID){
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
                }
            }

        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        this.eventPresenter = new EventPresenter(this.database);
        this.venuePresenter = new VenuePresenter(this.database);
        this.schedulePresenter = new SchedulePresenter(this.eventPresenter, this.database);
        this.userID = getIntent().getStringExtra("userID");
        addSchedule();

    }
}
