package com.summercoding.howfar;

import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.summercoding.howfar.utils.Preconditions;

public class Persister {

    private final static String TAG = Persister.class.getSimpleName();

    private final static String HOME_LATITUDE = "homeLatitude";
    private final static String HOME_LONGITUDE = "homeLongitude";
    private final static String RECORD_DISTANCE = "recordDistance";

    private final static String PROVIDER = "STORED";

    private final SharedPreferences sharedPreferences;

    public Persister(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void storeLocation(Location location) {
        Preconditions.checkNotNull(location);

        storeInSharedPreferences(HOME_LATITUDE, location.getLatitude());
        storeInSharedPreferences(HOME_LONGITUDE, location.getLongitude());

        Log.d(TAG, "Store home location: " + location);
    }

    public void storeRecord(double recordDistance) {
        storeInSharedPreferences(RECORD_DISTANCE, recordDistance);

        Log.d(TAG, "Store record distance: " + recordDistance);
    }

    private void storeInSharedPreferences(String key, double value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, Double.doubleToRawLongBits(value));
        editor.commit();
    }

    public Location loadLocation() {
        double latitude = Double.longBitsToDouble(sharedPreferences.getLong(HOME_LATITUDE, 0L));
        double longitude = Double.longBitsToDouble(sharedPreferences.getLong(HOME_LONGITUDE, 0L));

        if (isSet(latitude, longitude)) {
            return createLocation(latitude, longitude);
        }
        return null;
    }

    private static boolean isSet(double latitude, double longitude) {
        return latitude != 0.0 || longitude != 0.0;
    }

    private Location createLocation(double latitude, double longitude) {
        Location location = new Location(PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        return location;
    }

    public double loadRecord() {
        return Double.longBitsToDouble(sharedPreferences.getLong(RECORD_DISTANCE, 0L));
    }
}
