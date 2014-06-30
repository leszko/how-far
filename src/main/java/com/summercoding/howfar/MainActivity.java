package com.summercoding.howfar;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String PREFS_NAME = "HowFarPrefs";

    private LocationManager locationManager;
    private HomePersister homePersister;
    private MainTextSetter mainTextSetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mainText = (TextView) findViewById(R.id.textView);
        homePersister = new HomePersister(getSharedPreferences(PREFS_NAME, MODE_PRIVATE));
        mainTextSetter = new MainTextSetter(mainText, homePersister);
    }

    @Override
    protected void onStart() {
        super.onStart();

        locationManager = new LocationManager(this);
        locationManager.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        locationManager.stop();
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
        homePersister.store(locationManager.getLastLatitude(), locationManager.getLastLongitude());
        Toast.makeText(getApplicationContext(), "Home set", Toast.LENGTH_LONG).show();
    }

    public void updateLocation(double latitude, double longitude) {
        mainTextSetter.updateLocation(latitude, longitude);
    }
}
