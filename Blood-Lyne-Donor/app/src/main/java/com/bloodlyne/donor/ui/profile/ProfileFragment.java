package com.bloodlyne.donor.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodlyne.donor.databinding.FragmentProfileBinding;
import com.bloodlyne.firebase.authentication.AuthService;
import com.bloodlyne.firebase.firestore.DonorService;
import com.bloodlyne.models.BadgeInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.donorDisplayNameTV.setText(AuthService.getAuthDonor().getDisplayName());
        binding.donorDonationPointValueTV.setText(String.valueOf(Math.round(AuthService.getAuthDonor().getDonationPoints())));
        binding.donorBloodGroupValueTV.setText(AuthService.getAuthDonor().getBloodGroup());
        Date date = new Date(AuthService.getAuthDonor().getLastDonatedDate());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binding.donotLastDonatedDateValueTV.setText(simpleDateFormat.format(date));
        binding.donorGenderValueTV.setText(AuthService.getAuthDonor().getGender());
        binding.donorHeightValueTV.setText(String.valueOf(Math.round(AuthService.getAuthDonor().getHeight())));
        binding.donorWeightValueTV.setText(String.valueOf(Math.round(AuthService.getAuthDonor().getWeight())));

        recyclerView = binding.badgeRowRV;
        List<BadgeInfo> badgeInfoList = new ArrayList<>();

        DonorService donorService = new DonorService();
        donorService.getDocument(AuthService.getAuthDonor().getEmail()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Object> arrayList = (ArrayList<Object>) task.getResult().get("badges");

                if (arrayList != null && arrayList.size() > 0) {
                    binding.donorBadgeCountValueTV.setText(String.valueOf(arrayList.size()));
                    for (Object notification : arrayList) {
                        HashMap<String, Object> badgeMap = (HashMap<String, Object>) notification;
                        badgeInfoList.add(new BadgeInfo(badgeMap.get("id").toString(),
                                badgeMap.get("title").toString(),
                                badgeMap.get("imageURL").toString(),
                                (Long) badgeMap.get("achievedOn")));
                    }
                } else {
                    binding.donorBadgeCountValueTV.setText("0");
                }
                BadgeAdapter badgeAdapter = new BadgeAdapter(getContext(), badgeInfoList);
                recyclerView.setAdapter(badgeAdapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
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