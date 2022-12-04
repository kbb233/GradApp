package com.example.group_10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoom extends AppCompatActivity {

    private Button sendButton;
    private EditText inputMessage;

    private ListView chat_conversation;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_messages = new ArrayList<>();


    private String userID,room_name;
    private String temp_key;

    private DatabaseReference root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        sendButton = (Button) findViewById(R.id.sendButton);
        inputMessage = (EditText) findViewById(R.id.inputMessage);
        chat_conversation = (ListView) findViewById(R.id.chatView);

        userID = getIntent().getExtras().get("user_id").toString();
        room_name = getIntent().getExtras().get("room_name").toString();
        setTitle(" Room - "+room_name);

        root = FirebaseDatabase.getInstance().getReference().child("Rooms").child(room_name);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_messages);

        chat_conversation.setAdapter(arrayAdapter);

        /*
        When you click the button, your message and userID is sent to the database
         */
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map = new HashMap<String,Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String,Object> map2 = new HashMap<String,Object> ();
                map2.put("userID", userID);
                map2.put("msg", inputMessage.getText().toString());

                message_root.updateChildren(map2);
            }
        });


        /*
        When the database is changed i.e something is added, we refresh the ListView
         */
        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                append_chat_conversation(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                append_chat_conversation(snapshot);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        /*
        If you click on someone's profile, it will send you to their profile using their userID.
        This function will most likely be changed.
         */
        chat_conversation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(),OpenProfile.class);
                intent.putExtra("view_user_id",((TextView)view).getText().toString().split(":")[0]);
                startActivity(intent);
            }
        });


    }
    private String getMessage, getUserName;

    /*
    This function will pull all the messages from the database and build a ListView of each message.
     */
    private void append_chat_conversation(DataSnapshot snapshot) {

        Iterator i = snapshot.getChildren().iterator();

        while(i.hasNext()) {
            getMessage = (String) ((DataSnapshot)i.next()).getValue();
            getUserName = (String) ((DataSnapshot)i.next()).getValue();

            list_of_messages.add(getUserName+": "+getMessage);
        }

        arrayAdapter.notifyDataSetChanged();
        inputMessage.setText("");

    }

}
