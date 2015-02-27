package com.fabiendem.defib68.fragments;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by Fabien on 27/02/15.
 */
public class LocationUtils {
    public static boolean isLocationServiceEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        }
        return false;
    }
}
