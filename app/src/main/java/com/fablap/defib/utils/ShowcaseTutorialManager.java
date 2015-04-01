package com.fablap.defib.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fablap.defib.R;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

/**
 * Created by Fabien on 15/03/15.
 */
public class ShowcaseTutorialManager {
    private static final long SHOWCASE_1 = 1;
    private static final long SHOWCASE_2 = 2;
    private static final long SHOWCASE_3 = 3;

    private Activity mActivity;
    private View mClosestDefibBtn;
    private View mHautRhinBtn;
    private View mLocationBtn;
    private OnShowcaseEventListener mOnShowcaseEventListener;

    public ShowcaseTutorialManager(Activity activity, View closestDefibButton, View hautRhinButton, View locationButton, OnShowcaseEventListener onShowcaseEventListener) {
        mActivity = activity;
        mClosestDefibBtn = closestDefibButton;
        mHautRhinBtn = hautRhinButton;
        mLocationBtn = locationButton;
        mOnShowcaseEventListener = onShowcaseEventListener;
    }

    public void showTutorial() {
        showShowcaseHelp();
    }

    private void showShowcaseHelp() {
        ShowcaseView.Builder svClosestDefibBuilder = getClosestDefibShowcaseViewBuilder();
        ShowcaseView svClosestDefib = svClosestDefibBuilder.setShowcaseEventListener(new OnShowcaseEventListener() {

            @Override
            public void onShowcaseViewShow(final ShowcaseView scv) {
                mOnShowcaseEventListener.onShowcaseViewShow(scv);
            }

            @Override
            public void onShowcaseViewHide(final ShowcaseView scv) {
                showHautRhinShowcaseView();
            }

            @Override
            public void onShowcaseViewDidHide(final ShowcaseView scv) { }

        }).build();
        setButtonPositionBottomLeft(svClosestDefib);
    }

    private void showHautRhinShowcaseView() {
        ShowcaseView.Builder svHautRhinBuilder = getHautRhinShowcaseViewBuilder();
        svHautRhinBuilder.setShowcaseEventListener(new OnShowcaseEventListener() {
            @Override
            public void onShowcaseViewHide(ShowcaseView showcaseView) {
                showLocationShowcaseView();
            }

            @Override
            public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
            }

            @Override
            public void onShowcaseViewShow(ShowcaseView showcaseView) {

            }
        });
        ShowcaseView sv = svHautRhinBuilder.build();
        setButtonPositionBottomLeft(sv);
    }

    private void showLocationShowcaseView() {
        ShowcaseView.Builder svLocationBuilder = getLocationShowcaseViewBuilder();
        svLocationBuilder.setShowcaseEventListener(new OnShowcaseEventListener() {
            @Override
            public void onShowcaseViewHide(ShowcaseView showcaseView) {

            }

            @Override
            public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                mOnShowcaseEventListener.onShowcaseViewDidHide(showcaseView);
            }

            @Override
            public void onShowcaseViewShow(ShowcaseView showcaseView) {

            }
        });
        ShowcaseView sv = svLocationBuilder.build();
        setButtonPositionBottomLeft(sv);
    }

    private ShowcaseView.Builder getClosestDefibShowcaseViewBuilder() {
        ViewTarget target = new ViewTarget(mClosestDefibBtn);
        return new ShowcaseView.Builder(mActivity, true)
                .setTarget(target)
                .setContentTitle(R.string.tuto_title_closest)
                .setContentText(R.string.tuto_details_closest)
                .setStyle(R.style.CustomShowcaseTheme)
                .hideOnTouchOutside()
                .singleShot(SHOWCASE_1);
    }

    private ShowcaseView.Builder getHautRhinShowcaseViewBuilder() {
        ViewTarget target = new ViewTarget(mHautRhinBtn);
        return new ShowcaseView.Builder(mActivity, true)
                .setTarget(target)
                .setContentTitle(R.string.tuto_title_68)
                .setContentText(R.string.tuto_details_68)
                .setStyle(R.style.CustomShowcaseTheme)
                .hideOnTouchOutside()
                .singleShot(SHOWCASE_2);
    }


    private ShowcaseView.Builder getLocationShowcaseViewBuilder() {
        ViewTarget target = new ViewTarget(mLocationBtn);
        return new ShowcaseView.Builder(mActivity, true)
                .setTarget(target)
                .setContentTitle(R.string.tuto_title_location)
                .setContentText(R.string.tuto_details_location)
                .setStyle(R.style.CustomShowcaseTheme)
                .hideOnTouchOutside()
                .singleShot(SHOWCASE_3);
    }

    private RelativeLayout.LayoutParams getButtonPositionBottomLeft() {
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        int marginLeft = UiUtils.dpToPx(mActivity, 24);
        int marginTop = UiUtils.dpToPx(mActivity, 250);
        lps.setMargins(marginLeft, marginTop, 0, 0);
        return lps;
    }

    private void setButtonPositionBottomLeft(ShowcaseView showcaseView) {
        showcaseView.setButtonPosition(getButtonPositionBottomLeft());
    }
}
