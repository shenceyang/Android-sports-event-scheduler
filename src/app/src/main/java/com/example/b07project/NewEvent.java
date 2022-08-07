package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NewEvent extends AppCompatActivity {
    private EventPresenter eventPresenter;
    private VenuePresenter venuePresenter;
    private SchedulePresenter schedulePresenter;
    private DatabaseReference database = FirebaseDatabase.getInstance("https://android-sport-app-default-rtdb.firebaseio.com/").getReference();
    private String userID;

    final Calendar calendar = Calendar.getInstance();
    private Spinner eventVenue;
    private Spinner eventSport;
    private Button getSportsButton;
    private EditText eventMaxPlayers;
    private EditText datePicker;
    private EditText startTimePicker;
    private EditText endTimePicker;
    private Button submitButton;

    // ********** Date Picker **********

    private String getDateText() {
        String format = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CANADA);
        return dateFormat.format(calendar.getTime());
    }

    private void selectDateListener() {
        // When user selects date and press OK
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(calendar.DAY_OF_MONTH, day);

                datePicker.setText(getDateText());
            }
        };

        // On Click set date area
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(NewEvent.this, onDateSetListener, year, month, day);
                // Events can only be set starting tomorrow
                Calendar tomorrow = Calendar.getInstance();
                tomorrow.add(Calendar.DATE, 1);

                dpd.getDatePicker().setMinDate(tomorrow.getTimeInMillis());
                dpd.show();
            }
        });
    }

    // ********** Time Picker **********

    private String getTimeText(Calendar currentTime) {
        String format = "HH:mm";
        SimpleDateFormat timeFormat = new SimpleDateFormat(format, Locale.CANADA);
        return timeFormat.format(currentTime.getTime());
    }

    private void selectTimeListener(EditText timePicker) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                currentTime.set(Calendar.HOUR_OF_DAY, hour);
                currentTime.set(Calendar.MINUTE, minute);

                timePicker.setText(getTimeText(currentTime));
            }
        };

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewEvent.this, onTimeSetListener, hour, minute, false);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });
    }

    // ********** Sport Spinner **********
    private void initSportSpinner() {
        List<String> initSports = new ArrayList<String>();
        initSports.add("None");
        ArrayAdapter<String> initSportsAdapter = new ArrayAdapter<String>(NewEvent.this, android.R.layout.simple_spinner_item, initSports);
        initSportsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSport.setAdapter(initSportsAdapter);
    }

    // ********** Main **********

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        this.eventPresenter = new EventPresenter(this.database);
        this.venuePresenter = new VenuePresenter(this.database);
        this.schedulePresenter = new SchedulePresenter(eventPresenter, this.database);
        this.userID = getIntent().getStringExtra("userID");

        this.eventVenue = (Spinner) findViewById(R.id.venue_spinner);
        this.eventSport = (Spinner) findViewById(R.id.sport_spinner);
        this.getSportsButton = (Button) findViewById(R.id.get_sports);
        this.eventMaxPlayers = (EditText) findViewById(R.id.max_players_prompt);
        this.datePicker = (EditText) findViewById(R.id.date_picker);
        this.startTimePicker = (EditText) findViewById(R.id.start_time_picker);
        this.endTimePicker = (EditText) findViewById(R.id.end_time_picker);
        this.submitButton = (Button) findViewById(R.id.new_event_submit);

        // Get venue names List
        venuePresenter.getVenueNamesList(new VenueCallback.GetVenueNamesListCallback() {
            @Override
            public void getVenueNamesListCallback(List<String> venueNames) {
                // If no venues, just exit
                if(venueNames.isEmpty()) {
                    Toast.makeText(NewEvent.this, "No venues, cannot create events", Toast.LENGTH_SHORT).show();
                    finish();
                }

                ArrayAdapter<String> venueAdapter = new ArrayAdapter<String>(NewEvent.this, android.R.layout.simple_spinner_item, venueNames);
                venueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                eventVenue.setAdapter(venueAdapter);

                eventVenue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long itemId) {
                        String venueName = eventVenue.getSelectedItem().toString();
                        venuePresenter.getVenueIDFromName(venueName, NewEvent.this, new VenueCallback.GetVenueIDFromNameCallback() {
                            @Override
                            public void getVenueIDFromNameCallback(int venueID) {
                                venuePresenter.getAvailableSports(venueID, new VenueCallback.GetAvailableSportsCallback() {
                                    @Override
                                    public void getAvailableSportsCallback(List<String> sports) {
                                        ArrayAdapter<String> sportsAdapter = new ArrayAdapter<String>(NewEvent.this, android.R.layout.simple_spinner_item, sports);
                                        sportsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        eventSport.setAdapter(sportsAdapter);
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });



//        initSportSpinner();
//
//        this.getSportsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String venueName = eventVenue.getSelectedItem().toString();
//                venuePresenter.getVenueIDFromName(venueName, NewEvent.this, new VenueCallback.GetVenueIDFromNameCallback() {
//                    @Override
//                    public void getVenueIDFromNameCallback(int venueID) {
//                        venuePresenter.getAvailableSports(venueID, new VenueCallback.GetAvailableSportsCallback() {
//                            @Override
//                            public void getAvailableSportsCallback(List<String> sports) {
//                                ArrayAdapter<String> sportsAdapter = new ArrayAdapter<String>(NewEvent.this, android.R.layout.simple_spinner_item, sports);
//                                sportsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                eventSport.setAdapter(sportsAdapter);
//                            }
//                        });
//                    }
//                });
//            }
//        });

        selectDateListener();
        selectTimeListener(this.startTimePicker);
        selectTimeListener(this.endTimePicker);

        // On Submit Button Click
        this.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                venuePresenter.newEventSubmit(eventVenue, eventSport, eventMaxPlayers, datePicker, startTimePicker, endTimePicker, eventPresenter, NewEvent.this, userID, schedulePresenter);
            }
        });
    }
}