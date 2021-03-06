package com.fablap.defib.models.defibrillator;

import com.fablap.defib.models.EnvironmentEnum;

/**
 * Created by Fabien on 26/10/2014.
 */
public class DefibrillatorModel implements Defibrillator {

    private int mId;

    private String mLocationDescription;

    private String mCity;

    private EnvironmentEnum mEnvironment;

    private double mLatitude;
    private double mLongitude;

    public DefibrillatorModel(int id, String locationDescription, String city, EnvironmentEnum environment, double latitude, double longitude) {
        this.mId = id;
        this.mLocationDescription = locationDescription;
        this.mCity = city;
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

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity = city;
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

    public static class Builder {
        private int mId;
        private String mLocationDescription;
        private String mCity;
        private EnvironmentEnum mEnvironment;
        private double mLatitude;
        private double mLongitude;

        public Builder setId(int id) {
            mId = id;
            return this;
        }

        public Builder setLocationDescription(String locationDescription) {
            mLocationDescription = locationDescription;
            return this;
        }

        public Builder setCity(String city) {
            mCity = city;
            return this;
        }

        public Builder setEnvironment(EnvironmentEnum environment) {
            mEnvironment = environment;
            return this;
        }

        public Builder setLatitude(double latitude) {
            mLatitude = latitude;
            return this;
        }

        public Builder setLongitude(double longitude) {
            mLongitude = longitude;
            return this;
        }

        public DefibrillatorModel build() {
            return new DefibrillatorModel(mId, mLocationDescription, mCity, mEnvironment, mLatitude, mLongitude);
        }
    }
}
