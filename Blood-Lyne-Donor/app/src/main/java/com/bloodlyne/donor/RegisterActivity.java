package com.bloodlyne.donor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bloodlyne.firebase.authentication.AuthService;
import com.bloodlyne.models.Donor;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailField, passwordField, phoneNumberField, socialSecutiryNumerField;
    private Button signUpBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailField = findViewById(R.id.etRegisterEmail);
        passwordField = findViewById(R.id.etRegisterPassword);
        socialSecutiryNumerField = findViewById(R.id.etRegisterNationalIdentificationNumber);
        phoneNumberField = findViewById(R.id.etRegisterPhoneNumber);
        loginBtn = findViewById(R.id.navigateToLoginBtn);
        signUpBtn = findViewById(R.id.signUpBtn);

        loginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        signUpBtn.setOnClickListener(view -> {
            if ((!emailField.getText().toString().isEmpty())
                    && passwordField.getText().toString().length() > 5
                    && (!socialSecutiryNumerField.getText().toString().isEmpty())
                    && (phoneNumberField.getText().toString().length() > 6
                    && phoneNumberField.getText().toString().length() < 11)) {

                AuthService authService = new AuthService();
                authService.signUp(emailField.getText().toString(), passwordField.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Donor donor = new Donor();
                        AuthService.setAuthDonor(donor);
                        AuthService.getAuthDonor().setEmail(emailField.getText().toString());
                        AuthService.getAuthDonor().setSocialSecurityNumber(socialSecutiryNumerField.getText().toString());
                        AuthService.getAuthDonor().setPhoneNumber(phoneNumberField.getText().toString());

                        Intent intent = new Intent(getApplicationContext(), SetupProfileActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to create account", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Missing required Fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}