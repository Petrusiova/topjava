package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenInclusiveTime(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }

    public static <T> boolean filterByDateAndTime(Comparable<T> lt,
                                                  Comparable<? super T> startDate,
                                                  Comparable<? super T> endDate) {
        Class<? extends Comparable> aClass = lt.getClass();
        if (startDate == null) {
            if (aClass == LocalDate.class){
                startDate = (Comparable<? super T>) LocalDate.MIN;
            } else {
                if (aClass == LocalTime.class){
                    startDate = (Comparable<? super T>) LocalTime.MIN;
                }
            }
        }
        if (endDate == null) {
            if (aClass == LocalDate.class){
                endDate = (Comparable<? super T>) LocalDate.MAX;
            } else {
                if (aClass == LocalTime.class){
                    endDate = (Comparable<? super T>) LocalTime.MAX;
                }
            }
        }

        return lt.compareTo((T) startDate) >= 0 && lt.compareTo((T) endDate) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

