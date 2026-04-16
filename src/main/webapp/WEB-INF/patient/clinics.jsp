<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="en-HK" />

<html lang="en">
<head>
    <title>Clinics</title>
    <link rel="stylesheet" href="/css/pico.slate.min.css">
    <style>
        details > :not(summary)
        {
            font-size: smaller;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/includes/header.jsp">
        <jsp:param name="type" value="patient" />
    </jsp:include>

    <main class="container">
        <h1>Clinics</h1>

        <jsp:useBean id="clinics" scope="request" type="java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.Clinic>" />
        <jsp:useBean id="clinicServices" scope="request" type="java.util.Map<java.lang.Integer, java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.ClinicService>>" />
        <jsp:useBean id="services" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.Service>" />

        <c:forEach items="${clinics}" var="clinic">
            <details>
                <summary><strong>${clinic.location}</strong></summary>
                <p>
                    <strong>Opening hours:</strong> ${clinic.openingTime} &ndash; ${clinic.closingTime}
                </p>
                <p>
                    <strong>Services:</strong>
                    <ul>
                        <c:forEach items="${clinicServices.get(clinic.id)}" var="service">
                            <li>
                                ${services.get(service.serviceId).name}
                                <c:url value="/appointments" var="bookAppointment">
                                    <c:param name="action" value="book" />
                                    <c:param name="service" value="${service.id}" />
                                </c:url>
                                <a href="${bookAppointment}">[Book Appointment]</a>
                                <c:if test="${clinic.walkinEnabled}">
                                    <a href="/queue?action=join&service=${service.id}">[Join Queue]</a>
                                </c:if>
                            </li>
                        </c:forEach>
                    </ul>
                </p>
            </details>
            <hr />
        </c:forEach>
    </main>
</body>
</html>
