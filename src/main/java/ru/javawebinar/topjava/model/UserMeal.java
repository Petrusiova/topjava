package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class UserMeal {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;
    private static Map<LocalDate, Integer> generalCalories = new HashMap<>();

    public UserMeal(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        generalCalories.merge(dateTime.toLocalDate(), calories, (oldVal, newVal) -> oldVal + newVal);

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

    public int getGeneralCalories(LocalDate date) {
        return generalCalories.getOrDefault(date, 0);
    }
}
