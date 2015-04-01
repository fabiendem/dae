package com.fablap.defib.models.defibrillator;

import com.fablap.defib.models.EnvironmentEnum;

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

    public String getLocationDescription() {
        return mDefibrillator.getLocationDescription();
    }

    public String getCity() {
        return mDefibrillator.getCity();
    }

    public EnvironmentEnum getEnvironment() {
        return mDefibrillator.getEnvironment();
    }

    public double getLatitude() {
        return mDefibrillator.getLatitude();
    }

    public double getLongitude() {
        return mDefibrillator.getLongitude();
    }

    public String toString() {
        return mDefibrillator.toString();
    }
}
