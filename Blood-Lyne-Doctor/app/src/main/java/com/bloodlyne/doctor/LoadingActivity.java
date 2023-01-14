package com.bloodlyne.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.bloodlyne.firebase.AuthService;
import com.bloodlyne.firebase.DoctorService;
import com.bloodlyne.models.Doctor;
import com.bloodlyne.models.HospitalInfo;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;

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
                // Getting created users info from authentication database
                FirebaseUser firebaseAuthUser = authService.auth.getCurrentUser();

                // Setting values to user object to be passed to the hashmap
                Doctor doctor = new Doctor();
                doctor.setEmail(firebaseAuthUser.getEmail());
                doctor.setUuid(firebaseAuthUser.getUid());

                DoctorService doctorService = new DoctorService();
                doctorService.getDocument(authService.getFirebaseAuthInstance().getCurrentUser().getEmail()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        doctor.setName(documentSnapshot.get("displayName").toString());

                        HashMap<String, Object> hospitalInfoMap = (HashMap<String, Object>) documentSnapshot.get("hospitalInfo");

                        HospitalInfo hospitalInfo = new HospitalInfo();
                        hospitalInfo.setName(hospitalInfoMap.get("name").toString());
                        hospitalInfo.setAddress(hospitalInfoMap.get("address").toString());
                        hospitalInfo.setCity(hospitalInfoMap.get("city").toString());
                        hospitalInfo.setCountry(hospitalInfoMap.get("country").toString());

                        HashMap<String, String> hospitalCoordinatesMap = (HashMap<String, String>) hospitalInfoMap.get("geoCoordinates");
                        hospitalInfo.setLatitude(hospitalCoordinatesMap.get("latitude"));
                        hospitalInfo.setLongitude(hospitalCoordinatesMap.get("longitude"));
                        doctor.setHospitalInfo(hospitalInfo);

                        AuthService.setAuthDoctor(doctor);

                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                    }
                    finish();
                });

            }, TIME_oUT);
        } else {
            handler.postDelayed(() -> {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }, TIME_oUT);
        }

    }
}