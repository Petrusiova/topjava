package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        List<MealTo> mealsList = Arrays.asList(
                new MealTo(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Breakfast", 500, false),
                new MealTo(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Lunch", 1000, false),
                new MealTo(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Dinner", 500, false),
                new MealTo(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Extra meal", 100, true),
                new MealTo(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Breakfast", 1000, true),
                new MealTo(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Lunch", 500, true),
                new MealTo(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Dinner", 410, true)
        );
        request.setAttribute("meals", mealsList);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
