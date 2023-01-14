package com.bloodlyne.models;

public class Doctor {

    private String photoURL;
    private String name;
    private String email;
    private String medicalRegistration;
    private String phoneNumber;
    private String uuid;
    private HospitalInfo hospitalInfo;

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMedicalRegistration() {
        return medicalRegistration;
    }

    public void setMedicalRegistration(String medicalRegistration) {
        this.medicalRegistration = medicalRegistration;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public HospitalInfo getHospitalInfo() {
        return hospitalInfo;
    }

    public void setHospitalInfo(HospitalInfo hospitalInfo) {
        this.hospitalInfo = hospitalInfo;
    }
}
