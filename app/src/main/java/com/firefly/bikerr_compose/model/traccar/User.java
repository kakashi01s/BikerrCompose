package com.firefly.bikerr_compose.model.traccar;

public class User {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String speedUnit;

    public String getSpeedUnit() { return speedUnit; }

    public void setSpeedUnit(String speedUnit) { this.speedUnit = speedUnit; }

    private double latitude;

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    private double longitude;

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    private int zoom;

    public int getZoom() { return zoom; }

    public void setZoom(int zoom) { this.zoom = zoom; }

    private boolean twelveHourFormat;

    public boolean getTwelveHourFormat() { return twelveHourFormat; }

    public void setTwelveHourFormat(boolean twelveHourFormat) { this.twelveHourFormat = twelveHourFormat; }
}
