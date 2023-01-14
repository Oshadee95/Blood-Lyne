package com.bloodlyne.models;

public class DonationParticipant {

    private final String donationId;
    private final String bloodGroup;
    private final String distanceToMedicalCentre;
    private final String email;
    private final String name;
    private final String age;
    private final String gender;
    private final String phoneNumber;
    boolean hasDonated;

    public DonationParticipant(String donationId, String bloodGroup, String distanceToMedicalCentre, String email, String name, String age, String gender, String phoneNumber, boolean hasDonated) {
        this.donationId = donationId;
        this.bloodGroup = bloodGroup;
        this.distanceToMedicalCentre = distanceToMedicalCentre;
        this.email = email;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.hasDonated = hasDonated;
    }

    public String getDonationId() {
        return donationId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getDistanceToMedicalCentre() {
        return distanceToMedicalCentre;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean getHasDonated() {
        return hasDonated;
    }
}
