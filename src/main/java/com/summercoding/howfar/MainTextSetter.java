package com.summercoding.howfar;

import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;

public class MainTextSetter implements LocationListener {
    private static final String TAG = MainTextSetter.class.getSimpleName();

    private final TextView mainText;
    private final HomeDistanceCalculator distanceCalculator;

    private boolean isHomeSet = false;

    public MainTextSetter(TextView mainText, HomeDistanceCalculator distanceCalculator) {
        this.mainText = mainText;
        this.distanceCalculator = distanceCalculator;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, String.format("Update Location: %f, %f", location.getLatitude(), location.getLongitude()));

        if (isHomeSet) {
            mainText.setText(Utils.formatMainText(distanceCalculator.distanceInKm(location)));
        }
    }

    public void updateHome(Location location) {
        if (location != null) {
            isHomeSet = true;
            onLocationChanged(location);
        }
    }
}
