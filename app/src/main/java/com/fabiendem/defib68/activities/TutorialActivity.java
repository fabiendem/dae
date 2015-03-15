package com.fabiendem.defib68.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fabiendem.defib68.R;
import com.fabiendem.defib68.adapters.TutorialPagerAdapter;

import github.chenupt.springindicator.SpringIndicator;

/**
 * Created by Fabien on 07/03/15.
 */
public class TutorialActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private TutorialPagerAdapter mTutorialPagerAdapter;
    private ViewPager mViewPager;
    private TextView mTvSwipeTutorial;
    private Button mBtnContinue;
    private Button mBtnSkip;
    private SpringIndicator mSpringIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mSpringIndicator = (SpringIndicator) findViewById(R.id.indicator);
        mTvSwipeTutorial = (TextView) findViewById(R.id.tv_swipe_tutorial);
        mBtnSkip = (Button) findViewById(R.id.btn_skip);
        mBtnContinue = (Button) findViewById(R.id.btn_continue);

        mBtnSkip.setOnClickListener(this);
        mBtnContinue.setOnClickListener(this);

        mTutorialPagerAdapter = new TutorialPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTutorialPagerAdapter);

        mSpringIndicator.setViewPager(mViewPager);
        mSpringIndicator.setOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 2) {
            mBtnContinue.setVisibility(View.VISIBLE);
        }
        else {
            mBtnContinue.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void goToMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
            case R.id.btn_skip:
                goToMainActivity();
                break;
        }
    }
}
