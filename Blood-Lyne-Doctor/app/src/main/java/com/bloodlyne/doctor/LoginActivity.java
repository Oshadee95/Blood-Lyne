package com.bloodlyne.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bloodlyne.firebase.AuthService;
import com.bloodlyne.models.Doctor;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginEmail, etLoginPassword;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginEmail = findViewById(R.id.etLoginEmailAddress);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(view -> {
            if ((!etLoginEmail.getText().toString().isEmpty()) && (etLoginPassword.getText().toString().length() > 5)) {
                AuthService authService = new AuthService();
                authService.signIn(etLoginEmail.getText().toString(), etLoginPassword.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                // Getting created users info from authentication database
                                FirebaseUser firebaseAuthUser = authService.auth.getCurrentUser();

                                // Setting values to user object to be passed to the hashmap
                                Doctor doctor = new Doctor();
                                doctor.setEmail(firebaseAuthUser.getEmail());
                                doctor.setUuid(firebaseAuthUser.getUid());
                                // Setting currently logged in user as the registered user
                                AuthService.setAuthDoctor(doctor);

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
    }
}