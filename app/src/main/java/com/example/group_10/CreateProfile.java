package com.example.group_10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateProfile extends AppCompatActivity {

    private static final String LOG_TAG =
            CreateProfile.class.getSimpleName();

    private EditText etName, etGradYear, etCurrentMe;

    private Button createButton;

    private FirebaseDatabase database;
    private DatabaseReference root;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        etName = findViewById(R.id.editTextName);
        etGradYear = findViewById(R.id.editTextGradYear);
        etCurrentMe = findViewById(R.id.editText_currentMe);
        createButton = findViewById(R.id.createProfile);
        userID = getIntent().getExtras().get("user_id").toString();

        database = FirebaseDatabase.getInstance();
        root = database.getReference().getRoot().child("users");

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String year = etGradYear.getText().toString();
                String currentMe = etCurrentMe.getText().toString();

                if(name.isEmpty() || year.isEmpty() || currentMe.isEmpty()) {
                    Toast.makeText(CreateProfile.this, "Make sure no fields are empty!",Toast.LENGTH_SHORT).show();
                } else {
                    root.child(userID).child("name").setValue(name);
                    root.child(userID).child("year").setValue(year);
                    root.child(userID).child("current").setValue(currentMe);

                    Intent intent = new Intent(CreateProfile.this, Profile.class);
                    intent.putExtra("user_id", userID);
                    startActivity(intent);

                }
            }
        });
    }
}