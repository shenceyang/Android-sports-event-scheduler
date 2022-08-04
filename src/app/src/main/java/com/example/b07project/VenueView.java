package com.example.b07project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VenueView extends AppCompatActivity {

    private VenuePresenter venuePresenter;
    private DatabaseReference database = FirebaseDatabase.getInstance("https://android-sport-app-default-rtdb.firebaseio.com/").getReference();

    private void addVenues() {

        this.venuePresenter.getAllVenues(new VenueCallback.GetAllVenuesCallback() {
        @Override
        public void getAllVenuesCallback(List<Venue> allVenues)
        {
            LinearLayout venueList = (LinearLayout) findViewById(R.id.venueList);
            for(Venue v: allVenues)
            {
                LayoutInflater inflater = getLayoutInflater();
                View newVenue = inflater.inflate(R.layout.activity_venues_template, venueList, false);

                TextView venueText = (TextView) newVenue.findViewById(R.id.venueName);

                venuePresenter.getVenue(v.getVenueID(), new VenueCallback.GetVenueCallback()
                {
                    @Override
                    public void getVenueCallback(Venue venue) {
                        venueText.setText("Venue: " + venue.getVenueName());
                    }
                });
                venueList.addView(newVenue);
            }
        }
    });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venues);
        this.venuePresenter = new VenuePresenter(this.database);
        addVenues();
    }
}
