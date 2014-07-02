package com.summercoding.howfar;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
    private static final String PREFS_NAME = "HowFarPrefs";

    public static final String HOME_SET_MESSAGE = "Home set";
    public static final String HOME_NOT_SET_MESSAGE = "Current location not found";

    private LocationReceiver locationReceiver;
    private HomeLocationPersister homeLocationPersister;
    private MainTextSetter mainTextSetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mainText = (TextView) findViewById(R.id.textView);
        homeLocationPersister = new HomeLocationPersister(getSharedPreferences(PREFS_NAME, MODE_PRIVATE));
        mainTextSetter = new MainTextSetter(mainText);
        mainTextSetter.updateHome(homeLocationPersister.loadLocation());
        locationReceiver = new LocationReceiver(mainTextSetter, (LocationManager) getSystemService(Context.LOCATION_SERVICE));
    }

    @Override
    protected void onStart() {
        super.onStart();

        locationReceiver.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        locationReceiver.stop();
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
        Location currentLocation = locationReceiver.getCurrentLocation();
        if (currentLocation != null) {
            homeLocationPersister.store(currentLocation);
            mainTextSetter.updateHome(currentLocation);
            mainTextSetter.onLocationChanged(currentLocation);

            Toast.makeText(getApplicationContext(), HOME_SET_MESSAGE, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), HOME_NOT_SET_MESSAGE, Toast.LENGTH_LONG).show();
        }
    }
}
