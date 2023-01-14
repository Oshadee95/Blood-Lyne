package com.bloodlyne.doctor.ui.donations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodlyne.doctor.databinding.FragmentDonationParticipantsBinding;
import com.bloodlyne.firebase.AuthService;
import com.bloodlyne.firebase.DonationService;
import com.bloodlyne.models.DonationParticipant;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DonationRequestsFragment extends Fragment {

    private FragmentDonationParticipantsBinding binding;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDonationParticipantsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.donationRequestsRecyclerView;
        List<DonationParticipant> donationParticipantList = new ArrayList<>();

        DonationService donorService = new DonationService();
        donorService.getDonationRequestsByMedicalCentre(AuthService.getAuthDoctor().getHospitalInfo().getName()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                if (documentSnapshotList != null && documentSnapshotList.size() > 0) {
                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {

                        ArrayList participantList = (ArrayList) documentSnapshot.get("participants");
                        if (participantList != null && participantList.size() > 0) {
                            for (Object participant : participantList) {
                                HashMap<String, Object> participantMap = (HashMap<String, Object>) participant;
                                donationParticipantList.add(new DonationParticipant(
                                        Objects.requireNonNull(documentSnapshot.get("donationId")).toString(),
                                        participantMap.get("bloodGroup").toString(),
                                        participantMap.get("distance").toString(),
                                        participantMap.get("email").toString(),
                                        participantMap.get("name").toString(),
                                        participantMap.get("age").toString(),
                                        participantMap.get("gender").toString(),
                                        participantMap.get("phoneNumber").toString(),
                                        (Boolean) participantMap.get("hasDonated")));
                            }
                        }
                    }
                    DonationRequestsAdapter notificationAdapter = new DonationRequestsAdapter(getContext(), donationParticipantList);
                    recyclerView.setAdapter(notificationAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }
        }).addOnFailureListener(Throwable::printStackTrace);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}