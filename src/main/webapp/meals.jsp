<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="meals" class="ru.javawebinar.topjava.web.MealServlet" scope="session"/>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Meals</title>
    <style>
        .normal {color: green;}
        .exceeded {color: red;}
    </style>
</head>
<body>
<h2><a href="index.html">Home</a></h2>

<table border="1">
    <tr>
        <th style="width:30%">mealId</th>
        <th style="width:30%">Дата/Время</th>
        <th style="width:40%">Описание</th>
        <th style="width:30%">Калории</th>
        <th style="width:30%">Операция 1</th>
        <th style="width:30%">Операция 2</th>
    </tr>
    <tbody>
    <c:forEach var="mealTo" items="${meals}">
            <tr align="center" class="${mealTo.excess ? 'exceeded' : 'normal'}">
                <td><c:out value="${mealTo.index}"/></td>
                <td><c:out value="${mealTo.dateTime}"/></td>
                <td><c:out value="${mealTo.description}"/></td>
                <td><c:out value="${mealTo.calories}"/></td>
                <td><a href="CRUDServlet?action=edit&index=<c:out value="${mealTo.index}"/>">Update</a></td>
                <td><a href="CRUDServlet?action=delete&index=<c:out value="${mealTo.index}"/>">Delete</a></td>
            </tr>
    </c:forEach>
    </tbody>
</table>
<p><a href="action=listMeals">Add User</a></p>
</body>
</html>