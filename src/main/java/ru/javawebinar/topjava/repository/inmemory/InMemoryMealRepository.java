package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        } else {
            if (isUsersMeal(meal, userId))
                return repository.merge(meal.getId(), meal, (oldVal, newVal) -> newVal);
        }
        // handle case: update, but not present in storage
        return null;
    }

    @Override
    public boolean delete(int id, Integer userId) {
        return isUsersMeal(repository.get(id), userId) && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, Integer userId) {
        Meal currentMeal = repository.get(id);
        return isUsersMeal(currentMeal, userId) ? currentMeal : null;
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        return repository.values()
                .stream()
                .filter(item -> isUsersMeal(item, userId))
                .sorted((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()))
                .collect(Collectors.toList());
    }

    private boolean isUsersMeal(Meal currentMeal, int userId) {
        return currentMeal != null && currentMeal.getUserId() == userId;
    }
}

