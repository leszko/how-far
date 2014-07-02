package com.summercoding.howfar;

import android.location.Location;

public class HomeDistanceCalculator {
    private static final double KILO = 1000;

    private double homeLatitude = 0.0;
    private double homeLongitude = 0.0;

    public void setHome(Location location) {
        Preconditions.checkNotNull(location);
        homeLatitude = location.getLatitude();
        homeLongitude = location.getLongitude();
    }

    public double distanceInKm(double latitude, double longitude) {
        float[] result = new float[1];
        Location.distanceBetween(homeLatitude, homeLongitude, latitude, longitude, result);
        return result[0] / KILO;
    }
}