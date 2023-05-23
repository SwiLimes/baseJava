package com.topjava.webapp.util;

import java.time.LocalDate;
import java.time.Month;

public class DateUtil {
    public static final LocalDate NOW = LocalDate.of(2500, 1, 1);

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static String toHtml(LocalDate start, LocalDate end) {
        if (end.isAfter(LocalDate.now())) {
            return start.toString() + " - Сейчас";
        }
        return start + " - " + end;
    }
}
