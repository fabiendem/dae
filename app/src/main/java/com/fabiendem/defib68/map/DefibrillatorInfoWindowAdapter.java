package com.fabiendem.defib68.map;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fabiendem.defib68.PreferencesManager;
import com.fabiendem.defib68.R;
import com.fabiendem.defib68.models.EnvironmentEnum;
import com.fabiendem.defib68.models.defibrillator.DefibrillatorModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.Map;

/**
 * Created by Fabien on 01/12/14.
 */
public class DefibrillatorInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    public static final String TAG = "DefibInfoWindowAdapter";

    private Context mContext;
    private View mInfoContentsView;
    private Map<String, DefibrillatorModel> mMapDefibrillators;

    // Dirty way to pass marker specific info, should create our own marker to pass those info
    private String mClosestMarkerId;
    private long mDistanceLocationDefib;

    private String getClosestMarkerId() {
        return mClosestMarkerId;
    }

    public void setClosestMarkerId(String closestMarkerId) {
        mClosestMarkerId = closestMarkerId;
    }

    private long getDistanceLocationDefib() {
        return mDistanceLocationDefib;
    }

    public void setDistanceLocationDefib(long distanceLocationDefib) {
        mDistanceLocationDefib = distanceLocationDefib;
    }

    public DefibrillatorInfoWindowAdapter(Context context, View infoContentsView, Map<String, DefibrillatorModel> mapDefibrillators) {
        mContext = context;
        mInfoContentsView = infoContentsView;
        mMapDefibrillators = mapDefibrillators;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        // Maker ID is actually the marker title
        String markerId = marker.getTitle();
        Log.d(TAG, "Marker id: " + markerId);

        final DefibrillatorModel defibrillatorModel = mMapDefibrillators.get(markerId);
        if (defibrillatorModel == null) {
            Log.e(TAG, "Defibrillator " + markerId + " unknown");
            return null;
        }

        String environment;
        if (defibrillatorModel.getEnvironment() == EnvironmentEnum.OUTDOORS) {
            environment = mContext.getString(R.string.environment_outdoors);
        } else {
            environment = mContext.getString(R.string.environment_indoors);
        }

        TextView txtTipDirections = ((TextView) mInfoContentsView.findViewById(R.id.txt_tip_directions));
        TextView txtTipClosest = ((TextView) mInfoContentsView.findViewById(R.id.txt_tip_closest));
        TextView txtDescription = ((TextView) mInfoContentsView.findViewById(R.id.txt_description));
        TextView txtEnvironment = ((TextView) mInfoContentsView.findViewById(R.id.txt_environment));
        TextView txtCity = ((TextView) mInfoContentsView.findViewById(R.id.txt_city));
        TextView txtDistance = ((TextView) mInfoContentsView.findViewById(R.id.txt_distance));

        txtDescription.setText(defibrillatorModel.getLocationDescription());
        txtEnvironment.setText(environment);
        txtCity.setText(defibrillatorModel.getCity());

        if(! PreferencesManager.getInstance(mContext).hasTipInfoWindowShowDirectionsBeenShown()) {
            txtTipDirections.setVisibility(View.VISIBLE);
        }
        else {
            txtTipDirections.setVisibility(View.GONE);
        }

        if(getClosestMarkerId() != null &&
                getClosestMarkerId().equals(markerId)) {
            txtTipClosest.setVisibility(View.VISIBLE);
            txtDistance.setText(mContext.getString(R.string.at_distance, MapUtils.getDistanceFormatted(getDistanceLocationDefib())));
            txtDistance.setVisibility(View.VISIBLE);
        }
        else {
            txtTipClosest.setVisibility(View.GONE);
            txtDistance.setVisibility(View.GONE);
        }

        return mInfoContentsView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
