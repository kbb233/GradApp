package com.example.group_10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText userIDPrompt;
    private EditText passwordPrompt;

    private Button loginButton;

    private TextView registerClicker;

    private FirebaseDatabase database;
    private DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        userIDPrompt = (EditText) findViewById(R.id.inputUserID);
        passwordPrompt = (EditText) findViewById(R.id.inputPassword);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerClicker = (TextView) findViewById(R.id.registerClick);

        database = FirebaseDatabase.getInstance();
        root = database.getReference().getRoot().child("users");

        registerClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = userIDPrompt.getText().toString();
                String password = passwordPrompt.getText().toString();

                if(userID.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Make sure no fields are empty!",Toast.LENGTH_SHORT).show();
                } else {
                    root.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(userID)) {
                                String getPassword = snapshot.child(userID).child("password").getValue().toString();
                                if(password.equals(getPassword)) {
                                    Toast.makeText(Login.this, "Logged in successfully",Toast.LENGTH_SHORT).show();
                                    // Start Activity to Main Page of APp
                                    Intent intent = new Intent(Login.this,MainActivity.class);
                                    intent.putExtra("user_id", userID);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Login.this, "Wrong Password",Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login.this, "UserID does not exist",Toast.LENGTH_SHORT).show();
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