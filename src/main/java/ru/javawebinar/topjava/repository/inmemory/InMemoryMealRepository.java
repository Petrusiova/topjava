package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(MEAL -> save(MEAL, MEAL.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (isUsersMeal(meal, userId)){
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
                repository.put(meal.getId(), meal);
                return meal;
            } else {
                return repository.merge(meal.getId(), meal, (oldVal, newVal) -> newVal);
            }
        }
        // handle case: update, but not present in storage
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return isUsersMeal(repository.get(id), userId) && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal currentMeal = repository.get(id);
        return isUsersMeal(currentMeal, userId) ? currentMeal : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        Set<Meal> meals = new TreeSet<>((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()));
        meals.addAll(repository.values()
                .stream()
                .filter(item -> isUsersMeal(item, userId))
                .collect(Collectors.toSet()));
        return meals;
    }

    private boolean isUsersMeal(Meal currentMeal, int userId){
        return currentMeal.getUserId() == userId;
    }
}

