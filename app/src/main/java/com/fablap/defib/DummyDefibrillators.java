package com.fablap.defib;

import com.fablap.defib.models.EnvironmentEnum;
import com.fablap.defib.models.defibrillator.DefibrillatorModel;
import com.fablap.defib.utils.HautRhinUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Fabien on 25/10/2014.
 */
public class DummyDefibrillators {
    /**
     * An array of sample (dummy) items.
     */
    public static ArrayList<DefibrillatorModel> ITEMS = new ArrayList<>();

    static {
        for (int i = 0; i < 1000; i++) {
            addItem(new DefibrillatorModel(i, "Défibrillateur " + i, "Commune " + i, EnvironmentEnum.INDOORS, getRandomDouble(HautRhinUtils.BOTTOM_BOUND, HautRhinUtils.TOP_BOUND), getRandomDouble(HautRhinUtils.LEFT_BOUND, HautRhinUtils.RIGHT_BOUND)));
        }
    }

    private static void addItem(DefibrillatorModel item) {
        ITEMS.add(item);
    }

    private static double getRandomDouble(double rangeMin, double rangeMax) {
        Random r = new Random();
        return rangeMin + (rangeMax - rangeMin) * r.nextDouble();
    }
}
