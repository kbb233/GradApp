package com.example.group_10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.group_10.Announcement.AnnouncementAdapter;
import com.example.group_10.Announcement.AnnouncementDataSource;
import com.example.group_10.Announcement.Announcement_Creation;
import com.example.group_10.Announcement.Announcement_Unit;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecycleView;
    AnnouncementAdapter mAdapter;
    ArrayList<Announcement_Unit> announceList;
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int announceId = announceList.get(position).getAnnounceId();
            Intent intent = new Intent(MainActivity.this, Announcement_Creation.class);
            intent.putExtra("announceId", announceId);
            startActivity(intent);
        }
    };

    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userID = getIntent().getExtras().get("user_id").toString();

        createAnnouncement();
        initDeleteSwitch();
        initEventbtn();
        initChatbtn();
        initProfilebtn();
    }

    public void onResume(){
        super.onResume();

        String sortBy = getSharedPreferences("MyAnnouncementListPreferences", Context.MODE_PRIVATE).getString("sortfield","time");
        String sortOrder = getSharedPreferences("MyAnnouncementListPreferences", Context.MODE_PRIVATE).getString("sortorder","DESC");
        AnnouncementDataSource ds = new AnnouncementDataSource(this);
        try{
            ds.open();
            announceList = ds.getAnnouncements(sortBy,sortOrder);
            ds.close();
            if(announceList.size()>0){
                mRecycleView = findViewById(R.id.announcement_recycle);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                mRecycleView.setLayoutManager(layoutManager);
                mAdapter = new AnnouncementAdapter(announceList,this);
                mAdapter.setOnItemClickListener(onItemClickListener);
                mRecycleView.setAdapter(mAdapter);
            }
        }
        catch (Exception e){

        }

    }

    public void createAnnouncement(){
        FloatingActionButton cre = findViewById(R.id.add_announcement_btn);
        cre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Announcement_Creation.class);
                intent.putExtra("user_id",userID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switch_delete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean status = compoundButton.isChecked();
                mAdapter.setDelete(status);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void initEventbtn(){
        ImageButton eventbtn = findViewById(R.id.Event_Button);
        eventbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,EventActivity.class);
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
                Intent intent = new Intent(MainActivity.this,ChatActivity.class);
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
                Intent intent = new Intent(MainActivity.this,Profile.class);
                intent.putExtra("user_id",userID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}