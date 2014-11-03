package com.fabiendem.defib68.map;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;

import com.fabiendem.defib68.DummyDefibrillators;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by Fabien on 26/10/2014.
 */
public class MapUtils {

    public static LatLng getLatLng(double latitude, double longitude) {
        return new LatLng(latitude, longitude);
    }

    public static LatLng getLatLng(Location location) {
        return getLatLng(location.getLatitude(), location.getLongitude());
    }

    public static boolean isLocationInBounds(Location location, LatLngBounds bounds) {
        if(bounds.contains(getLatLng(location))) {
            return true;
        }
        return false;
    }

    public static LatLngBounds getLatLngBounds(LatLng firstLocation, LatLng secondLocation) {
        double westLongitude;
        double eastLongitude;
        double northLatitude;
        double southLatitude;

        if(firstLocation.longitude < secondLocation.longitude) {
            westLongitude = firstLocation.longitude;
            eastLongitude = secondLocation.longitude;
        }
        else {
            westLongitude = secondLocation.longitude;
            eastLongitude = firstLocation.longitude;
        }

        if(firstLocation.latitude < secondLocation.latitude) {
            northLatitude = secondLocation.latitude;
            southLatitude = firstLocation.latitude;
        }
        else {
            northLatitude = firstLocation.latitude;
            southLatitude = secondLocation.latitude;
        }

        LatLng southwest = new LatLng(southLatitude, westLongitude);
        LatLng northeast = new LatLng(northLatitude, eastLongitude);

        LatLngBounds latLngBounds = new LatLngBounds(southwest, northeast);
        return latLngBounds;
    }

    public static Intent getIntentGoogleMap(LatLng latLng) {
        String uriString = "http://maps.google.com/maps?daddr=" + latLng.latitude + "," + latLng.longitude + "&dirflg=" + TransportMode.WALKING.getCode();
        return new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(uriString));
    }

    public enum TransportMode {
        WALKING("w");

        private String code;

        TransportMode(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }
    }

}
