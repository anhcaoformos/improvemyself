package com.produck.service.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for Date Time.
 */
public final class DateTimeUtils {

    private DateTimeUtils() {}

    public static LocalDate convertYearMonthStringToLocalDate(String target) {
        return LocalDate.parse(target + "-01", DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static List<LocalDate> generateLocalDateMonthsList(LocalDate startDate) {
        List<LocalDate> localDateMonthsList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

        while (!startDate.isAfter(currentDate)) {
            localDateMonthsList.add(startDate.withDayOfMonth(1));
            startDate = startDate.plusMonths(1);
        }

        return localDateMonthsList;
    }
}
