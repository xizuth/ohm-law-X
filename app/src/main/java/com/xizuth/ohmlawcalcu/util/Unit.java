package com.xizuth.ohmlawcalcu.util;

/**
 * Created by josef on 8/18/17.
 */

public class Unit {

    public static double NANO = 0.000000001;
    public static double MICRO = 0.000001;
    public static double MILI = 0.001;
    public static int UNIT = 1;
    public static int KILO = 1000;
    public static int MEGA = 1000000;


    public static final int VOLTAGE = 0;
    public static final int CURRENT = 1;
    public static final int RESISTANCE = 2;

    private Unit() {
    }

    public static double[] units() {
        return new double[]{NANO, MICRO, MILI, UNIT, KILO, MEGA};
    }

}
