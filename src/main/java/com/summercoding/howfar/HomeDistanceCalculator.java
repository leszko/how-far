package com.summercoding.howfar;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class HomeDistanceCalculator {
    private static final double KILO = 1000;

    private static final double HOME_LATITUDE = 50.063987;
    private static final double HOME_LONGITUDE = 19.926916;

    public double distanceInKm(double latitude, double longitude) {
        float[] result = new float[1];
        Location.distanceBetween(HOME_LATITUDE, HOME_LONGITUDE, latitude, longitude, result);
        return result[0] / KILO;
    }
}
