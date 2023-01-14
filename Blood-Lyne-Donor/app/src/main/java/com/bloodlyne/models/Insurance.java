package com.bloodlyne.models;

public class Insurance {

    private final String id;
    private final String providerName;
    private final String packageName;
    private final String description;
    private final long pointsNeededToClaim;

    public Insurance(String id, String providerName, String packageName, String description, long pointsNeededToClaim) {
        this.id = id;
        this.providerName = providerName;
        this.packageName = packageName;
        this.description = description;
        this.pointsNeededToClaim = pointsNeededToClaim;
    }

    public String getId() {
        return id;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getDescription() {
        return description;
    }

    public long getPointsNeededToClaim() {
        return pointsNeededToClaim;
    }
}
