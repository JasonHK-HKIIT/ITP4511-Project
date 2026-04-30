<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="user" scope="request" type="dev.jasonhk.hkiit.itp4511.clinicman.bean.User" />
<jsp:useBean id="notifications" scope="request" type="java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.Notification>"/>

<!DOCTYPE html>
<html>
<head>
    <title>Home</title>
    <link rel="stylesheet" href="/css/pico.jade.min.css">
</head>
<body>
    <jsp:include page="/WEB-INF/includes/header.jsp" />

    <main class="container">
        <h1>Welcome, ${user.fullName}</h1>

        <section>
            <h2>Notifications</h2>
            <c:forEach items="${notifications}" var="notification">
            <article>
                <header>${notification.title}</header>
                ${notification.message}
                <footer>${notification.type}</footer>
            </article>
            </br>
            </c:forEach>
        </section>
    </main>
</body>
</html>