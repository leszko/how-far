package com.summercoding.howfar;

import android.content.Context;
import android.content.Intent;
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

import com.summercoding.howfar.record.RecordActivity;
import com.summercoding.howfar.record.RecordLocationListener;
import com.summercoding.howfar.thirdparty.Eula;

public class MainActivity extends FragmentActivity {
    private static final String PREFS_NAME = "HowFarPrefs";

    public static final String HOME_SET_MESSAGE = "Home is set";
    public static final String HOME_NOT_SET_MESSAGE = "Current location not found";

    private final HomeDistanceCalculator distanceCalculator = new HomeDistanceCalculator();
    private LocationReceiver locationReceiver;
    private Persister persister;
    private MainTextUpdater mainTextUpdater;
    private CurrentLocationProvider currentLocationProvider;

    @Override
    protected void onSaveInstanceState(Bundle SavedInstanceState) {
        super.onSaveInstanceState(SavedInstanceState);
        TextView mainText = (TextView) findViewById(R.id.mainTextView);
        SavedInstanceState.putCharSequence("mainText", mainText.getText());
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        TextView mainText = (TextView) findViewById(R.id.mainTextView);
        mainText.setText(savedInstanceState.getCharSequence("mainText"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Eula.show(this);

        init();
        updateSetHomeButtonVisibility();

        locationReceiver.start();
    }

    private void init() {
        persister = new Persister(getSharedPreferences(PREFS_NAME, MODE_PRIVATE));
        mainTextUpdater = new MainTextUpdater((TextView) findViewById(R.id.mainTextView), distanceCalculator);
        currentLocationProvider = new CurrentLocationProvider();
        locationReceiver = new LocationReceiver((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        RecordLocationListener recordLocationListener = new RecordLocationListener(persister, distanceCalculator);

        locationReceiver.addLocationListener(mainTextUpdater);
        locationReceiver.addLocationListener(currentLocationProvider);
        locationReceiver.addLocationListener(recordLocationListener);

        Location homeLocation = persister.loadLocation();
        distanceCalculator.setHome(homeLocation);
        mainTextUpdater.onLocationChanged(homeLocation);

        ((HowFarApplication) getApplication()).setLocationReceiver(locationReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                return true;
            case R.id.action_record:
                onRecordActionClicked();
                return true;
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
        persister.storeLocation(location);
        distanceCalculator.setHome(location);
        mainTextUpdater.onLocationChanged(location);
        updateSetHomeButtonVisibility();
    }

    private void updateSetHomeButtonVisibility() {
        Location homeLocation = persister.loadLocation();
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

    private void onRecordActionClicked() {
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }

    private void showToast(String message) {
        Context context = getApplicationContext();
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}
