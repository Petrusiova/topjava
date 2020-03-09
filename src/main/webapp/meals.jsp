<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="meals" class="ru.javawebinar.topjava.web.MealServlet" scope="session"/>

<html>
<head>
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
        <th style="width:30%">Дата/Время</th>
        <th style="width:40%">Описание</th>
        <th style="width:30%">Калории</th>
    </tr>
    <c:forEach var="mealTo" items="${meals}">
            <tr align="center" class="${mealTo.excess ? 'exceeded' : 'normal'}">
                <td><c:out value="${mealTo.dateTime}"/></td>
                <td><c:out value="${mealTo.description}"/></td>
                <td><c:out value="${mealTo.calories}"/></td>
            </tr>
    </c:forEach>
</table>
</body>
</html>