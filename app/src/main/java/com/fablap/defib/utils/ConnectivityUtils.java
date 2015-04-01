package com.fablap.defib.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by Fabien on 27/02/15.
 */
public class ConnectivityUtils {

    /**
     * Gets the state of Airplane Mode.
     *
     * @param context
     * @return true if enabled.
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        }
        else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    /**
     *
     * Requires permissions:
     *  <uses-permission android:name="android.permission.INTERNET" />
     *  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @param context
     * @return
     */
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return  activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
