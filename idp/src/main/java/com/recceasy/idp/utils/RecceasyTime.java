package com.recceasy.idp.utils;

public class RecceasyTime {

    public static long now() {
        return System.currentTimeMillis();
    }

    public static long addMinutes(int minutes) {
        assert minutes >= 0;
        assert minutes <= 60;
        return now() + minutes * 60 * 1000;
    }

    public static long addHours(int hours) {
        assert hours >= 0;
        assert hours <= 24;
        return now() + hours * 60 * 60 * 1000;
    }


}
