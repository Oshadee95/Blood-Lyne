package com.bloodlyne.firebase.firestore;


import com.bloodlyne.models.Donor;

public class DonorService extends CollectionService<Donor> {

    public DonorService() {
        super("donors");
    }
}
