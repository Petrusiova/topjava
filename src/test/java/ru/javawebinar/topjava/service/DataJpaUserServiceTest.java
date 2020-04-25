package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.*;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.Profiles.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Autowired
    private UserService service;

    @Test
    public void getUserWithMeal() {
        List<Map<User, Meal>> list = service.userWithMeal(USER_ID);
        Set<AbstractBaseEntity> values = new HashSet<>();
        list.forEach(item -> values.addAll(item.values()));

        Assert.assertTrue(values.contains(USER));
        values.remove(USER);
        Assert.assertTrue(values.containsAll(MEALS));
    }

    @Test
    public void getWrongUser() {
        Assert.assertThrows(NotFoundException.class,
                () -> service.userWithMeal(-150));
    }
}
