package com.example.kringlan.sololev.util;

import android.location.Location;

public final class GPSTracker {
    public static Location lastLocation;

    public static Location getLastLocation() {
        return lastLocation;
    }

    public static void setLastLocation(Location lastLocation) {
        GPSTracker.lastLocation = lastLocation;
    }
}
