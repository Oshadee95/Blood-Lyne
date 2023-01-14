package com.bloodlyne.firebase;

import com.bloodlyne.models.Doctor;

public class DoctorService extends CollectionService<Doctor> {

    public DoctorService() {
        super("doctors");
    }
}
