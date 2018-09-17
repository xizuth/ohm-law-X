package com.xizuth.ohmlawcalcu.util;

import android.content.Context;

import com.xizuth.ohmlawcalcu.R;

/**
 * Created by josef on 8/18/17.
 */

public final class Unit {

    public final static double PICO = 0.000000000001;
    public final static double NANO = 0.000000001;
    public final static double MICRO = 0.000001;
    public final static double MILI = 0.001;
    public final static int UNIT = 1;
    public final static int KILO = 1000;
    public final static long MEGA = 1000000;
    public final static long GIGA = 1000000000;

    public static final int VOLTAGE = 0;
    public static final int CURRENT = 1;
    public static final int RESISTANCE = 2;
    public static final int POWER = 3;

    private Unit() {
    }

    public static double[] notation() {
        return new double[]{PICO, NANO, MICRO, MILI, UNIT, KILO, MEGA, GIGA};
    }

    public final static String[] getListUnitType(Context context, final int unit) {
        String[] unitNotation = context.getResources().getStringArray(R.array.notation_list);

        int listNotation = unitNotation.length;
        String[] unitTypeList = new String[listNotation];

        String unitType = getUnit(context, unit);

        for (int x = 0; x < listNotation; x++) {
            unitTypeList[x] = unitNotation[x] + unitType;
        }

        return unitTypeList;
    }

    private static String getUnit(Context context, final int unit) {
        if (unit == VOLTAGE)
            return context.getString(R.string.voltage);
        else if (unit == CURRENT)
            return context.getString(R.string.current);
        else if (unit == RESISTANCE)
            return context.getString(R.string.resistance);
        else
            return context.getString(R.string.power);
    }

}
