package com.summercoding.howfar;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.LocationListener;
import com.summercoding.howfar.utils.Preconditions;

import java.util.LinkedList;

public class LocationReceiver implements android.location.LocationListener {

    private final static String TAG = LocationReceiver.class.getSimpleName();

    private static final String PROVIDER = LocationManager.GPS_PROVIDER;
    private static final String NETWORK_PROVIDER = LocationManager.NETWORK_PROVIDER;

    private static LocationReceiver INSTANCE;

    private static final long MIN_TIME = 1000L;
    private static final float MIN_DISTANCE = 50;
    private static final double MIN_ACCURACY = 200;

    private final LinkedList<LocationListener> locationListeners;
    private LocationManager locationManager;

    private LocationReceiver() {
        this.locationListeners = new LinkedList<LocationListener>();
    }

    public static LocationReceiver getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LocationReceiver();
        }
        return INSTANCE;
    }

    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public void addLocationListener(LocationListener locationListener) {
        locationListeners.add(locationListener);
    }

    public void removeLocationListener(LocationListener locationListener) {
        locationListeners.remove(locationListener);
    }

    public void start() {
        Preconditions.checkNotNull(locationManager);

        if (locationManager.getAllProviders().contains(PROVIDER)) {
            locationManager.requestLocationUpdates(PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        }
        if (locationManager.getAllProviders().contains(NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        }
    }

    public void stop() {
        Preconditions.checkNotNull(locationManager);

        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, String.format("onLocationChanged %f, %f", location.getLatitude(), location.getLongitude()));

        if (location.getAccuracy() < MIN_ACCURACY) {
            notifyLocationListeners(location);
        }
    }

    private void notifyLocationListeners(Location location) {
        for (LocationListener locationListener : locationListeners) {
            locationListener.onLocationChanged(location);
        }
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
