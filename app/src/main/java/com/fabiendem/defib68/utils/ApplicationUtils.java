package com.fabiendem.defib68.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.fabiendem.defib68.PreferencesManager;
import com.fabiendem.defib68.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Fabien on 15/11/14.
 */
public class ApplicationUtils {

    public static final int REQUEST_CODE_ALERT_RATING = 998899;

    public void showRatingPromptIfNeeded(FragmentActivity activity) {
        if(! PreferencesManager.getInstance(activity).shouldNeverShowRatingPrompt()) {
            SimpleDialogFragment.createBuilder(activity, activity.getSupportFragmentManager())
                    .setTitle(R.string.rate_app_alert_title)
                    .setMessage(R.string.rate_app_alert_details)
                    .setPositiveButtonText(R.string.rate_app_alert_positive)
                    .setNeutralButtonText(R.string.rate_app_alert_neutral)
                    .setNegativeButtonText(R.string.rate_app_alert_negative)
                    .setRequestCode(REQUEST_CODE_ALERT_RATING)
                    .show();
        }
    }

    public void onRatingPromptNegativeClick(Context context) {
        PreferencesManager.getInstance(context).setShouldNeverShowRatingPrompt(true);
    }

    public void onRatingPromptPositiveClick(Context context) {
        rateTheApp(context);
    }

    /**
     * Opens Google Play's application page.
     *
     * @param context : current context, used to get application's package name
     */
    public static void rateTheApp(@NonNull Context context) {
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName()));
        context.startActivity(goToMarket);
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
