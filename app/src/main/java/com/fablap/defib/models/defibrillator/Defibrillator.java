package com.fablap.defib.models.defibrillator;

import com.fablap.defib.models.EnvironmentEnum;

/**
 * Created by Fabien on 17/06/2014.
 */
public interface Defibrillator {
    public int getId();

    public String getLocationDescription();

    public String getCity();

    public EnvironmentEnum getEnvironment();

    public double getLatitude();

    public double getLongitude();

    @Override
    public String toString();
}
