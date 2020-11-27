package com.example.firebaseintro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    Spinner spinnerDepartment;
    EditText etName, etRegNumber, etEmail, etPassword, etConfirmPassword;
    Button btnRegister;
    String department;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // references for the views

        spinnerDepartment = findViewById(R.id.spinnerDepartment);
        etName = findViewById(R.id.etName);
        etRegNumber = findViewById(R.id.etRegNumber);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);

        // Authentication object
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. get student data
                String name = etName.getText().toString();
                String regNumber = etRegNumber.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                // 2. validations
                // empty fields validation
                // Validations.checkEmptiness(name, etName, "Name can't be blank");
                if (name.isEmpty()){
                    etName.setError("Name can't be blank");
                    etName.requestFocus();
                    return;
                }

                if (regNumber.isEmpty()){
                    etRegNumber.setError("Registration Number can't be blank");
                    etRegNumber.requestFocus();
                    return;
                }

                if (email.isEmpty()){
                    etEmail.setError("Email Address can't be blank");
                    etEmail.requestFocus();
                    return;
                }
                // email format validation
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etEmail.setError("Enter a valid email address");
                    etEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()){
                    etPassword.setError("Password can't be blank");
                    etPassword.requestFocus();
                    return;
                }
                // validating password length
                // Minimum firebase password length is 6
                if (password.length() < 6){
                    etPassword.setError("Minimum Password length should be 6 characters");
                    etPassword.requestFocus();
                    return;
                }

                if (confirmPassword.isEmpty()){
                    etConfirmPassword.setError("Confirm Password can't be blank");
                    etConfirmPassword.requestFocus();
                    return;
                }

                //validating if pswd & confirm pswd match
                if( !password.equals(confirmPassword)){
                    etConfirmPassword.setError("Password does not match");
                    etConfirmPassword.requestFocus();
                    return;
                }

                // Registering user in firebase realtime database

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    User user = new User(name, regNumber, email, department);

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser()
                                                    .getUid())
                                            .setValue(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this,
                                                        "Student registered successfully",
                                                        Toast.LENGTH_LONG).show();
                                            }else {
                                                Toast.makeText(RegisterActivity.this,
                                                        "Something went wrong, " +
                                                                "could not register the user",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(RegisterActivity.this,
                                            "Something went wrong, could not register the user",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });

        spinnerDepartment.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        department = parent.getItemAtPosition(position).toString();
//        Toast.makeText(this, department, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}