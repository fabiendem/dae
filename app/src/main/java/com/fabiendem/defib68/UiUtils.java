package com.fabiendem.defib68;

import android.content.Context;

/**
 * Created by Fabien on 29/10/2014.
 */
public class UiUtils {

    public static int dpToPx(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
