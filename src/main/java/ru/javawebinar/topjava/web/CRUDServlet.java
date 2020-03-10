package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.RepositoryImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class CRUDServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static String INSERT_OR_EDIT = "/meal.jsp";
    private static String LIST_USER = "/meals.jsp";

    private RepositoryImpl repository;

    public CRUDServlet() {
        super();
        repository = new RepositoryImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Meal meal = new Meal(
                LocalDateTime.now(),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        String mealId = request.getParameter("mealId");
        if(mealId == null || mealId.isEmpty()) {
            repository.addMeal(meal);
        } else {
            repository.updateMeal(meal);
        }
        RequestDispatcher view = request.getRequestDispatcher("/meals.jsp");
        request.setAttribute("meals", repository.getAll());
        view.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            repository.deleteMeal(mealId);
            request.setAttribute("meals", repository.getAll());
        } else if (action.equalsIgnoreCase("edit")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = repository.getMealById(mealId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("listMeals")) {
            request.setAttribute("meals", repository.getAll());
        }

        RequestDispatcher view = request.getRequestDispatcher("/meals.jsp");
        view.forward(request, response);
    }
}
