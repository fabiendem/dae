package com.fabiendem.defib68.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Fabien on 15/11/14.
 */
public class ApplicationUtils {
    /**
     * Opens Google Play's application page.
     *
     * @param activity : current activity, used to get application's package name
     */
    public static void rateTheApp(Activity activity) {
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName()));
        activity.startActivity(goToMarket);
    }
}
