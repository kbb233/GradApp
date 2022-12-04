package com.example.group_10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    private EditText userIDPrompt;
    private EditText passwordPrompt;

    private Button registerButton;

    private FirebaseDatabase database;
    private DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        userIDPrompt = (EditText) findViewById(R.id.inputUserIDReg);
        passwordPrompt = (EditText) findViewById(R.id.inputPasswordReg);
        registerButton = (Button) findViewById(R.id.registerButton);

        database = FirebaseDatabase.getInstance();
        root = database.getReference().getRoot().child("users");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = userIDPrompt.getText().toString();
                String password = passwordPrompt.getText().toString();

                if(userID.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Register.this, "Make sure no fields are empty!",Toast.LENGTH_SHORT).show();
                } else {
                    root.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(userID)) {
                                Toast.makeText(Register.this, "UserID already Exists! Try a different UserID",Toast.LENGTH_SHORT).show();
                            } else {
                                root.child(userID).child("userID").setValue(userID);
                                root.child(userID).child("password").setValue(password);
                                Toast.makeText(Register.this, "Successfully Registered!",Toast.LENGTH_SHORT).show();
                                // Start Activity to go to Start Profile Screen
                                Intent intent = new Intent(Register.this, CreateProfile.class);
                                intent.putExtra("user_id", userID);
                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

    }

}