package com.example.group_10;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChatActivity extends AppCompatActivity {
    private Button addButton;
    private EditText room_name;

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();
    private String userID;
    private FirebaseDatabase database;
    private DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userID = getIntent().getExtras().get("user_id").toString();

        initProfilebtn();
        initMainbtn();
        initEventbtn();
        database = FirebaseDatabase.getInstance();
        root = database.getReference().getRoot().child("Rooms");

        addButton = (Button) findViewById(R.id.addRoomButton);
        room_name = (EditText) findViewById(R.id.inputRoomName);
        listView = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list_of_rooms);

        listView.setAdapter(arrayAdapter);



         /*
        When the addButton button is clicked, it will send the room to the database.
         */
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put(room_name.getText().toString(),"");
                root.updateChildren(map);
            }
        });

        /*
        This function will update the ListView each time we add a room to the database. Think of it as a 'refresh'.
         */
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = snapshot.getChildren().iterator();

                while(i.hasNext()) {
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                arrayAdapter.notifyDataSetChanged();
                room_name.setText("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        This function will take you to the room you selected in the ListView by starting an Intent.
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), ChatRoom.class);
                intent.putExtra("room_name",((TextView)view).getText().toString());
                intent.putExtra("user_id",userID);
                startActivity(intent);
            }
        });
    }


    public void initMainbtn(){
        ImageButton mainbtn = findViewById(R.id.Main_Button);
        mainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this,MainActivity.class);
                intent.putExtra("user_id",userID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    public void initEventbtn(){
        ImageButton eventbtn = findViewById(R.id.Event_Button);
        eventbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this,EventActivity.class);
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
                Intent intent = new Intent(ChatActivity.this,Profile.class);
                intent.putExtra("user_id",userID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
