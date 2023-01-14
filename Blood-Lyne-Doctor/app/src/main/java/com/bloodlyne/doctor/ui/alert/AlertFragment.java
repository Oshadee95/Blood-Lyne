package com.bloodlyne.doctor.ui.alert;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bloodlyne.doctor.CloudFunctionService;
import com.bloodlyne.doctor.databinding.FragmentAlertBinding;
import com.bloodlyne.firebase.AuthService;
import com.bloodlyne.models.DonationAlert;

import java.net.URL;
import java.util.Date;

public class AlertFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentAlertBinding binding;
    private Date selectedDonationDate = null;
    private String selectedDonationType = null;
    private String selectedBloodGroup = null;
    private String[] selectedDonationTime = null;
    private String[] donationTypes = {"Select Donation Type", "Whole Blood", "Power Red", "Platelet", "AB Elite Plasma"};
    private String[] bloodGroups = {"Select Blood Group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAlertBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ProgressDialog progress = new ProgressDialog(getContext());
        CalendarView expectedDonationDateCalenderView = binding.donationDateCalenderView;
        expectedDonationDateCalenderView.setOnDateChangeListener((view, year, month, dayOfMonth) -> selectedDonationDate = new Date(year, month, dayOfMonth));

        EditText numberOfPintsRequiredEditText = binding.NoOfPintsRequiredEditText;

        Spinner donationTypeSpinner = binding.donationTypeSpinner;
        donationTypeSpinner.setOnItemSelectedListener(this);

        ArrayAdapter donationTypeArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, donationTypes) {
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
        donationTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        donationTypeSpinner.setAdapter(donationTypeArrayAdapter);

        donationTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    switch (donationTypes[position]) {
                        case "Whole Blood":
                            selectedDonationType = "whole_blood";
                            break;
                        case "Power Red":
                            selectedDonationType = "power_red";
                            break;
                        case "Platelet":
                            selectedDonationType = "platelet";
                            break;
                        case "AB Elite Plasma":
                            selectedDonationType = "ab_elite_plasma";
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner bloodGroupSpinner = binding.bloodGroupSpinner;
        bloodGroupSpinner.setOnItemSelectedListener(this);

        ArrayAdapter bloodGroupArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, bloodGroups) {
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

        EditText donationTimeEditText = binding.donationTimeEditText;

        Button sendDonationAlertButton = binding.sendDonationAlertButton;
        sendDonationAlertButton.setOnClickListener(v -> {

            if (selectedDonationType != null) {
                if (selectedBloodGroup != null) {
                    if (selectedDonationDate != null) {
                        selectedDonationTime = donationTimeEditText.getText().toString().trim().split(":");
                        if (selectedDonationTime.length == 2) {
                            if (Integer.parseInt(selectedDonationTime[0]) > 0 && Integer.parseInt(selectedDonationTime[0]) < 25) {
                                if (Integer.parseInt(selectedDonationTime[1]) > -1 && Integer.parseInt(selectedDonationTime[1]) < 61) {

                                    progress.setCancelable(false); //is used so that ProgressDialog cannot be cancelled until the work is done
                                    progress.setMessage("Sending alert to donors");
                                    progress.show(); //To start the ProgressDialog
//                                    progress.dismiss(); //To stop the ProgressDialog
//                                    AsyncTask<String, Void, String> cloudFunctionService = null;
                                    try {
                                        URL url = new URL("https://us-central1-blood-lyne.cloudfunctions.net/onLocateViableDonorRequest");
                                        DonationAlert donationAlert = new DonationAlert(AuthService.getAuthDoctor().getHospitalInfo().getCountry(), AuthService.getAuthDoctor().getHospitalInfo().getName(), selectedBloodGroup, AuthService.getAuthDoctor().getHospitalInfo().getLongitude(), AuthService.getAuthDoctor().getHospitalInfo().getLatitude(), selectedDonationType, Integer.parseInt(numberOfPintsRequiredEditText.getText().toString()), selectedDonationDate.getYear(), selectedDonationDate.getMonth(), selectedDonationDate.getDate(), Integer.parseInt(selectedDonationTime[0]), Integer.parseInt(selectedDonationTime[1]));

                                        //  Reference : https://stackoverflow.com/questions/9963691/android-asynctask-sending-callbacks-to-ui
                                         new CloudFunctionService(url, donationAlert.toString(), s -> {
                                            progress.dismiss(); //To stop the ProgressDialog
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                            AlertDialog dialog = builder.create();
                                            dialog.setMessage(s);
                                            CharSequence charSequence = "Close";
                                            dialog.setButton(DialogInterface.BUTTON_POSITIVE, charSequence, (dialog1, which) -> dialog1.cancel());
                                            dialog.show();
                                        }).execute();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    Toast.makeText(getContext(), "Invalid minute format. Should be between 0 and 60", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Invalid hour format. Should be between 0 and 24", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Invalid time format. Should be as 14:25", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Please select a date to continue", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please select a blood group to continue", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), "Please select a donation type to continue", Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}