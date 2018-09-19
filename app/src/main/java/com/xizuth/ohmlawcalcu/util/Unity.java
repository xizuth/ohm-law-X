package com.xizuth.ohmlawcalcu.util;

import android.content.Context;

import static com.xizuth.ohmlawcalcu.util.Unit.CURRENT;
import static com.xizuth.ohmlawcalcu.util.Unit.GIGA;
import static com.xizuth.ohmlawcalcu.util.Unit.KILO;
import static com.xizuth.ohmlawcalcu.util.Unit.MEGA;
import static com.xizuth.ohmlawcalcu.util.Unit.MICRO;
import static com.xizuth.ohmlawcalcu.util.Unit.MILI;
import static com.xizuth.ohmlawcalcu.util.Unit.NANO;
import static com.xizuth.ohmlawcalcu.util.Unit.PICO;
import static com.xizuth.ohmlawcalcu.util.Unit.POWER;
import static com.xizuth.ohmlawcalcu.util.Unit.RESISTANCE;
import static com.xizuth.ohmlawcalcu.util.Unit.UNIT;
import static com.xizuth.ohmlawcalcu.util.Unit.VOLTAGE;

/**
 * Created by josef on 8/18/17.
 */

public class Unity {

    private final Context context;
    private double value;
    private final int type;
    private int position;
    private double result;

    public Unity(Context context, double value, int type) {
        this.context = context;
        this.value = value;
        this.type = type;
        setNotation();
    }

    public double getValue() {
        return result;
    }

    private void setNotation() {
        if (value == 0) {
            position = Unit.notation().length - 4;
            return;
        }

        int negative = 1;

        if (value < 0) {
            value = Math.abs(value);
            negative = -1;
        }

        if (value >= GIGA) {
            result = value / GIGA;
            position = Unit.notation().length - 1;
        }

        if ((value >= MEGA) && (value < GIGA)) {
            result = value / MEGA;
            position = Unit.notation().length - 2;
        }
        if ((value < MEGA) && (value >= KILO)) {
            result = value / KILO;
            position = Unit.notation().length - 3;
        }
        if ((value >= UNIT) && (value < KILO)) {
            result = value;
            position = Unit.notation().length - 4;
        }
        if ((value < UNIT) && (value >= MILI)) {
            result = value / MILI;
            position = Unit.notation().length - 5;
        }
        if ((value < MILI) && (value >= MICRO)) {
            result = value / MICRO;
            position = Unit.notation().length - 6;
        }
        if ((value < MICRO) && (value >= NANO)) {
            result = value / NANO;
            position = Unit.notation().length - 7;
        }
        if ((value < NANO) && (value >= PICO)){
            result = value / PICO;
            position = Unit.notation().length - 8;
        }

        result *= negative;
    }

    public String getUnity() {

        String[] voltage = Unit.getListUnitType(context, VOLTAGE);
        String[] current = Unit.getListUnitType(context, CURRENT);
        String[] resistance = Unit.getListUnitType(context, RESISTANCE);
        String[] power = Unit.getListUnitType(context, POWER);

        if (type == CURRENT) {
            return current[position];
        } else if (type == RESISTANCE) {
            return resistance[position];
        } else if (type == VOLTAGE) {
            return voltage[position];
        } else return power[position];
    }

    @Override
    public String toString() {
        return "Unity{" +
                "context=" + context +
                ", value=" + value +
                ", type=" + type +
                ", position=" + position +
                ", result=" + result +
                '}';
    }
}
