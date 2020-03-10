package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

import java.util.Map;

public class RepositoryImpl implements Repository {

    private Map<Integer, Meal> allMeal;

    @Override
    public void addMeal(Meal meal) {
        allMeal.put(meal.getIndex(), meal);
    }

    @Override
    public void deleteMeal(int id) {
        if (allMeal.keySet().contains(id)) {
            allMeal.remove(id);
        }
    }

    @Override
    public void updateMeal(Meal meal) {
        allMeal.merge(meal.getIndex(), meal, (oldValue, newValue) -> newValue);
    }

    @Override
    public Meal getMealById(int id) {
        return allMeal.get(id);
    }

    public Map<Integer, Meal> getAll(){
        return allMeal;
    }
}
