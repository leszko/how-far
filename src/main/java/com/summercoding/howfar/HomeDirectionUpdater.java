package com.summercoding.howfar;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationListener;

import com.summercoding.howfar.view.Arrow;

/**
 * Created by piotrek on 8/27/14.
 */
public class HomeDirectionUpdater implements LocationListener, DirectionListener {

    private final Arrow homeDirectionArrow;
    private final HomeDistanceCalculator distanceCalculator;
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
//            updateArrow();
        }
    }

    @Override
    public void onDirectionChanged(double azimuth) {
        this.azimuth = (float)azimuth;
        updateArrow();
    }

    public float getRotation() {
        return this.bearing - this.azimuth;
    }

    public void updateArrow() {
        this.homeDirectionArrow.setRotation(180 + getRotation());
    }
}
