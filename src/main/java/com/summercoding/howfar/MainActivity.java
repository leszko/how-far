package com.summercoding.howfar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "HowFarPrefs";

    private HomeDistanceCalculator distanceCalculator = new HomeDistanceCalculator();

    private DecimalFormat distanceFormat = new DecimalFormat("#.#");

    private TextView mainText;
    private MainActivityLocationManager locationManager;
    private HomeLocationPersister homeLocationPersister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainText = (TextView) findViewById(R.id.textView);
        homeLocationPersister = new HomeLocationPersister(getSharedPreferences(PREFS_NAME, MODE_PRIVATE));
    }

    @Override
    protected void onStart() {
        super.onStart();

        locationManager = new MainActivityLocationManager(this);
        locationManager.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        locationManager.stop();
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
        double homeLatitude = homeLocationPersister.getLatitude();
        double homeLongitude = homeLocationPersister.getLongitude();
        mainText.setText(String.format("Home location: %f, %f", homeLatitude, homeLongitude));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_set_home:
                setHome();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setHome() {
        storeHomeLocation();
        showHomeSetToast();
    }

    private void storeHomeLocation() {
        homeLocationPersister.store(locationManager.getLastLatitude(), locationManager.getLastLongitude());
    }

    private void showHomeSetToast() {
        Context context = getApplicationContext();
        CharSequence text = "Home set";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
