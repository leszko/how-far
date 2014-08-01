package com.summercoding.howfar.record;

import android.location.Location;

import com.google.android.gms.location.LocationListener;
import com.summercoding.howfar.HomeDistanceCalculator;

public class RecordLocationListener implements LocationListener {

    private final HomeDistanceCalculator distanceCalculator;
    private final RecordPersister recordPersister;

    private double recordDistance;

    public RecordLocationListener(RecordPersister recordPersister, HomeDistanceCalculator distanceCalculator) {
        this.recordPersister = recordPersister;
        this.distanceCalculator = distanceCalculator;

        recordDistance = recordPersister.load();
    }

    @Override
    public void onLocationChanged(Location location) {
        double distance = distanceCalculator.distanceInKm(location);
        if (distance > recordDistance) {
            recordPersister.store(distance);
            recordDistance = distance;
        }
    }
}
