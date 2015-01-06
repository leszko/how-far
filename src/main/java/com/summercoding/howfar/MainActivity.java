package com.summercoding.howfar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.summercoding.howfar.record.RecordActivity;
import com.summercoding.howfar.record.RecordLocationListener;
import com.summercoding.howfar.view.Arrow;


// TODO split this into SetHomeActivity and WhereIsHomeActivity or HowFarActivity
public class MainActivity extends FragmentActivity {
    private static final String PREFS_NAME = "HowFarPrefs";

    public static final String HOME_SET_MESSAGE = "Home is set";
    public static final String HOME_LOCATION_NOT_FOUND = "Current location not found";
    public static final String HOME_NOT_SET_MESSAGE = "Home not set";
    public static final String SET_HOME_CONFIRMATION_MESSAGE = "Do you want to set a new home?";

    private final HomeDistanceCalculator distanceCalculator = new HomeDistanceCalculator();
    private LocationReceiver locationReceiver;
    private OrientationReceiver orientationReceiver;
    private Persister persister;

    // TODO I think this is unnecessary - it should be done in Activity
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

        init();
        updateSetHomeButtonVisibility();
        updateSetArrowVisibility();

        // TODO it should be move to onResume - we don't need location while in background
        locationReceiver.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        orientationReceiver.start();
    }

    @Override
    protected void onPause() {
        orientationReceiver.stop();
        super.onPause();
    }

    private void init() {
        persister = new Persister(getSharedPreferences(PREFS_NAME, MODE_PRIVATE));
        mainTextUpdater = new MainTextUpdater(
                (TextView) findViewById(R.id.mainTextView),
                (Arrow) findViewById(R.id.homeDirectionArrow),
                distanceCalculator);
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

        orientationReceiver = new OrientationReceiver((SensorManager) getSystemService(Context.SENSOR_SERVICE));
        HomeDirectionUpdater homeDirectionUpdater = new HomeDirectionUpdater(
                (Arrow) findViewById(R.id.homeDirectionArrow),
                distanceCalculator
        );

        orientationReceiver.setDirectionListener(homeDirectionUpdater);
        locationReceiver.addLocationListener(homeDirectionUpdater);
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
                tryToSetHome();
                return true;
            case R.id.action_record:
                onRecordActionClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSetHomeButtonClicked(View view) {
        tryToSetHome();
    }

    private void tryToSetHome() {
        // TODO We don't need to subscribe for location events to obtain that We can use LocationManager.getLastKnownLocation and check how old is it
        Location currentLocation = currentLocationProvider.getCurrentLocation();
        if (currentLocation != null) {
            if (persister.loadLocation() == null) {
                setHome(currentLocation);
            } else {
                setHomeWithConfirmationDialog(currentLocation);
            }
        } else {
            showToast(HOME_LOCATION_NOT_FOUND);
        }
    }

    private void setHomeWithConfirmationDialog(final Location currentLocation) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        setHome(currentLocation);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        showToast(HOME_NOT_SET_MESSAGE);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(SET_HOME_CONFIRMATION_MESSAGE).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    // TODO I really don't like having two methods with the same name - tryToSetHome
    private void setHome(Location location) {
        persister.storeLocation(location);
        distanceCalculator.setHome(location);
        mainTextUpdater.onLocationChanged(location);
        updateSetHomeButtonVisibility();
        updateSetArrowVisibility();

        showToast(HOME_SET_MESSAGE);
    }

    private boolean hasLocation() {
        Location homeLocation = persister.loadLocation();
        return homeLocation != null;
    }

    private void updateSetHomeButtonVisibility() {
        if (hasLocation()) {
            removeButtonLayout();
        }
    }

    private void removeButtonLayout() {
        RelativeLayout buttonLayout = (RelativeLayout) findViewById(R.id.buttonLayout);
        Button setHomeButton = (Button) findViewById(R.id.setHomeButton);
        if (buttonLayout != null && setHomeButton != null) {
            buttonLayout.removeView(setHomeButton);
        }
    }

    private void updateSetArrowVisibility() {
        Arrow homeDirectionArrow = (Arrow) findViewById(R.id.homeDirectionArrow);
        if (homeDirectionArrow != null && orientationReceiver.hasOrientationCapabilities()) {
            if (hasLocation()) {
                homeDirectionArrow.setVisibility(View.VISIBLE);
            } else {
                homeDirectionArrow.setVisibility(View.INVISIBLE);
            }
        } else {
            removeArrow();
        }
    }

    private void removeArrow() {
        Arrow homeDirectionArrow = (Arrow) findViewById(R.id.homeDirectionArrow);
        RelativeLayout arrowLayout = (RelativeLayout) findViewById(R.id.buttonLayout);
        if (homeDirectionArrow != null && arrowLayout != null) {
            arrowLayout.removeView(homeDirectionArrow);
            if (arrowLayout.getChildCount() == 0) {
                removeHomeButtonLayout();
            }
        }
    }

    private void removeHomeButtonLayout() {
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        RelativeLayout setHomeButtonLayout = (RelativeLayout) findViewById(R.id.buttonLayout);
        if (mainLayout != null && setHomeButtonLayout != null) {
            mainLayout.removeView(setHomeButtonLayout);
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
