package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> map = new HashMap<>();
        meals.forEach(item ->
            map.merge(item.getDateTime().toLocalDate(), item.getCalories(), (oldVal, newVal) -> oldVal + newVal));

        List<UserMealWithExcess> result = new ArrayList<>();
        meals.forEach(item -> {
            LocalDateTime dateTime = item.getDateTime();
            if (TimeUtil.isBetweenHalfOpen(dateTime.toLocalTime(), startTime, endTime))
                result.add(new UserMealWithExcess(dateTime, item.getDescription(), item.getCalories(),
                        caloriesPerDay >= map.getOrDefault(dateTime.toLocalDate(), 0)));
        });

        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> map = meals.stream().collect(Collectors.toMap(
                item -> item.getDateTime().toLocalDate(),
                UserMeal::getCalories,
                (calories1, calories2) -> calories1 + calories2));

        return meals.stream()
                .filter(item -> TimeUtil.isBetweenHalfOpen(item.getDateTime().toLocalTime(), startTime, endTime))
                .map(item -> new UserMealWithExcess(item.getDateTime(), item.getDescription(), item.getCalories(),
                        caloriesPerDay >= map.getOrDefault(item.getDateTime().toLocalDate(), 0)))
                .collect(Collectors.toList());
    }
}
