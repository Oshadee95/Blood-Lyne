package com.bloodlyne.donor.ui.insurance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodlyne.donor.databinding.FragmentInsuranceBinding;
import com.bloodlyne.firebase.firestore.InsuranceService;
import com.bloodlyne.models.Insurance;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InsuranceFragment extends Fragment {

    private FragmentInsuranceBinding binding;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentInsuranceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.insuranceRV;
        List<Insurance> insuranceList = new ArrayList<>();

        InsuranceService insuranceService = new InsuranceService();

        insuranceService.getCollection().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                if (documentSnapshotList != null && documentSnapshotList.size() > 0) {
                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {

                        insuranceList.add(new Insurance(
                                Objects.requireNonNull(documentSnapshot.get("id")).toString(),
                                documentSnapshot.get("providerName").toString(),
                                documentSnapshot.get("packageName").toString(),
                                documentSnapshot.get("description").toString(),
                                (long) documentSnapshot.get("pointsNeededToClaim")));
                    }
                    InsuranceAdapter insuranceAdapter = new InsuranceAdapter(getContext(), insuranceList);
                    recyclerView.setAdapter(insuranceAdapter);
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