package com.summercoding.howfar;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.location.LocationListener;

public class LocationReceiver implements android.location.LocationListener {

    private static final String PROVIDER = LocationManager.GPS_PROVIDER;
    private static final String NETWORK_PROVIDER = LocationManager.NETWORK_PROVIDER;

    private static final long MIN_TIME = 5000L;
    private static final float MIN_DISTANCE = 100;
    private static final double MIN_ACCURACY = 100;

    private static final long CURRENT_LOCATION_TIMEOUT = 5L * 60L * 1000L; // 5 min

    private final LocationListener locationListener;
    private LocationManager locationManager;

    private Location currentLocation = null;
    private long currentLocationTimestamp = 0L;

    public LocationReceiver(LocationListener locationListener, LocationManager locationManager) {
        this.locationListener = locationListener;
        this.locationManager = locationManager;
    }

    public void start() {
        if (locationManager.getAllProviders().contains(PROVIDER)) {
            locationManager.requestLocationUpdates(PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        }
        if (locationManager.getAllProviders().contains(NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        }
    }

    public void stop() {
        locationManager.removeUpdates(this);
    }

    public Location getCurrentLocation() {
        if (System.currentTimeMillis() - currentLocationTimestamp > CURRENT_LOCATION_TIMEOUT) {
            return null;
        }
        return currentLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        currentLocationTimestamp = System.currentTimeMillis();
        if (location.getAccuracy() < MIN_ACCURACY) {
            locationListener.onLocationChanged(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // do nothing
    }

    @Override
    public void onProviderEnabled(String provider) {
        refreshLocationUpdates();
    }

    @Override
    public void onProviderDisabled(String provider) {
        refreshLocationUpdates();
    }

    private void refreshLocationUpdates() {
        stop();
        start();
    }
}
