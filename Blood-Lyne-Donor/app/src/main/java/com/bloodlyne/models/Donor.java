package com.bloodlyne.models;

import java.util.List;

public class Donor {

    private String photoURL;
    private String displayName;
    private String email;
    private String phoneNumber;
    private String uuid;
    private String socialSecurityNumber;
    private boolean isActive;
    private String bloodGroup;
    private long donationPoints;
    private BadgeInfo[] badges;
    private long lastDonatedDate;
    private LocationInfo locationInfo;
    private long height;
    private long weight;
    private List<String> healthConditions;
    private long age;
    private String gender;

    public Donor() {
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public float getDonationPoints() {
        return donationPoints;
    }

    public void setDonationPoints(long donationPoints) {
        this.donationPoints = donationPoints;
    }

    public BadgeInfo[] getBadges() {
        return badges;
    }

    public void setBadges(BadgeInfo[] badges) {
        this.badges = badges;
    }

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public List<String> getHealthConditions() {
        return healthConditions;
    }

    public void setHealthConditions(List<String> healthConditions) {
        this.healthConditions = healthConditions;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getLastDonatedDate() {
        return lastDonatedDate;
    }

    public void setLastDonatedDate(long lastDonatedDate) {
        this.lastDonatedDate = lastDonatedDate;
    }
}
