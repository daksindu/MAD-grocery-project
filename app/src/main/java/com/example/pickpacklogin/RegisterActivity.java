package com.example.pickpacklogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    TextView haveAccount;
    EditText inputName,inputEmail,inputPassword,inputConformPassword,inputAddress;
    Button btnRegister;
    String emailPattern = "a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        haveAccount=findViewById(R.id.haveAccount);

        inputName=findViewById(R.id.inputName);
        inputEmail=findViewById(R.id.inputEmail);
        inputPassword=findViewById(R.id.inputPassword);
        inputConformPassword=findViewById(R.id.inputConformPassword);
        inputAddress=findViewById(R.id.inputAddress);
        btnRegister=findViewById(R.id.btnRegister);
        progressDialog=new ProgressDialog(this);
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerforAuth();
            }
        });
    }

    private void PerforAuth() {
        String name=inputName.getText().toString();
        String email=inputEmail.getText().toString();
        String password=inputPassword.getText().toString();
        String conformPassword=inputConformPassword.getText().toString();
        String address=inputAddress.getText().toString();

        if (email.matches(emailPattern))
        {
            inputEmail.setError("Enter Correct Email");
        }else if(password.isEmpty() || password.length()<6)
        {
            inputPassword.setError("Enter Proper Password");
        }else if (password.equals(conformPassword))
        {
            inputConformPassword.setError("Password not match Both field");
        }else
        {
            progressDialog.setMessage("Please Wait While Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(RegisterActivity.this, "Registaration Successful", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }

    private void sendUserToNextActivity() {
        Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}