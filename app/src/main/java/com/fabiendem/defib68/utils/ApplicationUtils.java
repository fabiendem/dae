package com.fabiendem.defib68.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Fabien on 15/11/14.
 */
public class ApplicationUtils {
    /**
     * Opens Google Play's application page.
     *
     * @param activity : current activity, used to get application's package name
     */
    public static void rateTheApp(@NonNull Activity activity) {
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName()));
        activity.startActivity(goToMarket);
    }

    public static void launchContactUsByEmailIntent(Context context,
                                                    String emailClientChooserAlertTitle,
                                                    String emailDestination) {
        launchContactUsByEmailIntent(context, emailClientChooserAlertTitle, emailDestination, null, null);
    }

    public static void launchContactUsByEmailIntent(Context context,
                                                    String emailClientChooserAlertTitle,
                                                    String emailDestination,
                                                    String subject,
                                                    String content) {

        // Build up the intent
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");

        // Put extra information into the Intent, including the email address
        // that you wish to send to, and any subject (optional, of course).
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailDestination});

        if(subject != null) {
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        }
        if(content != null) {
            emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        }

        // Start the Intent, which will launch the user's email
        // app (make sure you save any necessary information in YOUR app
        // in your onPause() method, as launching the email Intent will
        // pause your app). This will create a
        // popup box that the user can use to determine which app they would like
        // to use in order to send the email.
        context.startActivity(Intent.createChooser(emailIntent, emailClientChooserAlertTitle));
    }

    public static void launchLocationSettingsIntent(Context context) {
        Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(viewIntent);
    }

    @Nullable
    public static String loadJSONFromAsset(@NonNull Context context, @NonNull String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
