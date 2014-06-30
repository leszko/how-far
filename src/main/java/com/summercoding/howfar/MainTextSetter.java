package com.summercoding.howfar;

import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainTextSetter {
    private static final String TAG = MainTextSetter.class.getSimpleName();

    private final TextView mainText;
    private final HomePersister homePersister;

    private final HomeDistanceCalculator distanceCalculator = new HomeDistanceCalculator();
    private final DecimalFormat distanceFormat = new DecimalFormat("#.#");

    public MainTextSetter(TextView mainText, HomePersister homePersister) {
        this.mainText = mainText;
        this.homePersister = homePersister;
    }

    public void updateLocation(double latitude, double longitude) {
        Log.i(TAG, "Update Location: latitude = " + latitude + ", longitude = " + longitude);

        double distance = distanceCalculator.distanceInKm(latitude, longitude);
        String stringDistance = distanceFormat.format(distance);
        if (stringDistance.equals("0")) {
            mainText.setText("you're home");
        } else {
            mainText.setText(stringDistance + " km");
        }
        double homeLatitude = homePersister.getLatitude();
        double homeLongitude = homePersister.getLongitude();
        mainText.setText(String.format("Home location: %f, %f", homeLatitude, homeLongitude));
    }
}
