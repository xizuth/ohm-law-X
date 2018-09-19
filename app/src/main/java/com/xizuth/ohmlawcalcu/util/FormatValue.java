package com.xizuth.ohmlawcalcu.util;

public final class FormatValue {

    private FormatValue() {
    }

    public static String roundFormat(double value) {

        long valueInt = (long) value;

        if (value - valueInt != 0) {
            return String.format("%.2f", value);
        }

        return String.format("%d", valueInt);
    }
}
