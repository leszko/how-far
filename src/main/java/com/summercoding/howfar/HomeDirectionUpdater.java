package com.summercoding.howfar;

import android.location.Location;

import com.google.android.gms.location.LocationListener;
import com.summercoding.howfar.utils.AverageQueue;
import com.summercoding.howfar.view.Arrow;

/**
 * Created by piotrek on 8/27/14.
 */
public class HomeDirectionUpdater implements LocationListener, DirectionListener {

    private final Arrow homeDirectionArrow;
    private final HomeDistanceCalculator distanceCalculator;
    private final AverageQueue rotation = new AverageQueue(60);
    private float azimuth;
    private float bearing;

    public HomeDirectionUpdater(Arrow homeDirectionArrow, HomeDistanceCalculator homeDistanceCalculator) {
        this.homeDirectionArrow = homeDirectionArrow;
        this.distanceCalculator = homeDistanceCalculator;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (distanceCalculator.isHomeSet()) {
            this.bearing = distanceCalculator.bearingTo(location);
            updateRotation();
        }
    }

    @Override
    public void onDirectionChanged(double azimuth) {
        this.azimuth = (float) azimuth;
        updateRotation();
        updateArrow();
    }

    public void updateRotation() {
        this.rotation.addValue(getCurrentRotation());
    }

    public float getCurrentRotation() {
        return this.bearing - this.azimuth;
    }

    public float getAverageRotation() {
        return this.rotation.getAverage();
    }

    public void updateArrow() {
        this.homeDirectionArrow.setRotation(180 + getAverageRotation());
    }
}
