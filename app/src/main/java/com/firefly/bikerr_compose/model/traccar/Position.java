package com.firefly.bikerr_compose.model.traccar;

import java.util.Date;

public class Position extends Event {

    private Date fixTime;

    public Date getFixTime() {
        if (fixTime != null) {
            return new Date(fixTime.getTime());
        } else {
            return null;
        }
    }

    public void setFixTime(Date fixTime) {
        if (fixTime != null) {
            this.fixTime = new Date(fixTime.getTime());
        } else {
            this.fixTime = null;
        }
    }

    public void setTime(Date time) {
        setDeviceTime(time);
        setFixTime(time);
    }

    private boolean outdated;

    public boolean getOutdated() {
        return outdated;
    }

    public void setOutdated(boolean outdated) {
        this.outdated = outdated;
    }

    private boolean valid;

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private double longitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private double altitude;

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    private double speed; // value in knots

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    private double course;

    public double getCourse() {
        return course;
    }

    public void setCourse(double course) {
        this.course = course;
    }

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
