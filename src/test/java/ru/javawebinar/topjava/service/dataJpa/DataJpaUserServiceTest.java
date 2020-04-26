package ru.javawebinar.topjava.service.dataJpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Autowired
    private UserService service;

    @Test
    public void getUserWithMeal() {
        User user = service.userWithMeal(USER_ID);
        USER_MATCHER.assertMatch(user, USER);
        MEAL_MATCHER.assertMatch(user.getMeals(), MEALS);
    }

    @Test
    public void getWrongUser() {
        Assert.assertThrows(NotFoundException.class,
                () -> service.userWithMeal(-150));
    }

    @Test
    public void getUserWithoutMeals(){
        User user = service.create(UserTestData.getNew());
        Assert.assertTrue(service.userWithMeal(user.getId()).getMeals().isEmpty());
    }
}
