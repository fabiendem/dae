package com.fabiendem.defib68.models.defibrillator;

import com.fabiendem.defib68.models.EnvironmentEnum;

/**
 * Created by Fabien on 17/06/2014.
 */
public interface Defibrillator {
    public int getId();

    public String getLocationDescription();

    public EnvironmentEnum getEnvironment();

    public double getLatitude();

    public double getLongitude();

    @Override
    public String toString();
}
