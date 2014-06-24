package com.summercoding.howfar;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";

    private HomeDistanceCalculator distanceCalculator = new HomeDistanceCalculator();

    private DecimalFormat distanceFormat = new DecimalFormat("#.#");

    private TextView mainText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainText = (TextView) findViewById(R.id.textView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        MainActivityLocationManager locationManager = new MainActivityLocationManager(this);
        locationManager.start();
    }

    public void updateLocation(double latitude, double longitude) {
        Log.i(TAG, "Update Location: latitude = " + latitude + ", longitude = " + longitude);

        double distance = distanceCalculator.distanceInKm(latitude, longitude);
        String stringDistance = distanceFormat.format(distance);
        mainText.setText(stringDistance + " km");
    }
}
