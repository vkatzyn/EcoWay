package com.bgu.ecoway.data;

import com.google.gson.annotations.Expose;

public class RoutePoint {
    @Expose
    private double lon;
    @Expose
    private double lat;
    @Expose
    private float score;

    public RoutePoint(double lon, double lat, float score) {
        this.lon = lon;
        this.lat = lat;
        this.score = score;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
