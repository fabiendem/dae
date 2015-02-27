package com.fabiendem.defib68.utils;

import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by Fabien on 27/02/15.
 */
public class AnimUtils {
    public static void translateY(View view, float startY, float endY, int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", startY, endY);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }
}
