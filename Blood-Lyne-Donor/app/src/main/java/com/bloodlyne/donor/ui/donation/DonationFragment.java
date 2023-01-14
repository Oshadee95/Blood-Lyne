package com.bloodlyne.donor.ui.donation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodlyne.donor.databinding.FragmentDonationBinding;
import com.bloodlyne.firebase.authentication.AuthService;
import com.bloodlyne.firebase.firestore.DonorService;
import com.bloodlyne.models.NotificationInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DonationFragment extends Fragment {

    private FragmentDonationBinding binding;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDonationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.notificationRecyclerView;
        List<NotificationInfo> notificationInfoList = new ArrayList<>();

        DonorService donorService = new DonorService();
        donorService.getDocument(AuthService.getAuthDonor().getEmail()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Object> arrayList = (ArrayList<Object>) task.getResult().get("donationNotifications");
                if (arrayList != null && arrayList.size() > 0) {
                    for (Object notification : arrayList) {
                        HashMap<String, Object> notificationMap = (HashMap<String, Object>) notification;
                        notificationInfoList.add(new NotificationInfo(notificationMap.get("distanceToMedicalCentre").toString(), notificationMap.get("donationId").toString(), notificationMap.get("medialCentreName").toString(), notificationMap.get("requiredBloodGroup").toString(), notificationMap.get("requiredDate").toString(), notificationMap.get("requiredTime").toString(), (Boolean) notificationMap.get("isParticipating")));
                    }
                }
                DonationAdapter donationAdapter = new DonationAdapter(getContext(), notificationInfoList);
                recyclerView.setAdapter(donationAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
