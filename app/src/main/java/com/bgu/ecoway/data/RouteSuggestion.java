package com.bgu.ecoway.data;

import com.google.gson.annotations.Expose;

import java.util.List;

public class RouteSuggestion {

    @Expose
    private String id;
    @Expose
    private String time;
    @Expose
    private String distance;
    @Expose
    private String ecoScore;
    @Expose
    private List<RoutePoint> routePoints;
    public boolean selected = false;

    public RouteSuggestion(String id, String time, String distance, String ecoScore) {
        this.id = id;
        this.time = time;
        this.distance = distance;
        this.ecoScore = ecoScore;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RoutePoint> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(List<RoutePoint> routePoints) {
        this.routePoints = routePoints;
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
