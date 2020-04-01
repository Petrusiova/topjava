package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        assertMatch(service.get(USER_MEAL_ID, USER_ID), MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(USER_MEAL_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        service.delete(ADMIN_MEAL_ID, ADMIN_ID);
        service.get(ADMIN_MEAL_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(USER_MEAL_ID, ADMIN_ID);
    }

    @Test
    public void getBetweenHalfOpenNoBorders() {
        service.create(getNew(), USER_ID);
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, service.getBetweenHalfOpen(null, null, USER_ID));
    }

    @Test
    public void getBetweenHalfOpenOnlyFrom(){
        LocalDate from = LocalDate.of(2020, Month.JANUARY, 31);
        List<Meal> onlyFrom = service.getBetweenHalfOpen(from, null, USER_ID);
        onlyFrom.stream().map(Meal::getDateTime).forEach(dateTime -> {
            LocalDateTime fromDateStart = from.atStartOfDay();
            Assert.assertTrue(dateTime.isAfter(fromDateStart) || dateTime.isEqual(fromDateStart));
        });
    }

    @Test
    public void getBetweenHalfOpenOnlyTo(){
        LocalDate to = LocalDate.of(2020, Month.JANUARY, 31);
        List<Meal> onlyTo = service.getBetweenHalfOpen(null, to, USER_ID);
        onlyTo.stream().map(Meal::getDateTime).forEach(dateTime ->
                Assert.assertTrue(dateTime.isBefore(to.atTime(23, 59))));
    }

    @Test
    public void getBetweenHalfOpenTwoBorders(){
        LocalDate from = LocalDate.of(2020, Month.JANUARY, 31);
        LocalDate to = LocalDate.of(2020, Month.JANUARY, 31);

        List<Meal> fromAndTo = service.getBetweenHalfOpen(from, to, USER_ID);
        fromAndTo.stream().map(Meal::getDateTime).forEach(dateTime -> {
            LocalDateTime fromDateStart = from.atStartOfDay();
            Assert.assertTrue((dateTime.isAfter(fromDateStart) || dateTime.isEqual(fromDateStart) &&
                    dateTime.isBefore(to.atTime(23, 59))));
        });
    }

    @Test
    public void getAll() {
        List<Meal> testMealsList = Arrays.asList(MEAL_1, MEAL_2, MEAL_3, MEAL_4, MEAL_5, MEAL_6, MEAL_7);
        testMealsList.sort(Comparator.comparing(Meal::getDateTime).reversed());
        assertMatch(service.getAll(USER_ID), testMealsList);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(updated.getId(), USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Meal updated = getUpdated();
        updated.setId(550);
        service.update(updated, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, USER_ID);
        assertMatch(newMeal, created);
        assertMatch(newMeal, service.get(created.getId(), USER_ID));
    }
}