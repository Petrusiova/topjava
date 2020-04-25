package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {
    @Autowired
    private MealService service;

    @Test
    public void getMealWithUser() {
        Meal meal = service.mealWithUser(MEAL1_ID, USER_ID);
        MEAL_MATCHER.assertMatch(meal, MEAL1);
        USER_MATCHER.assertMatch(meal.getUser(), USER);
    }

    @Test
    public void getMealWithWrongUser() {
        Assert.assertThrows(NotFoundException.class,
                () -> service.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void getWrongMealWithUser() {
        Assert.assertThrows(NotFoundException.class,
                () -> service.get(-150, ADMIN_ID));
    }
}
