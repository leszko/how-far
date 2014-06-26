package com.summercoding.howfar;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivityLocationManager implements LocationListener {

    private static final String PROVIDER = LocationManager.GPS_PROVIDER;
    private static final String NETWORK_PROVIDER = LocationManager.NETWORK_PROVIDER;

    private static final long MIN_TIME = 5000L;
    private static final float MIN_DISTANCE = 100;
    private static final double MIN_ACCURACY = 100;

    private final MainActivity mainActivity;
    private LocationManager locationManager;

    public MainActivityLocationManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void start() {
        locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
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

    @Override
    public void onLocationChanged(Location location) {
        if (location.getAccuracy() < MIN_ACCURACY) {
            mainActivity.updateLocation(location.getLatitude(), location.getLongitude());
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
