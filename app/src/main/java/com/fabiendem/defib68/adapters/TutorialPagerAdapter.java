package com.fabiendem.defib68.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fabiendem.defib68.R;
import com.fabiendem.defib68.fragments.TutorialFragment;

/**
 * Created by Fabien on 07/03/15.
 */
public class TutorialPagerAdapter extends FragmentPagerAdapter {
    public final static int NUM_PAGES = 3;

    public TutorialPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TutorialFragment.newInstance(position, R.string.tip_closest_defib);
            case 1:
                return TutorialFragment.newInstance(position, R.string.tip_location);
            case 2:
                return TutorialFragment.newInstance(position, R.string.tip_haut_rhin);
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return position + 1 + "";
    }
}
