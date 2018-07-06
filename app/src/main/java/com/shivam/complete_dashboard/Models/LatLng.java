package com.shivam.complete_dashboard.Models;

import java.io.Serializable;

/**
 * Created by shivam sharma on 2/8/2018.
 */

public class LatLng implements Serializable{
    private Double latitude;
    private Double longitude;

    public LatLng() {}

    public LatLng(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /*@Override
    public String toString() {
        return latitude + "\n" + longitude;
    }*/
}
