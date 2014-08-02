package com.summercoding.howfar;

import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.summercoding.howfar.utils.Preconditions;

public class HomeLocationPersister {

    private final static String TAG = HomeLocationPersister.class.getSimpleName();

    private final static String HOME_LATITUDE = "homeLatitude";
    private final static String HOME_LONGITUDE = "homeLongitude";

    private final static String PROVIDER = "STORED";

    private final SharedPreferences sharedPreferences;

    public HomeLocationPersister(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void store(Location location) {
        Preconditions.checkNotNull(location);

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        storeInSharedPreferences(latitude, longitude);

        Log.d(TAG, String.format("Store home location: %f, %f", latitude, longitude));
    }

    private void storeInSharedPreferences(double latitude, double longitude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(HOME_LATITUDE, Double.doubleToRawLongBits(latitude));
        editor.putLong(HOME_LONGITUDE, Double.doubleToRawLongBits(longitude));
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
}
