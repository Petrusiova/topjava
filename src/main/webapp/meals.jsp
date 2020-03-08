<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="meals" class="ru.javawebinar.topjava.web.MealServlet" scope="session"/>

<html>
<head>
    <title>Meals</title>
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
        <c:if test="${mealTo.excess eq true}">
            <tr align="center" style="color: red">
                <td><c:out value="${mealTo.dateTime}"/></td>
                <td><c:out value="${mealTo.description}"/></td>
                <td><c:out value="${mealTo.calories}"/></td>
            </tr>
        </c:if>
        <c:if test="${mealTo.excess eq false}">
            <tr align="center" style="color: green">
                <td><c:out value="${mealTo.dateTime}"/></td>
                <td><c:out value="${mealTo.description}"/></td>
                <td><c:out value="${mealTo.calories}"/></td>
            </tr>
        </c:if>
    </c:forEach>
</table>
</body>
</html>