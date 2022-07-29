package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> arr = new ArrayList<String>();
        arr.add("soccer");
        arr.add("basketball");
        Venue v = new Venue("testVenue", arr);
        Event e = new Event(4, 13, 12, 2022, 7, 0, 9, 0, 1);
        DatabaseReference d = FirebaseDatabase.getInstance("https://android-sport-app-default-rtdb.firebaseio.com/").getReference();
        VenuePresenter venuePresenter = new VenuePresenter(new VenueView(), d);
        EventPresenter eventPresenter = new EventPresenter(new EventView(), d);
        venuePresenter.pushVenue(v);
        eventPresenter.pushEvent(e);
    }
}