package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Meal {
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private final int index;
    private static AtomicInteger id = new AtomicInteger(1);

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.index = getId().get();
        setId();
    }

    public AtomicInteger getId() {
        return id;
    }

    public static void setId() {
        id.incrementAndGet();
    }

    public int getIndex() {
        return index;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }
}
