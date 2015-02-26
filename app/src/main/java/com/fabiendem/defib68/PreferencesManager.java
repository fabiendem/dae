package com.fabiendem.defib68;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by Fabien on 23/02/15.
 */
public class PreferencesManager {
    public static final String PREFERENCES_STORAGE_NAME = BuildConfig.APPLICATION_ID + ".preferences";
    private static PreferencesManager sInstance;
    private final SharedPreferences mSharedPreferences;

    private static final String MAP_TYPE = "MAP_TYPE";

    private static final String HAS_TIP_INFO_WINDOW_SHOW_DIRECTIONS_BEEN_SHOWN = "HAS_TIP_INFO_WINDOW_SHOW_DIRECTIONS_BEEN_SHOWN";

    private PreferencesManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCES_STORAGE_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized PreferencesManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
        return sInstance;
    }

    public void setMapType(int mapType) {
        mSharedPreferences.edit()
                .putInt(MAP_TYPE, mapType)
                .apply();
    }

    public int getMapType() {
        return mSharedPreferences.getInt(MAP_TYPE, GoogleMap.MAP_TYPE_NORMAL);
    }

    public void setHasTipInfoWindowShowDirectionsBeenShown (boolean hasTipInfoWindowShowDirectionsBeenShown) {
        mSharedPreferences.edit()
                .putBoolean(HAS_TIP_INFO_WINDOW_SHOW_DIRECTIONS_BEEN_SHOWN, hasTipInfoWindowShowDirectionsBeenShown)
                .apply();
    }

    public boolean hasTipInfoWindowShowDirectionsBeenShown() {
        return mSharedPreferences.getBoolean(HAS_TIP_INFO_WINDOW_SHOW_DIRECTIONS_BEEN_SHOWN, false);
    }
}
