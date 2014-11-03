package com.fabiendem.defib68.models.defibrillator;

import com.fabiendem.defib68.models.EnvironmentEnum;

/**
 * Created by Fabien on 26/10/2014.
 */
public class DefibrillatorModel implements Defibrillator {
    private int mId;
    private String mLocationDescription;
    private EnvironmentEnum mEnvironment;
    private double mLatitude;
    private double mLongitude;

    public DefibrillatorModel(int id, String locationDescription, EnvironmentEnum environment, double latitude, double longitude) {
        this.mId = id;
        this.mLocationDescription = locationDescription;
        this.mEnvironment = environment;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getLocationDescription() {
        return mLocationDescription;
    }

    public void setLocationDescription(String mLocationDescription) {
        this.mLocationDescription = mLocationDescription;
    }

    public EnvironmentEnum getEnvironment() {
        return mEnvironment;
    }

    public void setEnvironment(EnvironmentEnum mEnvironment) {
        this.mEnvironment = mEnvironment;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    @Override
    public String toString() {
        return "ID: " + mId;
    }
}
