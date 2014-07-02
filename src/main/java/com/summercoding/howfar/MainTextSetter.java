package com.summercoding.howfar;

import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;

import java.text.DecimalFormat;

public class MainTextSetter implements LocationListener {
    private static final String TAG = MainTextSetter.class.getSimpleName();

    private final TextView mainText;

    private final HomeDistanceCalculator distanceCalculator = new HomeDistanceCalculator();
    private final DecimalFormat distanceFormat = new DecimalFormat("0.0");

    private boolean homeSet = false;

    public MainTextSetter(TextView mainText) {
        this.mainText = mainText;
    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocation(location.getLatitude(), location.getLongitude());
    }

    private void updateLocation(double latitude, double longitude) {
        Log.d(TAG, String.format("Update Location: %f, %f", latitude, longitude));

        if (!homeSet) {
            mainText.setText("Where is your home?");
        } else {
            double distance = distanceCalculator.distanceInKm(latitude, longitude);
            String stringDistance = distanceFormat.format(distance);
            if (stringDistance.equals("0")) {
                mainText.setText("you're home");
            } else {
                mainText.setText(stringDistance + " km");
            }
        }
    }

    public void updateHome(Location location) {
        if (location != null) {
            Log.d(TAG, String.format("Update home location: %f, %f", location.getLatitude(), location.getLongitude()));

            homeSet = true;
            distanceCalculator.setHome(location);
        }
    }
}
