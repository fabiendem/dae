package com.fabiendem.defib68.map;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
    public static final String TAG = "DefibrillatorInfoWindowAdapter";

    private View mInfoContentsView;
    private Map<String, DefibrillatorModel> mMapDefibrillators;

    public DefibrillatorInfoWindowAdapter(View infoContentsView, Map<String, DefibrillatorModel> mapDefibrillators) {
        mInfoContentsView = infoContentsView;
        mMapDefibrillators = mapDefibrillators;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        String markerId = marker.getTitle();
        Log.d(TAG, "Marker id: " + markerId);
        final DefibrillatorModel defibrillatorModel = mMapDefibrillators.get(markerId);
        if (defibrillatorModel == null) {
            Log.e(TAG, "Defibrillator " + markerId + " unknown");
            return null;
        }

        String environment;
        if (defibrillatorModel.getEnvironment() == EnvironmentEnum.OUTDOORS) {
            environment = "Extérieur";
        } else {
            environment = "Intérieur";
        }

        TextView txtDescription = ((TextView) mInfoContentsView.findViewById(R.id.txt_description));
        TextView txtEnvironment = ((TextView) mInfoContentsView.findViewById(R.id.txt_environment));
        TextView txtAddress = ((TextView) mInfoContentsView.findViewById(R.id.txt_address));

        txtDescription.setText(defibrillatorModel.getLocationDescription());
        txtEnvironment.setText(environment);
        txtAddress.setText("Lat/Lng: " + defibrillatorModel.getLatitude() + "," + defibrillatorModel.getLongitude());

        return mInfoContentsView;
    }
}
