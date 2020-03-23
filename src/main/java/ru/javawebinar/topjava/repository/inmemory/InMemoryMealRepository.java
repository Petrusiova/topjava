package ru.javawebinar.topjava.repository.inmemory;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    //userId, mealId + meal
    private Map<Integer, Map<Integer, Meal>> generalRepository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        Map<Integer, Meal> map = generalRepository.get(userId);
        if (map == null){
            map = new HashMap<>();
        }
        if (!meal.isNew() && map.get(meal.getId()) == null){
            return null;
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }
        map.put(meal.getId(), meal);
        generalRepository.put(userId, map);
        // handle case: update, but not present in storage
        return meal;
    }

    @Override
    public boolean delete(int id, Integer userId) {
        Map<Integer, Meal> map = generalRepository.get(userId);
        if (map != null && map.get(id) != null) {
            map.remove(id);
            return map.get(id) == null;
        }
        return false;
    }

    @Override
    public Meal get(int id, Integer userId) {
        Map<Integer, Meal> map = generalRepository.get(userId);
        return map != null ? map.get(id) : null;
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        Map<Integer, Meal> map = generalRepository.get(userId);
        return map != null ?
                map.values()
                        .stream()
                        .sorted((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()))
                        .collect(Collectors.toList()) : Collections.emptyList();
    }
}

