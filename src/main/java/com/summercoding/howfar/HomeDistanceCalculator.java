package com.summercoding.howfar;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class HomeDistanceCalculator {
    private static final double KILO = 1000;

    private static final double HOME_LATITUDE = 50.063987;
    private static final double HOME_LONGITUDE = 19.926916;

    private static final double MIN_DIFF = 0.01;

    private double lastLatitude = 0.0;
    private double lastLongitude = 0.0;
    private double lastResult = 0.0;

    public double distanceInKm(double latitude, double longitude) {
        if (isDifferenceBigEnough(latitude, longitude)) {
            float[] result = new float[1];
            Location.distanceBetween(HOME_LATITUDE, HOME_LONGITUDE, latitude, longitude, result);
            lastResult = result[0] / KILO;
        }
        return lastResult;
    }

    private boolean isDifferenceBigEnough(double latitude, double longitude) {
        return Math.abs(lastLatitude - latitude) > MIN_DIFF || Math.abs(lastLongitude - longitude) > MIN_DIFF;
    }
}
