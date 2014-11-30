package com.fabiendem.defib68.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.fabiendem.defib68.R;
import com.fabiendem.defib68.UiUtils;
import com.fabiendem.defib68.fragments.AboutFragment;
import com.fabiendem.defib68.fragments.MapFragment;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        mDrawerToogleButton = (ImageButton) findViewById(R.id.drawer_toggle_btn);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerContent = findViewById(R.id.left_drawer);

        computeDrawerWidth();

        mDrawerToogleButton.setOnClickListener(this);

        // Drawer interactions
        mDrawerContentClickListener = new DrawerContentClickListener();
        mMapTypeChooserButton = (Button) findViewById(R.id.map_type_chooser_btn);
        mMapTypeChooserButton.setOnClickListener(mDrawerContentClickListener);
        mAboutButton = (Button) findViewById(R.id.about_btn);
        mAboutButton.setOnClickListener(mDrawerContentClickListener);

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

    private void computeDrawerWidth() {
        int defaultActionBarHeight = 56;
        int actionBarHeight = UiUtils.dpToPx(this, defaultActionBarHeight);
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        int width = getResources().getDisplayMetrics().widthPixels - actionBarHeight;
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawerContent.getLayoutParams();
        params.width = width;
        mDrawerContent.setLayoutParams(params);
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

    private class DrawerContentClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.map_type_chooser_btn:
                    MapFragment mapFragment = (MapFragment)
                            getSupportFragmentManager().findFragmentById(R.id.content_frame);
                    if(mapFragment != null) {
                        int mapType = mapFragment.toggleMapType();
                        mDrawerLayout.closeDrawer(mDrawerContent);
                    }
                    break;
                case R.id.about_btn:
                    Intent aboutActivity = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(aboutActivity);
                    break;
            }
        }
    }
}
