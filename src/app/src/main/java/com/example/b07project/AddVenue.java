package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class AddVenue extends AppCompatActivity {

    String name;
    ArrayList<String> sports = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_venue);

        Button addButton = (Button) findViewById(R.id.button2);
        EditText editText1 = (EditText) findViewById(R.id.editTextTextPersonName2);
        EditText editText2 = (EditText) findViewById(R.id.editTextTextPersonName);

        addButton.setOnClickListener( new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        String sport = editText1.getText().toString();
                        sports.add(sport);
                        editText1.getText().clear();
                    }
                });

        Button submitButton = (Button) findViewById(R.id.button);
        submitButton.setOnClickListener( new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        //Intent intent = new Intent(this, something.class); // REPLACE something WITH REDIRECTION AFTER SUBMIT AND UNCOMMENT startActivity
                        name = editText2.getText().toString();
                        Venue venue = new Venue(name, sports);
                        DatabaseReference d = FirebaseDatabase.getInstance("https://android-sport-app-default-rtdb.firebaseio.com/").getReference();
                        VenuePresenter venuePresenter = new VenuePresenter(new VenueView(), d);
                        venuePresenter.pushVenue(venue);
                        //startActivity(intent);
                    }
                });
    }

    // called when user presses ADD button
    /*public void addSport(View view){
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName2);
        String sport = editText.getText().toString();
        sports.add(sport);
        editText.getText().clear();
    }

    // called when user pressed SUBMIT button
    public void createVenue(View view){
        //Intent intent = new Intent(this, something.class); // REPLACE something WITH REDIRECTION AFTER SUBMIT AND UNCOMMENT startActivity
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        name = editText.getText().toString();
        Venue venue = new Venue(name, sports);
        DatabaseReference d = FirebaseDatabase.getInstance("https://android-sport-app-default-rtdb.firebaseio.com/").getReference();
        VenuePresenter venuePresenter = new VenuePresenter(new VenueView(), d);
        venuePresenter.pushVenue(venue);
        //startActivity(intent);
    }*/
}
