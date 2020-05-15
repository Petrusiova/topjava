package ru.javawebinar.topjava.util;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class LocalDateFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<LocalDateFormat> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Collections.singletonList(LocalDate.class));
    }

    @Override
    public Printer<?> getPrinter(LocalDateFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    @Override
    public Parser<?> getParser(LocalDateFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    private Formatter<LocalDate> configureFormatterFrom(LocalDateFormat annotation, Class<?> fieldType) {
        return new LocalDateFormatter();
    }
}
