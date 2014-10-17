package com.summercoding.howfar.utils;

public class Preconditions {
    public static void checkNotNull(Object object) {
        if (object == null) {
            throw new NullPointerException("Argument can't be null!");
        }
    }

    public static void greatherThanZero(int argument) {
        if (argument <= 0) {
            throw new IllegalArgumentException("Argument has to be greather than zero!");
        }
    }
}
