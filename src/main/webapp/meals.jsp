<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<h2><a href="index.html">Home</a></h2>
<ul>
    <c:forEach var="meal" items="${meals}">
        <li><c:out value="${meal}" /></li>
    </c:forEach>
</ul>
</body>
</html>