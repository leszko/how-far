package com.summercoding.howfar.record;

import android.content.SharedPreferences;
import android.util.Log;

public class RecordPersister {
    private final static String TAG = RecordPersister.class.getSimpleName();

    private final static String RECORD_DISTANCE = "recordDistance";

    private final SharedPreferences sharedPreferences;

    public RecordPersister(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void store(double recordDistance) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(RECORD_DISTANCE, Double.doubleToRawLongBits(recordDistance));
        editor.commit();

        Log.d(TAG, String.format("Store record distance: %f", recordDistance));
    }

    public double load() {
        return Double.longBitsToDouble(sharedPreferences.getLong(RECORD_DISTANCE, 0L));
    }
}
