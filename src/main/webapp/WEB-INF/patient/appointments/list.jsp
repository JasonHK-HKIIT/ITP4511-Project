<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="appointments" scope="request" type="java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.Appointment>" />
<jsp:useBean id="clinics" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.Clinic>" />
<jsp:useBean id="services" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.Service>" />
<jsp:useBean id="clinicServices" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.ClinicService>" />
<jsp:useBean id="timeslots" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.Timeslot>" />

<html>
<head>
    <title>Appointments</title>
    <link rel="stylesheet" href="/css/pico.slate.min.css">
    <style>
        #appointments td:nth-child(3)
        {
            white-space: nowrap;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/includes/header.jsp">
        <jsp:param name="type" value="patient" />
    </jsp:include>

    <main class="container">
        <h1>Appointments</h1>

        <table id="appointments">
            <thead>
                <tr>
                    <th scope="col">Date</th>
                    <th scope="col">Time</th>
                    <th scope="col">Clinic</th>
                    <th scope="col">Service</th>
                    <th scope="col">Status</th>
                    <th scope="col"></th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${empty appointments}">
                    <tr>
                        <td colspan="6" style="text-align: center">No records.</td>
                    </tr>
                </c:if>
                <c:forEach items="${appointments}" var="appointment">
                    <tr>
                        <c:set var="timeslot" value="${timeslots.get(appointment.timeslotId)}" />
                        <c:set var="clinicService" value="${clinicServices.get(timeslot.clinicServiceId)}" />
                        <td>${timeslot.slotDate}</td>
                        <td>${timeslot.startTime}</td>
                        <td>${clinics.get(clinicService.clinicId).location}</td>
                        <td>${services.get(clinicService.serviceId).name}</td>
                        <td>
                            <c:choose>
                                <c:when test="${appointment.status.name() == 'PENDING'}">Pending</c:when>
                                <c:when test="${appointment.status.name() == 'CONFIRMED'}">Confirmed</c:when>
                                <c:when test="${appointment.status.name() == 'ARRIVED'}">Arrived</c:when>
                                <c:when test="${appointment.status.name() == 'COMPLETED'}">Completed</c:when>
                                <c:when test="${appointment.status.name() == 'NO_SHOW'}">No Show</c:when>
                                <c:when test="${appointment.status.name() == 'CANCELLED'}">Cancelled</c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:url value="/appointments" var="rescheduleAppointment">
                                <c:param name="action" value="reschedule" />
                                <c:param name="id" value="${appointment.id}" />
                            </c:url>
                            <a href="${rescheduleAppointment}">[Reschedule]</a>
                            <c:url value="/appointments" var="cancelAppointment">
                                <c:param name="action" value="cancel" />
                                <c:param name="id" value="${appointment.id}" />
                            </c:url>
                            <a href="${cancelAppointment}">[Cancel]</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </main>
</body>
</html>
