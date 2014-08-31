package com.summercoding.howfar;

import android.location.Location;

import com.summercoding.howfar.utils.Preconditions;

public class HomeDistanceCalculator {
    private static final double KILO = 1000;

    private Location homeLocation = null;

    public void setHome(Location location) {
        homeLocation = location;
    }

    public double distanceInKm(Location location) {
        Preconditions.checkNotNull(homeLocation);
        Preconditions.checkNotNull(location);

        double homeLatitude = homeLocation.getLatitude();
        double homeLongitude = homeLocation.getLongitude();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        float[] result = new float[1];
        Location.distanceBetween(homeLatitude, homeLongitude, latitude, longitude, result);
        return result[0] / KILO;
    }

    public float bearingTo(Location location) {
        return homeLocation.bearingTo(location);
    }

    public boolean isHomeSet() {
        return homeLocation != null;
    }
}