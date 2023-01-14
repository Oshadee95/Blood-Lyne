package com.bloodlyne.models;

public class DonationConfirmation {

    private final String donationId;
    private final String donorEmail;

    public DonationConfirmation(String donationId, String donorEmail) {
        this.donationId = donationId;
        this.donorEmail = donorEmail;
    }

    @Override
    public String toString() {
        return "{\"donationId\":\"" + this.donationId
                + "\",\"donorEmail\":\""
                + this.donorEmail + "\"}";
    }
}
