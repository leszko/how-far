package com.summercoding.howfar;

public class Preconditions {
    public static void checkNotNull(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
    }
}
