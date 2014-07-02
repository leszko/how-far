package com.summercoding.howfar;

import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;

import java.text.DecimalFormat;

public class MainTextSetter implements LocationListener {
    private static final String TAG = MainTextSetter.class.getSimpleName();

    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");

    private final TextView mainText;
    private final HomeDistanceCalculator distanceCalculator = new HomeDistanceCalculator();

    private boolean isHomeSet = false;

    public MainTextSetter(TextView mainText) {
        this.mainText = mainText;
    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocation(location.getLatitude(), location.getLongitude());
    }

    private void updateLocation(double latitude, double longitude) {
        Log.d(TAG, String.format("Update Location: %f, %f", latitude, longitude));

        if (!isHomeSet) {
            mainText.setText("Where is your home?");
        } else {
            mainText.setText(createDistanceFromHomeText(latitude, longitude));
        }
    }

    private String createDistanceFromHomeText(double latitude, double longitude) {
        double distance = distanceCalculator.distanceInKm(latitude, longitude);
        String stringDistance = DECIMAL_FORMAT.format(distance);
        return stringDistance + " km";
    }

    public void updateHome(Location location) {
        if (location != null) {
            Log.d(TAG, String.format("Update home location: %f, %f", location.getLatitude(), location.getLongitude()));

            isHomeSet = true;
            distanceCalculator.setHome(location);
        }
    }
}
