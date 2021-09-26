package com.bgu.ecoway.data;

public class Stroll {
    private String title;
    private String description;
    private String pic;
    private String distance;
    private String time;
    private String ecology;


    public Stroll(String title, String description, String pic, String distance, String time, String ecology) {
        this.title = title;
        this.description = description;
        this.pic = pic;
        this.distance = distance;
        this.time = time;
        this.ecology = ecology;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEcology() {
        return ecology;
    }

    public void setEcology(String ecology) {
        this.ecology = ecology;
    }
}
