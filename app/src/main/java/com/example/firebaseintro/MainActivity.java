package com.example.firebaseintro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText etLoginEmail, etLoginPassword;
    Button btnLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);

        mAuth = FirebaseAuth.getInstance();

        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userLogin();
            }
        });

    }

    public void onClickForgotPassword(View view) {
    }

    public void onClickRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void userLogin() {

        // 1. getting user data
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        // 2. Validations
        if (email.isEmpty()){
            etLoginEmail.setError("Email address is required");
            etLoginEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etLoginEmail.setError("Enter valid email address");
            etLoginEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            etLoginPassword.setError("Password is required");
            etLoginPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    // TODO: redirect the user to another activity (dashboard / profile)
                    Toast.makeText(getApplicationContext(),"Yaay!, You are authenticated!",Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}