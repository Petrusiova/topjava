package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class LocalTimeFormatter implements Formatter<LocalTime>  {

    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        return LocalTime.parse(text, DateTimeFormatter.ISO_TIME);
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        return DateTimeFormatter.ISO_TIME.format(object);
    }
}

