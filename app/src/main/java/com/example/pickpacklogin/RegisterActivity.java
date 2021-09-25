package com.example.pickpacklogin;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    TextView haveAccount;
    EditText inputName,inputEmail,inputPassword,inputPhoneNumber,inputAddress;
    Button btnRegister;

    FirebaseDatabase rootNode;
    DatabaseReference reference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        haveAccount=findViewById(R.id.haveAccount);

        inputName=findViewById(R.id.inputName);
        inputEmail=findViewById(R.id.inputEmail);
        inputPassword=findViewById(R.id.inputPassword);
        inputPhoneNumber=findViewById(R.id.inputPhoneNumber);
        inputAddress=findViewById(R.id.inputAddress);
        btnRegister=findViewById(R.id.btnRegister);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                String name = inputName.getEditableText().toString();
                String address = inputAddress.getEditableText().toString();
                String password = inputPassword.getEditableText().toString();
                String phoneNumber = inputPhoneNumber.getEditableText().toString();
                String email = inputEmail.getEditableText().toString();

                UserHelperClass helperClass = new UserHelperClass(name, address, password, phoneNumber, email);

                reference.child(phoneNumber).setValue(helperClass);
                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(intent);
                Toast.makeText(RegisterActivity.this, "hi", Toast.LENGTH_SHORT).show();
            }
        });


    }
}