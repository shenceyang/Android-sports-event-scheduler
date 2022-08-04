package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

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
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_venue);

        Button submitButton = (Button) findViewById(R.id.button);
        Button addButton = (Button) findViewById(R.id.button2);
        Button backButton = (Button) findViewById(R.id.button3);
        EditText editText2 = (EditText) findViewById(R.id.editTextTextPersonName);
        EditText editText1 = (EditText) findViewById(R.id.editTextTextPersonName2);
        DatabaseReference database = FirebaseDatabase.getInstance("https://android-sport-app-default-rtdb.firebaseio.com/").getReference();

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(AddVenue.this, Admin_center.class); // REPLACE something WITH REDIRECTION AFTER SUBMIT AND UNCOMMENT startActivity
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String sport = editText1.getText().toString();
                sports.add(sport);
                editText1.getText().clear();
                Toast.makeText(AddVenue.this, "Added " + sport, Toast.LENGTH_SHORT).show();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                name = editText2.getText().toString();
                if (name.equals("")){
                    Toast.makeText(AddVenue.this, "Please enter venue name", Toast.LENGTH_SHORT).show();
                }
                else if (sports.isEmpty()){
                    Toast.makeText(AddVenue.this, "Please enter at least one sport", Toast.LENGTH_SHORT).show();
                }
                else {
                    Venue venue = new Venue(name, sports);
                    VenuePresenter venuePresenter = new VenuePresenter(database);
                    venuePresenter.pushVenue(venue);
                    editText1.getText().clear();
                    editText2.getText().clear();
                    sports.clear();
                    Toast.makeText(AddVenue.this, "Added venue " + name, Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(this, something.class); // REPLACE something WITH REDIRECTION AFTER SUBMIT AND UNCOMMENT startActivity
                    //startActivity(intent);
                }
            }
        });
    }
}
