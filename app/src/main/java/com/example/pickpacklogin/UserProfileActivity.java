package com.example.pickpacklogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.CollationElementIterator;
import java.util.jar.Attributes;

public class UserProfileActivity extends AppCompatActivity {

    private TextView inputName,inputEmail,inputPassword,inputPhoneNumber,inputAddress;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private static final String USERS = "users";
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputPhoneNumber = findViewById(R.id.inputPhoneNumber);
        inputAddress = findViewById(R.id.inputAddress);

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(USERS);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (ds.child("email").getValue().equals(email)){

                        inputName.setText(ds.child("Name").getValue(String.class));
                        inputEmail.setText("Email");
                        inputPassword.setText(ds.child("Password").getValue(String.class));
                        inputPhoneNumber.setText(ds.child("Phone Number").getValue(String.class));
                        inputAddress.setText(ds.child("Address").getValue(String.class));


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }



    }
