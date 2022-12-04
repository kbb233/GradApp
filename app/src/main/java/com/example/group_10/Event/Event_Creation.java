package com.example.group_10.Event;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.group_10.EventActivity;
import com.example.group_10.R;

public class Event_Creation extends AppCompatActivity implements DatePickerFragment.SaveDateListener , TimePickerFragment.SaveTimeListener{
    private Event_Unit currentEvent;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        userID = getIntent().getExtras().get("user_id").toString();
        backToMain();
        saveData();
        textChangedEvents();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            initEvent(extras.getInt("eventId"));
        }
        else {
            currentEvent = new Event_Unit();
        }
    }

    private void initEvent(int eventId) {
        EventDataSource ds = new EventDataSource(Event_Creation.this);
        try {
            ds.open();
            currentEvent = ds.getSpecificEvent(eventId);
            ds.close();
        }
        catch (Exception e){
            Toast.makeText(this, "Load Event Failed", Toast.LENGTH_LONG).show();
        }
    }

    private void textChangedEvents(){
        final EditText title = findViewById(R.id.event_title);
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                currentEvent.setTitle(title.getText().toString());
            }
        });
        final EditText content = findViewById(R.id.event_content);
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                currentEvent.setContent(content.getText().toString());
            }
        });
    }

    public void saveData(){
        Button save = findViewById(R.id.confirm_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean wasSuccessful;
                EventDataSource ds = new EventDataSource(Event_Creation.this);
                try {
                    ds.open();
                    if (currentEvent.getEventId() == -1) {
                        wasSuccessful = ds.insertEvent(currentEvent);
                        if (wasSuccessful) {
                            int newId = ds.getLastEventId();
                            currentEvent.setEventId(newId);
                        }
                    } else {
                        wasSuccessful = ds.updateEvent(currentEvent);
                    }
                    ds.close();
                }
                catch (Exception e){
                    wasSuccessful = false;
                }
            }
        });
    }

    @Override
    public void didFinishDatePickerDialog(int year,int month,int day){
        currentEvent.setYear(year);
        currentEvent.setMonth(month);
        currentEvent.setDay(day);
    }
    @Override
    public void didFinishTimePickerDialog(int hour,int minute){
        currentEvent.setMinute(minute);
        currentEvent.setHour(hour);
    }


    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void backToMain(){
        Button back = findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Event_Creation.this, EventActivity.class);
                intent.putExtra("user_id",userID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
