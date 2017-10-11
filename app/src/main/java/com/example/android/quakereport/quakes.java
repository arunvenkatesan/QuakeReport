package com.example.android.quakereport;

public class quakes {
    private double magnitude;
    private String location;
    private long date;
    private String url;

    public quakes(double mag, String place,long date,String website){
        magnitude = mag;
        location=place;
        this.date= date;
        url= website;
    }

    public double getMagnitude() {

        return magnitude;
    }

    public String getLocation() {

        return location;
    }

    public long getDate() {

        return date;
    }

    public String getUrl() {

        return url;
    }
}
