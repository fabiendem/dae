package com.fabiendem.defib68.map;

import android.location.Location;
import android.util.Log;

import com.fabiendem.defib68.models.defibrillator.DefibrillatorClusterItem;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.geometry.Bounds;
import com.google.maps.android.quadtree.PointQuadTree;

import java.util.ArrayList;

/**
 * Created by Fabien on 01/12/14.
 */
public class DefibrillatorFinder {

    public static final String TAG = "DefibrillatorFinder";

    public static DefibrillatorClusterItem getClosestDefibrillator(Location currentLocation, PointQuadTree<DefibrillatorClusterItem> pointQuadTree, Bounds maxBoundsSearch) {
        if(currentLocation == null) {
            return null;
        }

        double incrementRadian = 0.005;
        double minX = currentLocation.getLongitude() - incrementRadian;
        double maxX = currentLocation.getLongitude() + incrementRadian;
        double minY = currentLocation.getLatitude() - incrementRadian;
        double maxY = currentLocation.getLatitude() + incrementRadian;

        Bounds boundsSearch = new Bounds(minX, maxX, minY, maxY);

        ArrayList<DefibrillatorClusterItem> closeDefibrillators = (ArrayList<DefibrillatorClusterItem>)
                pointQuadTree.search(boundsSearch);
        Log.d(TAG, "Got " + closeDefibrillators.size() + " close defibrillator");

        Log.d(TAG, "Looking for defib in bounds minX:" + minX + " maxX:" + maxX + " minY:" + minY + " maxY:" + maxY);

        while(closeDefibrillators.size() < 1 &&
                (minX >= maxBoundsSearch.minX ||
                        maxX <= maxBoundsSearch.maxX ||
                        minY >= maxBoundsSearch.minY ||
                        maxY <= maxBoundsSearch.maxY)) {

            if(minX > maxBoundsSearch.minX) {
                minX -= incrementRadian;
            }
            if(maxX < maxBoundsSearch.maxX) {
                maxX += incrementRadian;
            }
            if(minY > maxBoundsSearch.minY) {
                minY -= incrementRadian;
            }
            if(maxY < maxBoundsSearch.maxY) {
                maxY += incrementRadian;
            }

            Log.d(TAG, "Looking for defib in bounds minX:" + minX + " maxX:" + maxX + " minY:" + minY + " maxY:" + maxY);
            boundsSearch = new Bounds(minX, maxX, minY, maxY);
            closeDefibrillators = (ArrayList<DefibrillatorClusterItem>)
                    pointQuadTree.search(boundsSearch);
        }

        Log.d(TAG, "After the loop Got " + closeDefibrillators.size() + " close defibrillators");

        DefibrillatorClusterItem closestDefibrillator = null;
        // If a defib has been found
        if(closeDefibrillators.size() > 0) {
            // Get the distance
            LatLng currentLocationLatLng = MapUtils.getLatLng(currentLocation);
            closestDefibrillator = closeDefibrillators.get(0);
            double minDistance = SphericalUtil.computeDistanceBetween(currentLocationLatLng, MapUtils.getLatLng(closestDefibrillator.getLatitude(), closestDefibrillator.getLongitude()));

            // If there are more than 1 defib found
            if(closeDefibrillators.size() > 1) {
                DefibrillatorClusterItem closeDefibrillator;

                // Get the closest one
                double distance;
                for (int index = 0; index < closeDefibrillators.size(); index++) {
                    closeDefibrillator = closeDefibrillators.get(index);
                    distance = SphericalUtil.computeDistanceBetween(currentLocationLatLng, MapUtils.getLatLng(closeDefibrillator.getLatitude(), closeDefibrillator.getLongitude()));
                    if(distance < minDistance) {
                        minDistance = distance;
                        closestDefibrillator = closeDefibrillator;
                    }
                }
            }
        }

        return closestDefibrillator;
    }
}
