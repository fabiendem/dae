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

    public DefibrillatorInfoWindowAdapter(Context context, View infoContentsView, Map<String, DefibrillatorModel> mapDefibrillators) {
        mContext = context;
        mInfoContentsView = infoContentsView;
        mMapDefibrillators = mapDefibrillators;
    }

    @Override
    public View getInfoWindow(Marker marker) {
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
        TextView txtDescription = ((TextView) mInfoContentsView.findViewById(R.id.txt_description));
        TextView txtEnvironment = ((TextView) mInfoContentsView.findViewById(R.id.txt_environment));
        TextView txtCity = ((TextView) mInfoContentsView.findViewById(R.id.txt_city));

        txtDescription.setText(defibrillatorModel.getLocationDescription());
        txtEnvironment.setText(environment);
        txtCity.setText(defibrillatorModel.getCity());

        if(! PreferencesManager.getInstance(mContext).hasTipInfoWindowShowDirectionsBeenShown()) {
            txtTipDirections.setVisibility(View.VISIBLE);
        }
        else {
            txtTipDirections.setVisibility(View.GONE);
        }

        return mInfoContentsView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
