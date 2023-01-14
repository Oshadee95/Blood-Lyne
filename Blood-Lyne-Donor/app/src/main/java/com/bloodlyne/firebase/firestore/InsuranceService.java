package com.bloodlyne.firebase.firestore;

import com.bloodlyne.models.Insurance;

public class InsuranceService extends CollectionService<Insurance> {

    public InsuranceService() {
        super("insurance");
    }
}
