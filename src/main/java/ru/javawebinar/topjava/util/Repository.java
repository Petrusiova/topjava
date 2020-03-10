package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

public interface Repository {
    void addMeal(Meal meal);
    void deleteMeal(int id);
    void updateMeal(Meal meal);
    Meal getMealById(int id);
}
