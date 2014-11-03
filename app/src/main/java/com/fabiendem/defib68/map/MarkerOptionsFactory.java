package com.fabiendem.defib68.map;

import com.fabiendem.defib68.models.defibrillator.Defibrillator;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Fabien on 25/10/2014.
 */
public class MarkerOptionsFactory {
    public static MarkerOptions createFromDefibrillator(Defibrillator defibrillator) {
        return new MarkerOptions().position(MapUtils.getLatLng(defibrillator.getLatitude(), defibrillator.getLongitude()))
                .title(String.valueOf(defibrillator.getId()))
                .snippet(defibrillator.getLocationDescription())
                .alpha(0.7f);
    }


}
