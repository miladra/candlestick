package com.amazing.candlestick.utility;

import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.Date;

public class DateTimeHelper {

    private DateTimeHelper() {
    }

    public static Date applyUTCZone(Date target) {
        return Date.from(target.toInstant().atZone(ZoneOffset.UTC).toInstant());
    }

    public static Date adjustToFirstMinute(Date target) {
        return Date.from(target.toInstant().atZone(ZoneOffset.UTC)
                .withSecond(0)
                .with(ChronoField.MICRO_OF_SECOND, 0)
                .toInstant());
    }

    public static Date adjustToLastMinute(Date target) {
        return Date.from(target.toInstant().atZone(ZoneOffset.UTC)
                .withSecond(59)
                .with(ChronoField.MICRO_OF_SECOND, 999_999)
                .toInstant());
    }

    public static Date adjustToFirstNextMinute(Date target) {
        return Date.from(target.toInstant().atZone(ZoneOffset.UTC).plusMinutes(1).withSecond(0).toInstant());
    }

    public static Date plusMinutes(Date target, int minutes) {
        return Date.from(target.toInstant().atZone(ZoneOffset.UTC).plusMinutes(minutes).toInstant());
    }

    public static Date minusMinutes(Date target, int minutes) {
        return Date.from(target.toInstant().atZone(ZoneOffset.UTC).minusMinutes(minutes).toInstant());
    }

    public static Date minusSeconds(Date target, int seconds) {
        return Date.from(target.toInstant().atZone(ZoneOffset.UTC).minusSeconds(seconds).toInstant());
    }
}
