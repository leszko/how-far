package com.summercoding.howfar;

import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.summercoding.howfar.utils.Utils;

public class MainTextUpdater implements LocationListener {
    private static final String TAG = MainTextUpdater.class.getSimpleName();

    private final TextView mainText;
    private final HomeDistanceCalculator distanceCalculator;

    public MainTextUpdater(TextView mainText, HomeDistanceCalculator distanceCalculator) {
        this.mainText = mainText;
        this.distanceCalculator = distanceCalculator;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Update Location: " + location);

        if (distanceCalculator.isHomeSet()) {
            mainText.setText(Utils.formatMainText(distanceCalculator.distanceInKm(location)));
        }
    }
}
