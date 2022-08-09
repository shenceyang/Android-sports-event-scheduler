package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class AddVenue extends AppCompatActivity {

    String name;
    ArrayList<String> sports = new ArrayList<String>();

    public void clearSports() {
        this.sports.clear();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_venue);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar8);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button submitButton = (Button) findViewById(R.id.button);
        Button addButton = (Button) findViewById(R.id.button2);
        EditText editText2 = (EditText) findViewById(R.id.editTextTextPersonName);
        EditText editText1 = (EditText) findViewById(R.id.editTextTextPersonName2);
        DatabaseReference database = FirebaseDatabase.getInstance("https://android-sport-app-default-rtdb.firebaseio.com/").getReference();

        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String sport = editText1.getText().toString();

                if (sport.equals("")){
                    Toast.makeText(AddVenue.this, "Not a valid sport", Toast.LENGTH_SHORT).show();

                }
                else if (sports.contains(sport)){
                    editText1.getText().clear();
                    Toast.makeText(AddVenue.this, sport + " has already been added", Toast.LENGTH_SHORT).show();
                }
                else {
                    sports.add(sport);
                    editText1.getText().clear();
                    Toast.makeText(AddVenue.this, "Added " + sport, Toast.LENGTH_SHORT).show();
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                name = editText2.getText().toString();
                if (name.equals("")){
                    clearSports();
                    Toast.makeText(AddVenue.this, "Invalid name, please re-add sports with valid venue name", Toast.LENGTH_LONG).show();
                }
                else if (sports.isEmpty()){
                    Toast.makeText(AddVenue.this, "Please enter at least one sport", Toast.LENGTH_SHORT).show();
                }
                else {
                    VenuePresenter venuePresenter = new VenuePresenter(database);
                    // Check for duplicate venue name
                    // Show toast if venue of same name exists
                    venuePresenter.checkDuplicateVenue(name, AddVenue.this, new VenueCallback.CheckDuplicateVenueCallbackTrue() {
                        @Override
                        public void checkDuplicateVenueCallbackTrue() {
                            ID id = new ID(database);
                            id.getNextVenueID(new IDCallback.GetNextVenueIDCallback() {
                                @Override
                                public void getNextVenueIDCallback(int nextID) {
                                    Venue venue = new Venue(nextID, name, sports);
                                    venuePresenter.pushVenue(venue);
                                    editText1.getText().clear();
                                    editText2.getText().clear();
                                    clearSports();
                                    Toast.makeText(AddVenue.this, "Added venue " + name, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }, new VenueCallback.CheckDuplicateVenueCallbackFalse() {
                        @Override
                        public void checkDuplicateVenueCallbackFalse() {
                            Toast.makeText(AddVenue.this,"Venue already exists",Toast.LENGTH_SHORT).show();
                            clearSports();
                        }
                    });
                }
            }
        });
    }
}
