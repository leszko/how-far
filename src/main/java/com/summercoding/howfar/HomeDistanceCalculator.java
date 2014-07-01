package com.summercoding.howfar;

import android.location.Location;

public class HomeDistanceCalculator {
    private static final double KILO = 1000;

    private static final double MIN_DIFF = 0.001;

    private double homeLatitude = 0.0;
    private double homeLongitude = 0.0;

    private double lastLatitude = 0.0;
    private double lastLongitude = 0.0;
    private double lastResult = 0.0;

    public void setHome(Location location) {
        Preconditions.checkNotNull(location);
        homeLatitude = location.getLatitude();
        homeLongitude = location.getLongitude();
    }

    public double distanceInKm(double latitude, double longitude) {
        if (isDifferenceBigEnough(latitude, longitude)) {
            lastLatitude = latitude;
            lastLongitude = longitude;
            float[] result = new float[1];
            Location.distanceBetween(homeLatitude, homeLongitude, latitude, longitude, result);
            lastResult = result[0] / KILO;
        }
        return lastResult;
    }

    private boolean isDifferenceBigEnough(double latitude, double longitude) {
        return Math.abs(lastLatitude - latitude) > MIN_DIFF || Math.abs(lastLongitude - longitude) > MIN_DIFF;
    }
}
