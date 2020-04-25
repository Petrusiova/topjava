package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.MEALS;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Autowired
    private UserService service;

    @Test
    public void getUserWithMeal() {
        List<Meal> list = service.userWithMeal(USER_ID);
        list.forEach(item ->
            USER_MATCHER.assertMatch(item.getUser(), USER)
        );
        MEAL_MATCHER.assertMatch(list, MEALS);
    }

    @Test
    public void getWrongUser() {
        Assert.assertThrows(NotFoundException.class,
                () -> service.userWithMeal(-150));
    }
}
