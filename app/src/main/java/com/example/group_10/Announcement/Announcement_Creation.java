package com.example.group_10.Announcement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group_10.MainActivity;
import com.example.group_10.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Announcement_Creation extends AppCompatActivity {
    Announcement_Unit announce;
    private Context mContext;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_announcement);
        userID = getIntent().getExtras().get("user_id").toString();
        backToMain();
        saveData();
        textChangedEvents();
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            initAnnouncement(extras.getInt("announceId"));
        }
        else {
            announce = new Announcement_Unit();
        }
    }

    private void textChangedEvents(){
        final EditText title = findViewById(R.id.announcement_title);
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                announce.setTitle(title.getText().toString());
            }
        });
        final EditText content = findViewById(R.id.announcement_content);
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                announce.setContent(content.getText().toString());
            }
        });
    }

    public void backToMain(){
        Button back = findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Announcement_Creation.this,MainActivity.class);
                intent.putExtra("user_id",userID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    public void saveData(){
        Button save = findViewById(R.id.confirm_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDate();
                boolean wasSuccessful;
                AnnouncementDataSource ds = new AnnouncementDataSource(Announcement_Creation.this);
                try {
                    ds.open();
                    if (announce.getAnnounceId() == -1) {
                        wasSuccessful = ds.insertAnnouncement(announce);
                        if (wasSuccessful) {
                            int newId = ds.getLastAnnouncementId();
                            announce.setAnnounceId(newId);
                        }
                    } else {
                        wasSuccessful = ds.updateAnnouncement(announce);
                    }
                    ds.close();
                }
                catch (Exception e){
                    wasSuccessful = false;
                }
            }
        });
    }

    private void updateDate() {
        SimpleDateFormat formatter;
        Date date = new Date();

        formatter = new SimpleDateFormat("yyyy");
        announce.setYear(Integer.parseInt(formatter.format(date)));
        formatter = new SimpleDateFormat("MM");
        announce.setMonth(Integer.parseInt(formatter.format(date)));
        formatter = new SimpleDateFormat("dd");
        announce.setDay(Integer.parseInt(formatter.format(date)));
        formatter = new SimpleDateFormat("HH");
        announce.setHour(Integer.parseInt(formatter.format(date)));
        formatter = new SimpleDateFormat("mm");
        announce.setMinute(Integer.parseInt(formatter.format(date)));
        formatter = new SimpleDateFormat("ss");
        announce.setSecond(Integer.parseInt(formatter.format(date)));
        formatter = new SimpleDateFormat("MMddHHmmss");
        announce.setTime(Integer.parseInt(formatter.format(date)));
    }

    private void initAnnouncement(int id){
        AnnouncementDataSource ds = new AnnouncementDataSource(Announcement_Creation.this);
        try {
            ds.open();
            announce = ds.getSpecificAnnouncement(id);
            ds.close();
        }
        catch (Exception e){
            Toast.makeText(this, "Load Contact Failed", Toast.LENGTH_LONG).show();
        }
    }
}
