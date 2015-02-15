package com.fabiendem.defib68.utils;

import android.location.Location;

import com.fabiendem.defib68.map.MapUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.geometry.Bounds;

/**
 * Created by Fabien on 02/11/2014.
 */
public class HautRhinUtils {
    public static final double TOP_BOUND = 48.447412;
    public static final double BOTTOM_BOUND = 47.3717699;
    public static final double LEFT_BOUND = 6.7435655;
    public static final double RIGHT_BOUND = 8.0890955;

    public static final Bounds getBounds() {
        return new Bounds(LEFT_BOUND, RIGHT_BOUND, BOTTOM_BOUND, TOP_BOUND);
    }

    public static final LatLngBounds getLatLngBounds() {
        return new LatLngBounds(
                new LatLng(BOTTOM_BOUND, LEFT_BOUND),
                new LatLng(TOP_BOUND, RIGHT_BOUND));
    }

    public static boolean isLocationInHautRhin(Location location) {
        return MapUtils.isLocationInBounds(location, HautRhinUtils.getLatLngBounds());
    }
}
