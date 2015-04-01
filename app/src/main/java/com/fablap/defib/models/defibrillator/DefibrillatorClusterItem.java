package com.fablap.defib.models.defibrillator;

import com.fablap.defib.map.MapUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.geometry.Point;
import com.google.maps.android.quadtree.PointQuadTree;

/**
 * Created by Fabien on 26/10/2014.
 */
public class DefibrillatorClusterItem extends DefibrillatorDecorator implements ClusterItem, PointQuadTree.Item {

    public DefibrillatorClusterItem(Defibrillator defibrillator) {
        super(defibrillator);
    }

    @Override
    public LatLng getPosition() {
        return MapUtils.getLatLng(getLatitude(), getLongitude());
    }

    @Override
    public Point getPoint() {
        // Don't forget, here Longitude is for the X-axis, Latitude for the Y-axis, not natural
        return new Point(getLongitude(), getLatitude());
    }
}
