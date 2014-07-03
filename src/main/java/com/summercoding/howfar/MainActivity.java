package com.summercoding.howfar;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
    private static final String PREFS_NAME = "HowFarPrefs";

    public static final String HOME_SET_MESSAGE = "Home is set";
    public static final String HOME_NOT_SET_MESSAGE = "Current location not found";

    private LocationReceiver locationReceiver;
    private CurrentLocationProvider currentLocationProvider;
    private HomeLocationPersister homeLocationPersister;
    private MainTextSetter mainTextSetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        updateSetHomeButtonVisibility();
    }

    private void init() {
        homeLocationPersister = new HomeLocationPersister(getSharedPreferences(PREFS_NAME, MODE_PRIVATE));
        mainTextSetter = new MainTextSetter((TextView) findViewById(R.id.mainTextView));
        currentLocationProvider = new CurrentLocationProvider();
        locationReceiver = new LocationReceiver((LocationManager) getSystemService(Context.LOCATION_SERVICE));

        locationReceiver.addLocationListener(mainTextSetter);
        locationReceiver.addLocationListener(currentLocationProvider);

        mainTextSetter.updateHome(homeLocationPersister.loadLocation());
    }

    private void updateSetHomeButtonVisibility() {
        Location homeLocation = homeLocationPersister.loadLocation();
        if (homeLocation != null) {
            removeButtonLayout();
        }
    }

    private void removeButtonLayout() {
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        RelativeLayout buttonLayout = (RelativeLayout) findViewById(R.id.buttonLayout);
        if (mainLayout != null && buttonLayout != null) {
            mainLayout.removeView(buttonLayout);
        }
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

    public void onSetHomeButtonClicked(View view) {
        setHome();
    }

    private void setHome() {
        Location currentLocation = currentLocationProvider.getCurrentLocation();
        if (currentLocation != null) {
            setHome(currentLocation);
            showToast(HOME_SET_MESSAGE);
        } else {
            showToast(HOME_NOT_SET_MESSAGE);
        }
    }

    private void setHome(Location location) {
        homeLocationPersister.store(location);
        mainTextSetter.updateHome(location);
        updateSetHomeButtonVisibility();
    }

    private void showToast(String message) {
        Context context = getApplicationContext();
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}
