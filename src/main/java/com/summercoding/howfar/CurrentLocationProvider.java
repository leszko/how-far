package com.summercoding.howfar;

import android.location.Location;

import com.google.android.gms.location.LocationListener;

public class CurrentLocationProvider implements LocationListener {
    private static final long CURRENT_LOCATION_TIMEOUT = 5L * 60L * 1000L; // 5 min

    private Location currentLocation = null;
    private long currentLocationTimestamp = 0L;

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        currentLocationTimestamp = System.currentTimeMillis();
    }

    public Location getCurrentLocation() {
        if (System.currentTimeMillis() - currentLocationTimestamp > CURRENT_LOCATION_TIMEOUT) {
            return null;
        }
        return currentLocation;
    }
}
