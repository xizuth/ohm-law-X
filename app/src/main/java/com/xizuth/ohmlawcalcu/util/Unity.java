package com.xizuth.ohmlawcalcu.util;

import android.content.Context;
import android.util.Log;

import com.xizuth.ohmlawcalcu.R;

import static com.xizuth.ohmlawcalcu.util.Unit.CURRENT;
import static com.xizuth.ohmlawcalcu.util.Unit.KILO;
import static com.xizuth.ohmlawcalcu.util.Unit.MEGA;
import static com.xizuth.ohmlawcalcu.util.Unit.MICRO;
import static com.xizuth.ohmlawcalcu.util.Unit.MILI;
import static com.xizuth.ohmlawcalcu.util.Unit.NANO;
import static com.xizuth.ohmlawcalcu.util.Unit.RESISTANCE;
import static com.xizuth.ohmlawcalcu.util.Unit.UNIT;

/**
 * Created by josef on 8/18/17.
 */

public class Unity {

    private final Context context;
    private final double value;
    private final int type;
    private int position;
    private double result;

    public Unity(Context context, double value, int type) {
        this.context = context;
        this.value = value;
        this.type = type;
        setValue();
    }

    public double getValue() {
        return result;
    }

    private void setValue() {

        if (value > MEGA) {
            result = value / MEGA;
            position = Unit.units().length -1;
        }
        if ((value < MEGA) && (value >= KILO)) {
            result = value / KILO;
            position = Unit.units().length - 2;
        }
        if ((value >= UNIT) && (value < KILO)) {
            result = value;
            position = Unit.units().length - 3;
        }
        if ((value < UNIT) && (value >= MILI)) {
            result = value / MILI;
            position = Unit.units().length - 4;
        }
        if ((value < MILI) && (value >= MICRO)) {
            result = value / MICRO;
            position = Unit.units().length - 5;
        }
        if ((value < MICRO) && (value >= NANO)) {
            result = value / NANO;
            position = Unit.units().length - 6;
        }

    }

    public String getUnity() {
        String[] voltage = context.getResources().getStringArray(R.array.voltage_list);
        String[] current = context.getResources().getStringArray(R.array.current_list);
        String[] resistance = context.getResources().getStringArray(R.array.resistance_list);

        if (type == CURRENT) {
            return current[position];
        }
        if (type == RESISTANCE) {
            return resistance[position];
        } else {
            return voltage[position];
        }
    }

}
