package com.bgu.ecoway.data;

public class AddressSuggestion {

    private String addressText;
    private String city;
    private String distance;

    public AddressSuggestion(String addressText, String city, String distance) {
        this.addressText = addressText;
        this.city = city;
        this.distance = distance;
    }

    public String getAddressText() {
        return addressText;
    }

    public void setAddressText(String addressText) {
        this.addressText = addressText;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
