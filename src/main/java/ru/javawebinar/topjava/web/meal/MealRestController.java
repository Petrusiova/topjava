package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Collection<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()),
                SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public Meal update(Meal meal, int userId) {
        log.info("update {} with id={}", meal, meal.getId());
        return service.update(meal, SecurityUtil.authUserId());
    }

    public Collection<MealTo> filterByDateAndTime(LocalDate startDate, LocalDate endDate,
                                                  LocalTime startTime, LocalTime endTime) {
        Collection<MealTo> all = getAll();
        if (startDate != null) {
            all = all.stream()
                    .filter(item -> DateTimeUtil.isFromDateInclusive(item.getDate(), startDate))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        if (endDate != null) {
            all = all.stream()
                    .filter(item -> DateTimeUtil.isTillDateInclusive(item.getDate(), endDate))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        if (startTime != null) {
            all = all.stream()
                    .filter(item -> DateTimeUtil.isFromTimeInclusive(item.getTime(), startTime))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        if (endTime != null) {
            all = all.stream()
                    .filter(item -> DateTimeUtil.isTillTimeInclusive(item.getTime(), endTime))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        return all;
    }
}