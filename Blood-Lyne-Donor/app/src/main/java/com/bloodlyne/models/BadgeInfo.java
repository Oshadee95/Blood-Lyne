package com.bloodlyne.models;

public class BadgeInfo {

    private String id;
    private String title;
    private String imageURL;
    private long achievedOn;

    public BadgeInfo(){}

    public BadgeInfo(String id, String title, String imageURL, long achievedOn) {
        this.id = id;
        this.title = title;
        this.imageURL = imageURL;
        this.achievedOn = achievedOn;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public long getAchievedOn() {
        return achievedOn;
    }
}
