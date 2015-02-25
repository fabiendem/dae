package com.fabiendem.defib68.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.fabiendem.defib68.PreferencesManager;
import com.fabiendem.defib68.R;
import com.fabiendem.defib68.fragments.AboutFragment;
import com.fabiendem.defib68.fragments.MapFragment;
import com.fabiendem.defib68.utils.ApplicationUtils;
import com.google.android.gms.maps.GoogleMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Fabien on 06/11/14.
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Fragment mCurrentFragment;

    private ImageButton mDrawerToogleButton;
    private DrawerLayout mDrawerLayout;
    private View mDrawerContent;

    private DrawerContentClickListener mDrawerContentClickListener;
    private Button mMapTypeChooserButton;
    private Button mAboutButton;
    private Button mContactUsButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_main);

        mDrawerToogleButton = (ImageButton) findViewById(R.id.drawer_toggle_btn);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerContent = findViewById(R.id.left_drawer);

        mDrawerToogleButton.setOnClickListener(this);

        // Drawer interactions
        mDrawerContentClickListener = new DrawerContentClickListener();
        mMapTypeChooserButton = (Button) findViewById(R.id.map_type_chooser_btn);
        mMapTypeChooserButton.setOnClickListener(mDrawerContentClickListener);
        mMapTypeChooserButton.setActivated(false);
        mAboutButton = (Button) findViewById(R.id.about_btn);
        mAboutButton.setOnClickListener(mDrawerContentClickListener);
        mContactUsButton = (Button) findViewById(R.id.contact_us_btn);
        mContactUsButton.setOnClickListener(mDrawerContentClickListener);

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            mCurrentFragment = getSupportFragmentManager().getFragment(
                    savedInstanceState, "mCurrentFragment");
        }
        else {
            // During initial setup, plug in the fragment.
            mCurrentFragment = new MapFragment();
            mCurrentFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.content_frame, mCurrentFragment).commit();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        boolean isActivated = PreferencesManager.getInstance(MainActivity.this).getMapType() == GoogleMap.MAP_TYPE_HYBRID;
        updateSatelliteBtn(isActivated);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        getSupportFragmentManager().putFragment(outState, "mCurrentFragment", mCurrentFragment);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drawer_toggle_btn:
                mDrawerLayout.openDrawer(mDrawerContent);
                break;
        }
    }

    private void updateSatelliteBtn(boolean isActivated) {
        mMapTypeChooserButton.setActivated(isActivated);
        if(isActivated) {
//            mMapTypeChooserButton.setTextAppearance(this, R.style.TextAppearance_Black);
        }
        else {
  //          mMapTypeChooserButton.setTextAppearance(this, R.style.TextAppearance_Black);
        }
    }

    public void showAboutDialog() {
        AboutFragment.show(this);
    }

    private class DrawerContentClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.map_type_chooser_btn:
                    MapFragment mapFragment = (MapFragment)
                            getSupportFragmentManager().findFragmentById(R.id.content_frame);
                    if(mapFragment != null) {
                        int mapType = mapFragment.toggleMapType();
                        updateSatelliteBtn(mapType == GoogleMap.MAP_TYPE_HYBRID);
                        mDrawerLayout.closeDrawer(mDrawerContent);
                        PreferencesManager.getInstance(MainActivity.this).setMapType(mapType);
                    }
                    break;
                case R.id.contact_us_btn:
                    ApplicationUtils.launchContactUsByEmailIntent(MainActivity.this, getString(R.string.contact_title_chooser), getString(R.string.contact_email));
                    break;
                case R.id.about_btn:
                    showAboutDialog();
                    break;
            }
        }
    }
}
