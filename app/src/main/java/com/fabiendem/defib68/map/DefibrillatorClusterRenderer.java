package com.fabiendem.defib68.map;

import android.content.Context;

import com.fabiendem.defib68.R;
import com.fabiendem.defib68.models.defibrillator.DefibrillatorClusterItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

/**
 * Created by Fabien on 26/10/2014.
 */
public class DefibrillatorClusterRenderer extends DefaultClusterRenderer<DefibrillatorClusterItem> {

    public DefibrillatorClusterRenderer(Context context, GoogleMap map, ClusterManager<DefibrillatorClusterItem> clusterManager) {
        super(context, map, clusterManager);
    }

    /**
     * Called before the marker for a Cluster is added to the map.
     * The default implementation draws a circle with a rough count of the number of items.
     */
    @Override
    protected void onBeforeClusterRendered(Cluster<DefibrillatorClusterItem> cluster, MarkerOptions markerOptions) {
        int bucket = getBucket(cluster);
        BitmapDescriptor descriptor = mIcons.get(bucket);
        if (descriptor == null) {
            mColoredCircleBackground.getPaint().setColor(getColor(bucket));
            descriptor = BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon(getClusterText(bucket)));
            mIcons.put(bucket, descriptor);
        }
        // TODO: consider adding anchor(.5, .5) (Individual markers will overlap more often)
        markerOptions.icon(descriptor);
    }

    @Override
    protected void onBeforeClusterItemRendered(DefibrillatorClusterItem defibrillator, MarkerOptions markerOptions) {
        markerOptions.title(String.valueOf(defibrillator.getId()))
                     .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
    }
}
