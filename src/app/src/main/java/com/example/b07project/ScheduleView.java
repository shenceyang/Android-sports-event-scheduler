package com.example.b07project;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        this.schedulePresenter.getAllSchedules(userID, new ScheduleCallback.GetAllSchedulesCallback(){
            @Override
            public void getAllSchedulesCallBack(List<Schedule> allSchedules) {
                if(allSchedules.isEmpty()) {
                    Toast.makeText(ScheduleView.this, "No Joined Events", Toast.LENGTH_SHORT).show();
                    finish();
                }

                LinearLayout schList = (LinearLayout) findViewById(R.id.schedule);
                schList.removeAllViews();

                for (Schedule schedule: allSchedules){
                    LayoutInflater inflater = getLayoutInflater();
                    View newSchedule = inflater.inflate(R.layout.activity_schedule_template, schList, false);

                    TextView venueText = (TextView) newSchedule.findViewById(R.id.eventVenue);
                    TextView dateText = (TextView) newSchedule.findViewById(R.id.eventDate);
                    TextView sportText = (TextView) newSchedule.findViewById(R.id.eventSport);
                    TextView startText = (TextView) newSchedule.findViewById(R.id.eventStartTime);
                    TextView endText = (TextView) newSchedule.findViewById(R.id.eventEndTime);
                    TextView joinedPlayersText = (TextView) newSchedule.findViewById(R.id.eventJoinedPlayers);
                    TextView maxPlayersText = (TextView) newSchedule.findViewById(R.id.eventMaxPlayers);
                    Button deleteButton = (Button) newSchedule.findViewById(R.id.deleteButton);
                    
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
                            startText.setText("Start: " + event.getStartHour() + ":" + String.format("%02d", event.getStartMin()));
                            endText.setText("End: " + event.getEndHour() + ":" + String.format("%02d", event.getEndMin()));
                            joinedPlayersText.setText("Players joined: " + event.getCurrPlayers());
                            maxPlayersText.setText("Max Players: " + event.getMaxPlayers());
                        }
                    });
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            schedulePresenter.removeSchedule(schedule.getScheduleID());
                            Toast.makeText(ScheduleView.this, "Joined Schedule Deleted", Toast.LENGTH_SHORT).show();
                            schList.removeAllViews();
                            addSchedule();
                        }
                    });

                    schList.addView(newSchedule);
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
