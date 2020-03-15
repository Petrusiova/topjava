package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    public void delete(int id, int userId) {
        ValidationUtil.isUsersMeal(get(id, userId), userId);
        checkNotFoundWithId(repository.delete(id), id);
    }

    public Meal get(int id, int userId) {
        Meal currentMeal = checkNotFoundWithId(repository.get(id), id);
        ValidationUtil.isUsersMeal(currentMeal, userId);
        return currentMeal;
    }

    public Collection<Meal> getAll(int userId) {
        TreeSet<Meal> mealsSet = repository.getAll().stream()
                .filter(item -> item.getUserId() == userId).collect(Collectors.toCollection(TreeSet::new));
        if (mealsSet.isEmpty()){
            throw new NotFoundException("Current user doesn't have any meal");
        }
        return mealsSet;
    }

    public void update(Meal meal, int userId) {
        ValidationUtil.isUsersMeal(meal, userId);
        checkNotFoundWithId(repository.save(meal), meal.getId());
    }
}