package com.bloodlyne.models;

import java.util.Date;

public class DonationAlert {

    private final String country;
    private final String medialCentreName;
    private final String bloodGroup;
    private final String longitude;
    private final String latitude;
    private final String donationType;
    private final String requiredDate;
    private final int numberOfPintsRequired;

    public DonationAlert(String country, String medialCentreName, String bloodGroup, String longitude, String latitude, String donationType, int numberOfPintsRequired, int dateYear, int dateMonth, int dateDay, int hour, int minute) {
        this.country = country;
        this.medialCentreName = medialCentreName;
        this.bloodGroup = bloodGroup;
        this.longitude = longitude;
        this.latitude = latitude;
        this.donationType = donationType;
        this.numberOfPintsRequired = numberOfPintsRequired;
        this.requiredDate = String.valueOf(new Date(dateYear - 1900, dateMonth, dateDay, hour, minute).getTime());
    }

    @Override
    public String toString() {
        return "{\"country\":\"" + this.country +
                "\",\"medialCentreName\":\""
                + this.medialCentreName
                + "\",\"bloodGroup\":\""
                + this.bloodGroup
                + "\",\"longitude\":\""
                + this.longitude
                + "\",\"latitude\":\""
                + this.latitude + "\","
                + "\"donationType\":\""
                + this.donationType
                + "\",\"noOfPintsNeeded\":\""
                + this.numberOfPintsRequired
                + "\",\"requiredDateTime\":\""
                + this.requiredDate + "\"}";
    }
}
