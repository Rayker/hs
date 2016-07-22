<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<body>
	<c:forEach items="${cities}" var="city">
        <a href='/cities/${city.id}/specialities.html'>${city.displayedName}</a>
        <br />
    </c:forEach>
</body>

</html>
