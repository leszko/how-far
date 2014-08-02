package com.summercoding.howfar;

import android.app.Application;

public class HowFarApplication extends Application {
    private LocationReceiver locationReceiver;

    public void setLocationReceiver(LocationReceiver locationReceiver) {
        this.locationReceiver = locationReceiver;
    }

    public LocationReceiver getLocationReceiver() {
        return locationReceiver;
    }
}
