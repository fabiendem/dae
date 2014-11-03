package com.fabiendem.defib68.models.defibrillator;

import com.fabiendem.defib68.models.EnvironmentEnum;

/**
 * Created by Fabien on 17/06/2014.
 */
public interface Defibrillator {
    public int getId();

    public void setId(int mId);

    public String getLocationDescription();

    public void setLocationDescription(String mLocationDescription);

    public EnvironmentEnum getEnvironment();

    public void setEnvironment(EnvironmentEnum mEnvironment);

    public double getLatitude();

    public void setLatitude(double mLatitude);

    public double getLongitude();

    public void setLongitude(double mLongitude);

    @Override
    public String toString();
}
