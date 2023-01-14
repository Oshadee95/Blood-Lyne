package com.bloodlyne.donor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bloodlyne.firebase.authentication.AuthService;
import com.bloodlyne.firebase.firestore.DonorService;
import com.bloodlyne.models.BadgeInfo;
import com.bloodlyne.models.LocationInfo;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SetupProfileActivity extends AppCompatActivity {

    private EditText displayNameField, heightField, weightField, ageField;
    private Spinner bloodGroupSpinner, genderSpinner;
    private Button saveProfileBtn;
    private String[] bloodGroups = {"Select Blood Group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    private String[] genderTypes = {"Select Gender", "Male", "Female", "Other"};
    private String selectedBloodGroup, selectedGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        AuthService authService = new AuthService();

        displayNameField = findViewById(R.id.etRegisterName);
        heightField = findViewById(R.id.registerHeightET);
        weightField = findViewById(R.id.registerWeightET);
        ageField = findViewById(R.id.registerAgeET);

        bloodGroupSpinner = findViewById(R.id.registerBloodGroupSpinner);
        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    selectedBloodGroup = bloodGroups[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter bloodGroupArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, bloodGroups) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                } else {
                    v = super.getDropDownView(position, null, parent);
                }
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };
        bloodGroupArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(bloodGroupArrayAdapter);

        genderSpinner = findViewById(R.id.registerGenderSpinner);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    selectedGender = genderTypes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter genderArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, genderTypes) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                } else {
                    v = super.getDropDownView(position, null, parent);
                }
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderArrayAdapter);

        saveProfileBtn = findViewById(R.id.saveProfileBtn);
        saveProfileBtn.setOnClickListener(v -> {
            if ((!displayNameField.getText().toString().isEmpty())
                    && (!heightField.getText().toString().isEmpty())
                    && ((Integer.parseInt(heightField.getText().toString()) > 0))
                    && (!weightField.getText().toString().isEmpty())
                    && ((Integer.parseInt(weightField.getText().toString()) > 0))
                    && (!ageField.getText().toString().isEmpty())
                    && ((Integer.parseInt(ageField.getText().toString()) > 15))
                    && selectedBloodGroup != null
                    && selectedGender != null) {

                //Getting created users info from authentication database
                FirebaseUser firebaseAuthUser = authService.auth.getCurrentUser();

                //Setting values to user object to be passed to the hashmap
                AuthService.getAuthDonor().setDisplayName(displayNameField.getText().toString());
                AuthService.getAuthDonor().setUuid(firebaseAuthUser.getUid());
                AuthService.getAuthDonor().setActive(true);
                AuthService.getAuthDonor().setBloodGroup(selectedBloodGroup);
                AuthService.getAuthDonor().setDonationPoints(0);
                AuthService.getAuthDonor().setLocationInfo(new LocationInfo("1/2, Main Rd", "Colombo 1", "Colombo", "Sri Lanka", "79.97268700671474", "6.914682816518195"));

                AuthService.getAuthDonor().setHeight(Long.parseLong(heightField.getText().toString()));
                AuthService.getAuthDonor().setWeight(Long.parseLong(weightField.getText().toString()));
                AuthService.getAuthDonor().setHealthConditions(new ArrayList<>());
                AuthService.getAuthDonor().setAge(Integer.parseInt(ageField.getText().toString()));
                AuthService.getAuthDonor().setGender(selectedGender);

                AuthService.getAuthDonor().setLastDonatedDate(new Date(2022 - 1900, 0, 1).getTime());
                AuthService.getAuthDonor().setBadges(new BadgeInfo[]{});

                // Setting received info from authentication database to a Hashmap to create a firestore document for the user
                Map<String, Object> firestoreDocument = new HashMap<>();
                firestoreDocument.put("email", AuthService.getAuthDonor().getEmail());
                firestoreDocument.put("displayName", AuthService.getAuthDonor().getDisplayName());
                firestoreDocument.put("phoneNumber", AuthService.getAuthDonor().getPhoneNumber());
                firestoreDocument.put("uuid", AuthService.getAuthDonor().getUuid());
                firestoreDocument.put("socialSecurityNumber", AuthService.getAuthDonor().getSocialSecurityNumber());
                firestoreDocument.put("isActive", AuthService.getAuthDonor().isActive());
                firestoreDocument.put("bloodGroup", AuthService.getAuthDonor().getBloodGroup());
                firestoreDocument.put("donationPoints", 0);
                firestoreDocument.put("isActiveParticipant", false);

                Map<String, Object> locationInfoMap = new HashMap<>();
                locationInfoMap.put("address", AuthService.getAuthDonor().getLocationInfo().getAddress());
                locationInfoMap.put("city", AuthService.getAuthDonor().getLocationInfo().getCity());
                locationInfoMap.put("state", AuthService.getAuthDonor().getLocationInfo().getState());
                locationInfoMap.put("country", AuthService.getAuthDonor().getLocationInfo().getCountry());

                Map<String, Object> geoCoordinateMap = new HashMap<>();
                geoCoordinateMap.put("longitude", AuthService.getAuthDonor().getLocationInfo().getLongitude());
                geoCoordinateMap.put("latitude", AuthService.getAuthDonor().getLocationInfo().getLatitude());
                locationInfoMap.put("geoCoordinates", geoCoordinateMap);
                firestoreDocument.put("locationInfo", locationInfoMap);

                firestoreDocument.put("lastDonatedDate", AuthService.getAuthDonor().getLastDonatedDate());

                firestoreDocument.put("badges", new ArrayList<>());

                firestoreDocument.put("height", AuthService.getAuthDonor().getHeight());
                firestoreDocument.put("weight", AuthService.getAuthDonor().getWeight());
                firestoreDocument.put("age", AuthService.getAuthDonor().getAge());
                firestoreDocument.put("healthConditions", new ArrayList<>());
                firestoreDocument.put("gender", AuthService.getAuthDonor().getGender());

                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task1 -> {
                    if (!task1.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task1.getException());
                        Toast.makeText(getApplicationContext(), "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Get new FCM registration token
                    String token = task1.getResult();

                    firestoreDocument.put("fcmToken", token);

                    // Creating a new firestore document for the user
                    DonorService donorService = new DonorService();
                    donorService.addDocumentWithId(AuthService.getAuthDonor().getEmail(), firestoreDocument).addOnSuccessListener(aVoid -> {
                        Toast.makeText(getApplicationContext(), "Successfully saved profile", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent1);
                    }).addOnFailureListener(e -> {
                        Log.d(TAG, "RegisterActivity 103 :  Failed to create firestore document", e);
                        Toast.makeText(getApplicationContext(), "Failed to save profile", Toast.LENGTH_SHORT).show();
                    });

                });
            } else {
                Toast.makeText(getApplicationContext(), "Missing required Fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}