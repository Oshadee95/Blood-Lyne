package com.bloodlyne.firebase;

import com.bloodlyne.models.DonationParticipant;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class DonationService extends CollectionService<DonationParticipant> {

    public DonationService() {
        super("donations");
    }

    public Task<QuerySnapshot> getDonationRequestsByMedicalCentre(String medicalCentreName) {
        return db.collection(collectionName).whereEqualTo("isDonationActive", true).whereEqualTo("medialCentreName", medicalCentreName).orderBy("requestedDateAndTime", Query.Direction.DESCENDING).limit(1).get();
    }
}
