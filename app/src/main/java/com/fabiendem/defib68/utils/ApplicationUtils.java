package com.fabiendem.defib68.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fabiendem.defib68.models.defibrillator.DefibrillatorModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

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
