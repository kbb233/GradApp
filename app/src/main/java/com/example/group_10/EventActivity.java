package com.example.group_10;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_10.Event.EventAdapter;
import com.example.group_10.Event.EventDataSource;
import com.example.group_10.Event.Event_Creation;
import com.example.group_10.Event.Event_Unit;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {
    RecyclerView mRecycleView;
    EventAdapter mAdapter;
    ArrayList<Event_Unit> eventList;
    private String userID;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int eventId = eventList.get(position).getEventId();
            Intent intent = new Intent(EventActivity.this, Event_Creation.class);
            intent.putExtra("eventId", eventId);
            startActivity(intent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        userID = getIntent().getExtras().get("user_id").toString();
        initMainbtn();
        initChatbtn();
        createEvent();
        initDeleteSwitch();
        initProfilebtn();
    }
    public void onResume() {
        super.onResume();
        String sortBy = getSharedPreferences("MyEventListPreferences", Context.MODE_PRIVATE).getString("sortfield","title");
        String sortOrder = getSharedPreferences("MyEventListPreferences", Context.MODE_PRIVATE).getString("sortorder","ASC");
        EventDataSource ds = new EventDataSource(this);
        try{
            ds.open();
            eventList = ds.getEvents(sortBy,sortOrder);
            ds.close();
            if(eventList.size()>0){
                mRecycleView = findViewById(R.id.event_recycle);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                mRecycleView.setLayoutManager(layoutManager);
                mAdapter = new EventAdapter(eventList,this);
                mAdapter.setOnItemClickListener(onItemClickListener);
                mRecycleView.setAdapter(mAdapter);
            }
        }catch (Exception e){

        }
    }
    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switch_delete_event);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean status = compoundButton.isChecked();
                mAdapter.setDelete(status);
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    public void createEvent(){
        FloatingActionButton cre = findViewById(R.id.add_event_btn);
        cre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this, Event_Creation.class);
                intent.putExtra("user_id",userID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    public void initMainbtn(){
        ImageButton mainbtn = findViewById(R.id.Main_Button);
        mainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this,MainActivity.class);
                intent.putExtra("user_id",userID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    public void initChatbtn(){
        ImageButton chatbtn = findViewById(R.id.Chat_Button);
        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this,ChatActivity.class);
                intent.putExtra("user_id",userID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    public void initProfilebtn(){
        ImageButton profilebtn = findViewById(R.id.Profile_Button);
        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this,Profile.class);
                intent.putExtra("user_id",userID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
