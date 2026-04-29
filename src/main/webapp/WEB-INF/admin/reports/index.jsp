<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="clinics" scope="request" type="java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.Clinic>" />

<!DOCTYPE html>
<html>
<head>
    <title>Reports</title>
    <link rel="stylesheet" href="/css/pico.jade.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<main class="container">
    <h1>Reports</h1>

    <section>
        <h2>Appointments</h2>

        <form method="get">
            <input name="type" type="hidden" value="appointments" />

            <fieldset>
                <label>
                    Clinic
                    <select name="clinicId">
                        <option value=""></option>
                        <c:forEach items="${clinics}" var="clinic">
                            <option value="${clinic.id}" selected>${clinic.location}</option>
                        </c:forEach>
                    </select>
                </label>
            </fieldset>

            <button type="submit">Search</button>
        </form>
    </section>
</main>
</body>
</html>