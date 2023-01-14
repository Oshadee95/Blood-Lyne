package com.bloodlyne.models;

public class LocationInfo {

    private String address;
    private String city;
    private String state;
    private String country;
    private String longitude;
    private String latitude;

    public LocationInfo() {
    }

    public LocationInfo(String address, String city, String state, String country, String longitude, String latitude) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
