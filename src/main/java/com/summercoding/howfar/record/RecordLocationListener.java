package com.summercoding.howfar.record;

import android.location.Location;

import com.google.android.gms.location.LocationListener;
import com.summercoding.howfar.HomeDistanceCalculator;
import com.summercoding.howfar.Persister;

public class RecordLocationListener implements LocationListener {

    private final HomeDistanceCalculator distanceCalculator;
    private final Persister persister;

    private double recordDistance;

    public RecordLocationListener(Persister persister, HomeDistanceCalculator distanceCalculator) {
        this.persister = persister;
        this.distanceCalculator = distanceCalculator;

        recordDistance = persister.loadRecord();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (distanceCalculator.isHomeSet()) {
            double distance = distanceCalculator.distanceInKm(location);
            if (distance > recordDistance) {
                persister.storeRecord(distance);
                recordDistance = distance;
            }
        }
    }
}
