package com.summercoding.howfar.record;

import android.location.Location;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.summercoding.howfar.Persister;
import com.summercoding.howfar.utils.Utils;

public class RecordTextUpdater implements LocationListener {

    private final TextView textView;
    private final Persister persister;

    public RecordTextUpdater(TextView textView, Persister persister) {
        this.textView = textView;
        this.persister = persister;
    }

    @Override
    public void onLocationChanged(Location location) {
        updateTextView();
    }

    public void updateTextView() {
        double recordDistance = persister.loadRecord();
        if (isSet(recordDistance)) {
            textView.setText(Utils.formatMainText(recordDistance));
        }
    }

    private static boolean isSet(double recordDistance) {
        return recordDistance != 0.0;
    }
}
