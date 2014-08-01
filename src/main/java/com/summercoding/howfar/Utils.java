package com.summercoding.howfar;

import java.text.DecimalFormat;

public class Utils {
    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");

    public static String formatMainText(double distance) {
        String stringDistance = DECIMAL_FORMAT.format(distance);
        return stringDistance + " km";
    }
}
