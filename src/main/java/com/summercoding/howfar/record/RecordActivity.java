package com.summercoding.howfar.record;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.summercoding.howfar.R;
import com.summercoding.howfar.utils.Utils;

public class RecordActivity extends FragmentActivity {
    private static final String PREFS_NAME = "HowFarPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        updateRecordText();
    }

    private void updateRecordText() {
        RecordPersister recordPersister = new RecordPersister(getSharedPreferences(PREFS_NAME, MODE_PRIVATE));
        double recordDistance = recordPersister.load();
        if (isSet(recordDistance)) {
            TextView recordTextView = (TextView) findViewById(R.id.recordTextView);
            recordTextView.setText(Utils.formatMainText(recordDistance));
        }
    }

    private static boolean isSet(double recordDistance) {
        return recordDistance != 0.0;
    }
}
