package com.bloodlyne.models;

public class InviteResponse {

    private String donationId, donorEmail;
    private boolean donorResponse;

    public InviteResponse(String donationId, String donorEmail, boolean donorResponse) {
        this.donationId = donationId;
        this.donorEmail = donorEmail;
        this.donorResponse = donorResponse;
    }

    public String getDonationId() {
        return donationId;
    }

    public String getDonorEmail() {
        return donorEmail;
    }

    public boolean getDonorResponse() {
        return donorResponse;
    }

    @Override
    public String toString() {
        return "{\"donationId\":\"" + this.donationId
                + "\",\"donorEmail\":\""
                + this.donorEmail
                + "\",\"donorResponse\":\""
                + this.donorResponse + "\"}";
    }
}
