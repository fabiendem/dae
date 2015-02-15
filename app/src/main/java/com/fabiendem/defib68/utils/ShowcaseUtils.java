package com.fabiendem.defib68.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fabiendem.defib68.R;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

/**
 * Created by Fabien on 15/02/15.
 */
public class ShowcaseUtils {
    private static final long SHOWCASE_1 = 1;
    private static final long SHOWCASE_2 = 2;

    public static void showShowcaseHelp(final Activity activity, View closestDefibButton, final View hautRhinButton) {
        ShowcaseView.Builder svClosestDefibBuilder = getClosestDefibShowcaseViewBuilder(activity, closestDefibButton);
        ShowcaseView svClosestDefib = svClosestDefibBuilder.setShowcaseEventListener(new OnShowcaseEventListener() {

            @Override
            public void onShowcaseViewShow(final ShowcaseView scv) { }

            @Override
            public void onShowcaseViewHide(final ShowcaseView scv) {
                scv.setVisibility(View.GONE);
                showHautRhinShowcaseView(activity, hautRhinButton);
            }

            @Override
            public void onShowcaseViewDidHide(final ShowcaseView scv) { }

        }).build();
        setButtonPositionBottomLeft(activity, svClosestDefib);
    }

    private static void showHautRhinShowcaseView(Activity activity, View buttonTarget) {
        ShowcaseView sv = getHautRhinShowcaseViewBuilder(activity, buttonTarget).build();
        setButtonPositionBottomLeft(activity, sv);
    }

    private static ShowcaseView.Builder getClosestDefibShowcaseViewBuilder(Activity activity, View buttonTarget) {
        ViewTarget target = new ViewTarget(buttonTarget);
        ShowcaseView.Builder svBuilder = new ShowcaseView.Builder(activity)
                .setTarget(target)
                .setContentTitle("Tip 1/2: Closest defibrillator")
                .setContentText("Use this button to quickly find the closest defibrillator from your current location")
                .setStyle(R.style.CustomShowcaseTheme)
                .hideOnTouchOutside()
                .singleShot(SHOWCASE_1);
        return svBuilder;
    }

    private static ShowcaseView.Builder getHautRhinShowcaseViewBuilder(Activity activity, View buttonTarget) {
        ViewTarget target = new ViewTarget(buttonTarget);
        ShowcaseView.Builder svBuilder = new ShowcaseView.Builder(activity)
                .setTarget(target)
                .setContentTitle("Tip 2/2: Show Haut-Rhin")
                .setContentText("Tap on the 68 to zoom and center the map on the Haut-Rhin")
                .setStyle(R.style.CustomShowcaseTheme)
                .hideOnTouchOutside()
                .singleShot(SHOWCASE_2);
        return svBuilder;
    }

    private static RelativeLayout.LayoutParams getButtonPositinBottomLeft(Activity activity) {
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (activity.getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);
        return lps;
    }

    private static void setButtonPositionBottomLeft(Activity activity, ShowcaseView showcaseView) {
        showcaseView.setButtonPosition(getButtonPositinBottomLeft(activity));
    }
}
