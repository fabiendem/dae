package com.fabiendem.defib68.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fabiendem.defib68.R;
import com.fabiendem.defib68.models.EnvironmentEnum;
import com.fabiendem.defib68.models.defibrillator.Defibrillator;
import com.fabiendem.defib68.models.defibrillator.DefibrillatorClusterItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by Fabien on 26/10/2014.
 */
public class DefibrillatorClusterRenderer extends DefaultClusterRenderer<DefibrillatorClusterItem> {

    public DefibrillatorClusterRenderer(Context context, GoogleMap map, ClusterManager<DefibrillatorClusterItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(DefibrillatorClusterItem defibrillator, MarkerOptions markerOptions) {
        markerOptions.title(String.valueOf(defibrillator.getId()))
                     .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
    }
}
