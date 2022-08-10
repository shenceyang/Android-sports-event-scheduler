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

public class AdminVenueView extends AppCompatActivity {

    private VenuePresenter venuePresenter;
    private DatabaseReference database = FirebaseDatabase.getInstance("https://android-sport-app-default-rtdb.firebaseio.com/").getReference();

    private void addVenues() {

        this.venuePresenter.getAllVenues(new VenueCallback.GetAllVenuesCallback() {
        @Override
        public void getAllVenuesCallback(List<Venue> allVenues)
        {
            LinearLayout venueList = (LinearLayout) findViewById(R.id.adminVenueList);
            for(Venue v: allVenues)
            {
                LayoutInflater inflater = getLayoutInflater();
                View newVenue = inflater.inflate(R.layout.admin_activity_venues_template, venueList, false);

                TextView venueText = (TextView) newVenue.findViewById(R.id.adminVenueName);
                TextView availableSportsText = (TextView) newVenue.findViewById(R.id.adminAvailableSports);


                venuePresenter.getAvailableSports(v, new VenueCallback.GetAvailableSportsCallback() {
                    @Override
                    public void getAvailableSportsCallback(List<String> sports) {
                        for(String sport : sports)
                        {
                            availableSportsText.append(sport + "\n");
                        }
                    }
                });



                venuePresenter.getVenue(v.getVenueID(), new VenueCallback.GetVenueCallback()
                {
                    @Override
                    public void getVenueCallback(Venue venue) {
                        venueText.setText(venue.getVenueName());
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
        setContentView(R.layout.admin_activity_venues);
        this.venuePresenter = new VenuePresenter(this.database);
        addVenues();
    }
}
