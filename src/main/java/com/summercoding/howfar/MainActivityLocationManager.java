package com.summercoding.howfar;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MainActivityLocationManager implements LocationListener {

    private static final String PROVIDER = LocationManager.GPS_PROVIDER;
    private static final long MIN_TIME = 5000L;
    private static final float MIN_DISTANCE = 50;

    private final MainActivity mainActivity;
    private LocationManager locationManager;

    public MainActivityLocationManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void start() {
        locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(PROVIDER, MIN_TIME, MIN_DISTANCE, this);
    }

    public void stop() {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mainActivity.updateLocation(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // do nothing
    }

    @Override
    public void onProviderEnabled(String provider) {
        // do nothing
    }

    @Override
    public void onProviderDisabled(String provider) {
        // do nothing
    }
}
