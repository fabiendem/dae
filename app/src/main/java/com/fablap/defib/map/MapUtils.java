package com.fablap.defib.map;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;

import com.fablap.defib.utils.HautRhinUtils;
import com.fablap.defib.utils.UiUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
        return bounds.contains(getLatLng(location));
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

    public static void moveCameraToHautRhin(Context context, GoogleMap map) {
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(HautRhinUtils.getLatLngBounds(),
                UiUtils.dpToPx(context, 100)));
    }

    public static void animateCameraToHautRhin(Context context, GoogleMap map) {
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(HautRhinUtils.getLatLngBounds(),
                UiUtils.dpToPx(context, 0)));
    }

    public static void animateCameraToCurrentLocation(GoogleMap map, Location currentLocation) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(MapUtils.getLatLng(currentLocation), 15));
    }

    public static String getDistanceFormatted(long distanceInMeters) {
        String distanceFormatted = "";

        // Prepend Km
        if(distanceInMeters >= 1000) {
            distanceFormatted += distanceInMeters / 1000 + " km";
        }
        else {
            distanceFormatted += distanceInMeters + " m";
        }

        return distanceFormatted;
    }
}
