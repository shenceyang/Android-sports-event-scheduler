package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

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

        Customer c = new Customer("shence","123123");
        Customer c2 = new Customer("kang","123123");

        Schedule s1 = new Schedule("basketball","shence","panam");
        Schedule s2 = new Schedule("basketball","kang","panam");

        Admin a = new Admin("sb","123");


        Event e1 = new Event(4, 13, 12, 2022, 7, 0, 9, 0, v.getVenueID());
        Event e2 = new Event(8, 10, 8, 2022, 7, 0, 9, 0, v.getVenueID());
        v.addEvent(e1);

        DatabaseReference d = FirebaseDatabase.getInstance("https://android-sport-app-default-rtdb.firebaseio.com/").getReference();
        VenuePresenter venuePresenter = new VenuePresenter(new VenueView(), d);
        EventPresenter eventPresenter = new EventPresenter(new EventView(), d);

        CustomerPresenter customerPresenter = new CustomerPresenter(new CustomerView(), d);
        SchedulePresenter schedulePresenter = new SchedulePresenter(new ScheduleView(),d);
        AdminPresenter adminPresenter = new AdminPresenter(new AdminView(),d);

        venuePresenter.pushVenue(v);

        eventPresenter.pushEvent(e);

        customerPresenter.pushCustomer(c);
        customerPresenter.pushCustomer(c2);

        schedulePresenter.pushSchedule(s1);
        schedulePresenter.pushSchedule(s2);
        adminPresenter.pushAdmin(a);

        eventPresenter.pushEvent(e2);

//        eventPresenter.removeEvent(e2.getEventID());
        venuePresenter.removeVenue(v.getVenueID());


//        venuePresenter.pullVenue(v.getVenueID());
//        Log.d("thingy", venue.getVenueName());
//        Venue pullVenue = venuePresenter.getPulledVenue();
//        Log.d("thingy", pullVenue.getVenueName());
//        Log.i("venue", String.valueOf(pullVenue.getVenueID()));
//        Log.i("venue", pullVenue.get());
    }
}