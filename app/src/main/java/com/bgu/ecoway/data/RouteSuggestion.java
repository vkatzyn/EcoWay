package com.bgu.ecoway.data;

public class RouteSuggestion {

    private String time;
    private String distance;
    private String ecoScore;

    public RouteSuggestion(String time, String distance, String ecoScore) {
        this.time = time;
        this.distance = distance;
        this.ecoScore = ecoScore;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getEcoScore() {
        return ecoScore;
    }

    public void setEcoScore(String ecoScore) {
        this.ecoScore = ecoScore;
    }
}
