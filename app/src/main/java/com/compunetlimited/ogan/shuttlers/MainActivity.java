package com.compunetlimited.ogan.shuttlers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText usernameET, passwordEt, phoneNumberET, emailAddressET;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bottom_tv).setOnClickListener(this);
        findViewById(R.id.sign_up_bt).setOnClickListener(this);

        usernameET = findViewById(R.id.username_et);
        passwordEt = findViewById(R.id.password_et);
        phoneNumberET = findViewById(R.id.phone_number_et);
        emailAddressET = findViewById(R.id.email_et);
        progressBar = findViewById(R.id.sign_up_pb);

        mAuth = FirebaseAuth.getInstance();
    }

    private void registerUser() {
        String username = usernameET.getText().toString().trim();
        String emailAddress = emailAddressET.getText().toString().trim();
        String phoneNumber = phoneNumberET.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();

        if (emailAddress.isEmpty()) {
            emailAddressET.setError("Email is required");
            emailAddressET.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            emailAddressET.setError("Invalid email address");
            emailAddressET.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEt.setError("Password is required");
            passwordEt.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEt.setError("Password is too short");
            passwordEt.requestFocus();
            return;
        }

        if (username.isEmpty()) {
            usernameET.setError("Username is required");
            usernameET.requestFocus();
            return;
        }

        if (phoneNumber.isEmpty()) {
            phoneNumberET.setError("Phone number is required");
            phoneNumberET.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User registered successfully",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            if (task.getException() != null)
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom_tv:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;

            case R.id.sign_up_bt:
                registerUser();
                break;
        }
    }
}
