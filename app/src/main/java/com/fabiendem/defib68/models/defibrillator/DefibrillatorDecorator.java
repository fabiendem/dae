package com.fabiendem.defib68.models.defibrillator;

import com.fabiendem.defib68.models.EnvironmentEnum;

/**
 * Created by Fabien on 26/10/2014.
 */
public abstract class DefibrillatorDecorator implements Defibrillator {
    protected Defibrillator mDefibrillator;

    public DefibrillatorDecorator(Defibrillator defibrillator) {
        mDefibrillator = defibrillator;
    }

    public int getId() {
        return mDefibrillator.getId();
    }

    public void setId(int mId) {
        mDefibrillator.setId(mId);
    }

    public String getLocationDescription() {
        return mDefibrillator.getLocationDescription();
    }

    public void setLocationDescription(String mLocationDescription) {
        mDefibrillator.setLocationDescription(mLocationDescription);
    }

    public EnvironmentEnum getEnvironment() {
        return mDefibrillator.getEnvironment();
    }

    public void setEnvironment(EnvironmentEnum mEnvironment) {
        mDefibrillator.setEnvironment(mEnvironment);
    }

    public double getLatitude() {
        return mDefibrillator.getLatitude();
    }

    public void setLatitude(double mLatitude) {
        mDefibrillator.setLatitude(mLatitude);
    }

    public double getLongitude() {
        return mDefibrillator.getLongitude();
    }

    public void setLongitude(double mLongitude) {
        mDefibrillator.setLongitude(mLongitude);
    }

    public String toString() {
        return mDefibrillator.toString();
    }
}
