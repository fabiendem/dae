package com.fablap.defib.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fablap.defib.PreferencesManager;
import com.fablap.defib.R;
import com.fablap.defib.fragments.AboutDialogFragment;
import com.fablap.defib.fragments.InCaseOfEmergencyDialogFragment;
import com.fablap.defib.fragments.MapFragment;
import com.fablap.defib.utils.ApplicationUtils;
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
    private Button mRateAppButton;
    private Button mAboutButton;
    private Button mContactUsButton;
    private Button mInCaseEmergencyButton;
    private TextView mVersionNameTv;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_main);

        mDrawerToogleButton = (ImageButton) findViewById(R.id.drawer_toggle_btn);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerContent = findViewById(R.id.left_drawer);

        mDrawerToogleButton.setOnClickListener(this);

        mVersionNameTv = (TextView) findViewById(R.id.version_name);
        mVersionNameTv.setText(
                getString(R.string.app_name) +
                " " +
                ApplicationUtils.getVersionName(this, getString(R.string.unknown_version)));

        // Drawer interactions
        mDrawerContentClickListener = new DrawerContentClickListener();
        mMapTypeChooserButton = (Button) findViewById(R.id.map_type_chooser_btn);
        mInCaseEmergencyButton = (Button) findViewById(R.id.in_case_emergency_btn);
        mAboutButton = (Button) findViewById(R.id.about_btn);
        mRateAppButton = (Button) findViewById(R.id.rate_app_btn);
        mContactUsButton = (Button) findViewById(R.id.contact_us_btn);
        // Connect it
        mMapTypeChooserButton.setOnClickListener(mDrawerContentClickListener);
        mInCaseEmergencyButton.setOnClickListener(mDrawerContentClickListener);
        mAboutButton.setOnClickListener(mDrawerContentClickListener);
        mContactUsButton.setOnClickListener(mDrawerContentClickListener);
        mRateAppButton.setOnClickListener(mDrawerContentClickListener);

        mMapTypeChooserButton.setActivated(false);

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
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
            mMapTypeChooserButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_satellite_green500_24dp, 0, 0, 0);
        }
        else {
            mMapTypeChooserButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_satellite_grey600_24dp, 0, 0, 0);
        }
    }

    public void showInCaseOfEmergencyDialog() {
        InCaseOfEmergencyDialogFragment.show(this);
    }

    public void showAboutDialog() {
        AboutDialogFragment.show(this);
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
                case R.id.in_case_emergency_btn:
                    showInCaseOfEmergencyDialog();
                    break;
                case R.id.contact_us_btn:
                    ApplicationUtils.launchContactUsByEmailIntent(MainActivity.this, getString(R.string.contact_title_chooser), getString(R.string.contact_email));
                    break;
                case R.id.rate_app_btn:
                    ApplicationUtils.rateTheApp(MainActivity.this);
                    break;
                case R.id.about_btn:
                    showAboutDialog();
                    break;
            }
        }
    }
}
