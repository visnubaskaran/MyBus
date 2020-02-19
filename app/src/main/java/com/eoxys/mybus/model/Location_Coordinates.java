package com.eoxys.mybus.model;

public class Location_Coordinates {
    private String stationname;
    private double lat, lon;

    public Location_Coordinates(String stationname, double lat, double lon) {
        this.stationname = stationname;
        this.lat = lat;
        this.lon = lon;
    }

    public String getStationname() {
        return stationname;
    }

    public double getlat() {
        return lat;
    }

    public double getlon() {
        return lon;
    }
}
