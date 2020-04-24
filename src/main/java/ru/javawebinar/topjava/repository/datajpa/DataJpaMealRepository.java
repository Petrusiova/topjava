package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    @Autowired
    private CrudMealRepository crudMealRepository;

    @Autowired
    private CrudUserRepository crudUserRepository;


    public Meal save(Meal meal, int userId) {
        Integer mealId = meal.getId();
        if (mealId == null || get(mealId, userId) != null){
            meal.setUser(crudUserRepository.getOne(userId));
            return crudMealRepository.save(meal);
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudMealRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudMealRepository.get(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudMealRepository.getAll(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudMealRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }

    @Override
    public User userWithMeal(int userId){
        User user = crudUserRepository.findById(userId).orElse(null);
        List<Meal> meals = crudMealRepository.getAll(userId);
        if (meals != null) {
            user.setMeals(meals);
        }
        return user;
    }

    @Override
    public Meal mealWithUser(int id, int userId) {
        Meal meal = get(id, userId);
        meal.setUser(crudUserRepository.findById(userId).orElse(null));
        return meal;
    }
}
