package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Controller
public class MealRestController extends AbstractMealController {

    @Override
    public Collection<MealTo> getAll() {
        return super.getAll();

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

    @Override
    public Meal get(int id) {
        return super.get(id);
    }

    @Override
    public Meal create(Meal meal) {
        return super.create(meal);
    }

    @Override
    public void delete(int id) {
        super.delete(id);
    }

    @Override
    public void update(Meal meal, int id) {
        super.update(meal, id);
    }
}
