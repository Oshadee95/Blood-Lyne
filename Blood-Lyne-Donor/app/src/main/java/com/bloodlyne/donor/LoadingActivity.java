package com.bloodlyne.donor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.bloodlyne.firebase.authentication.AuthService;
import com.bloodlyne.firebase.firestore.DonorService;
import com.bloodlyne.models.Donor;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoadingActivity extends AppCompatActivity {

    public static int TIME_oUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        AuthService authService = new AuthService();
        Handler handler = new Handler();

        if (authService.getFirebaseAuthInstance().getCurrentUser() != null) {
            handler.postDelayed(() -> {
                DonorService donorService = new DonorService();
                donorService.getDocument(authService.getFirebaseAuthInstance().getCurrentUser().getEmail()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        Donor donor = new Donor();
                        donor.setEmail(documentSnapshot.get("email").toString());
                        donor.setDisplayName(documentSnapshot.get("displayName").toString());
                        donor.setDonationPoints((Long) documentSnapshot.get("donationPoints"));
                        donor.setBloodGroup(documentSnapshot.get("bloodGroup").toString());
                        donor.setLastDonatedDate((Long) documentSnapshot.get("lastDonatedDate"));
                        donor.setGender(documentSnapshot.get("gender").toString());
                        donor.setAge((Long) documentSnapshot.get("age"));
                        donor.setHeight((Long) documentSnapshot.get("height"));
                        donor.setWeight((Long) documentSnapshot.get("weight"));

                        AuthService.setAuthDonor(donor);
                        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    finish();
                });
            }, TIME_oUT);
        } else {
            handler.postDelayed(() -> {
                Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }, TIME_oUT);
        }
    }

}
