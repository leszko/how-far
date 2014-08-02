package com.summercoding.howfar.record;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.summercoding.howfar.LocationReceiver;
import com.summercoding.howfar.Persister;
import com.summercoding.howfar.R;

public class RecordActivity extends FragmentActivity {
    private static final String PREFS_NAME = "HowFarPrefs";

    private LocationReceiver locationReceiver;
    private RecordTextUpdater recordTextUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        init();
    }

    private void init() {
        locationReceiver = LocationReceiver.getInstance();
        Persister persister = new Persister(getSharedPreferences(PREFS_NAME, MODE_PRIVATE));
        TextView textView = (TextView) findViewById(R.id.recordTextView);
        recordTextUpdater = new RecordTextUpdater(textView, persister);

        recordTextUpdater.updateTextView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationReceiver.addLocationListener(recordTextUpdater);
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationReceiver.removeLocationListener(recordTextUpdater);
    }
}
