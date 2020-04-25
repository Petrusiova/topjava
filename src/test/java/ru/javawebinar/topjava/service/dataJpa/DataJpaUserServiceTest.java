package ru.javawebinar.topjava.service.dataJpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.service.UserServiceTest;

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
        Assert.assertNull(service.userWithMeal(-150));
    }

    @Test
    public void getUserWithoutMeals(){
        User user = service.create(getNew());
        Assert.assertNull(service.userWithMeal(user.getId()));
    }
}
