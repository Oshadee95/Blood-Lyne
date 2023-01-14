package com.bloodlyne.models;

public class NotificationInfo {

    private String distanceToMedicalCentre, donationId, medialCentreName, requiredBloodGroup, requiredDate, requiredTime;
    private boolean isParticipating;

    public NotificationInfo(String distanceToMedicalCentre, String donationId, String medialCentreName, String requiredBloodGroup, String requiredDate, String requiredTime, boolean isParticipating) {
        this.distanceToMedicalCentre = distanceToMedicalCentre;
        this.donationId = donationId;
        this.medialCentreName = medialCentreName;
        this.requiredBloodGroup = requiredBloodGroup;
        this.requiredDate = requiredDate;
        this.requiredTime = requiredTime;
        this.isParticipating = isParticipating;
    }

    public String getDistanceToMedicalCentre() {
        return distanceToMedicalCentre;
    }

    public void setDistanceToMedicalCentre(String distanceToMedicalCentre) {
        this.distanceToMedicalCentre = distanceToMedicalCentre;
    }

    public String getDonationId() {
        return donationId;
    }

    public void setDonationId(String donationId) {
        this.donationId = donationId;
    }

    public String getMedialCentreName() {
        return medialCentreName;
    }

    public void setMedialCentreName(String medialCentreName) {
        this.medialCentreName = medialCentreName;
    }

    public String getRequiredBloodGroup() {
        return requiredBloodGroup;
    }

    public void setRequiredBloodGroup(String requiredBloodGroup) {
        this.requiredBloodGroup = requiredBloodGroup;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
    }

    public String getRequiredTime() {
        return requiredTime;
    }

    public void setRequiredTime(String requiredTime) {
        this.requiredTime = requiredTime;
    }

    public boolean isParticipating() {
        return isParticipating;
    }

    public void setParticipating(boolean participating) {
        isParticipating = participating;
    }
}
