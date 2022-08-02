package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> arr = new ArrayList<String>();
        arr.add("soccer");
        arr.add("basketball");
        Venue v = new Venue("testVenue", arr);

        Event e = new Event(4, 11, 12, 2022, 7, 0, 9, 0, "basketball", v.getVenueID());

        Customer c = new Customer("shence","123123");
        Customer c2 = new Customer("kang","123123");

        Schedule s1 = new Schedule(e.getEventID(),"shence",v.getVenueID());
        Schedule s2 = new Schedule(e.getEventID(),"kang",v.getVenueID());

        Admin a = new Admin("sb","123");


        Event e1 = new Event(4, 13, 12, 2022, 7, 0, 9, 0, "basketball", v.getVenueID());
        Event e2 = new Event(8, 10, 8, 2022, 7, 0, 9, 0, "basketball", v.getVenueID());
        v.addEvent(e1);

        DatabaseReference d = FirebaseDatabase.getInstance("https://android-sport-app-default-rtdb.firebaseio.com/").getReference();
        VenuePresenter venuePresenter = new VenuePresenter(new VenueView(), d);
        EventView eventView = new EventView();
        EventPresenter eventPresenter = new EventPresenter(eventView, d);

        CustomerPresenter customerPresenter = new CustomerPresenter(new CustomerView(), d);
        SchedulePresenter schedulePresenter = new SchedulePresenter(new ScheduleView(), eventPresenter, d);
        AdminPresenter adminPresenter = new AdminPresenter(new AdminView(),d);

        venuePresenter.pushVenue(v);

        eventPresenter.pushEvent(e);

        customerPresenter.pushCustomer(c);
        customerPresenter.pushCustomer(c2);

        schedulePresenter.pushSchedule(s1);
        schedulePresenter.pushSchedule(s2);
        adminPresenter.pushAdmin(a);

        eventPresenter.pushEvent(e2);

        Intent intent = new Intent(this, EventView.class);
        startActivity(intent);

//        eventPresenter.getSortedListEvents(new EventCallback.GetSortedListEventsCallback() {
//            @Override
//            public void getSortedListEventsCallback(List<Event> sortedEvents) {
//                    // Update UI stuff
//            }
//        });

//        eventPresenter.removeEvent(e2.getEventID());
//        venuePresenter.removeVenue(v.getVenueID());


//        venuePresenter.pullVenue(v.getVenueID());
//        Log.d("thingy", venue.getVenueName());
//        Venue pullVenue = venuePresenter.getPulledVenue();
//        Log.d("thingy", pullVenue.getVenueName());
//        Log.i("venue", String.valueOf(pullVenue.getVenueID()));
//        Log.i("venue", pullVenue.get());
    }
}