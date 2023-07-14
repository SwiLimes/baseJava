package com.topjava.webapp.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final LocalDate NOW = LocalDate.of(2500, 1, 1);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static String toHtml(LocalDate date) {
        if (date == null) return "";
        return date.isAfter(LocalDate.now()) ? date + " - Сейчас" : date.toString();
    }

    public static LocalDate parse(String date) {
        if (HtmlUtil.isEmpty(date) || "Сейчас".equals(date)) {
            return NOW;
        }
        YearMonth yearMonth = YearMonth.parse(date, DATE_FORMATTER);
        return LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
    }
}
