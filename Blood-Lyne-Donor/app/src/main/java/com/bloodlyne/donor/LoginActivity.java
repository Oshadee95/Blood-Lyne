package com.bloodlyne.donor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bloodlyne.firebase.authentication.AuthService;
import com.bloodlyne.models.Donor;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button signUpBtn, loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AuthService authService = new AuthService();

        emailField = findViewById(R.id.etLoginEmailAddress);
        passwordField = findViewById(R.id.etLoginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        signUpBtn = findViewById(R.id.signUpregister);

        loginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        loginBtn.setOnClickListener(view -> {
            if ((!emailField.getText().toString().isEmpty()) && (passwordField.getText().toString().length() > 5)) {
                authService.signIn(emailField.getText().toString(), passwordField.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                // Getting created users info from authentication database
                                FirebaseUser firebaseAuthUser = authService.auth.getCurrentUser();

                                // Setting values to user object to be passed to the hashmap
                                Donor doctor = new Donor();
                                doctor.setEmail(firebaseAuthUser.getEmail());
                                doctor.setUuid(firebaseAuthUser.getUid());
                                // Setting currently logged in user as the registered user
                                AuthService.setAuthDonor(doctor);


                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Missing credentials", Toast.LENGTH_SHORT).show();
            }
        });


        signUpBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });
    }
}