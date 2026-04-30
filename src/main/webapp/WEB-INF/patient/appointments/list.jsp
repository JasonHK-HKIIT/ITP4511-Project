<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="appointments" scope="request" type="java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.Appointment>" />
<jsp:useBean id="clinics" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.Clinic>" />
<jsp:useBean id="services" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.Service>" />
<jsp:useBean id="clinicServices" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.ClinicService>" />
<jsp:useBean id="timeslots" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.Timeslot>" />
<jsp:useBean id="today" scope="request" type="java.time.LocalDate" />

<!DOCTYPE html>
<html>
<head>
    <title>Appointments</title>
    <link rel="stylesheet" href="/css/pico.jade.min.css">
    <style>
        #appointments td:nth-child(3)
        {
            white-space: nowrap;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/includes/header.jsp" />

    <main class="container">
        <h1>Appointments</h1>

        <table>
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
                                <c:when test="${appointment.status == 'PENDING'}">Pending</c:when>
                                <c:when test="${appointment.status == 'CONFIRMED'}">Confirmed</c:when>
                                <c:when test="${appointment.status == 'ARRIVED'}">Arrived</c:when>
                                <c:when test="${appointment.status == 'COMPLETED'}">Completed</c:when>
                                <c:when test="${appointment.status == 'NO_SHOW'}">No Show</c:when>
                                <c:when test="${appointment.status == 'CANCELLED'}">
                                    <span data-tooltip="${appointment.cancelReason}">Cancelled</span>
                                </c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${timeslot.slotDate.isAfter(today)}">
                                <c:if test="${(appointment.status == 'PENDING') || (appointment.status == 'CONFIRMED')}">
                                    <c:url value="/appointments" var="rescheduleAppointment">
                                        <c:param name="action" value="reschedule" />
                                        <c:param name="id" value="${appointment.id}" />
                                    </c:url>
                                    <a href="${rescheduleAppointment}">[Reschedule]</a>
                                    <a href data-action="cancel" data-id="${appointment.id}"
                                       data-date="${timeslot.slotDate}"
                                       data-time="${timeslot.startTime}"
                                       data-location="${clinics.get(clinicService.clinicId).location}"
                                       data-service="${services.get(clinicService.serviceId).name}"
                                    >
                                        [Cancel]
                                    </a>
                                </c:if>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </main>

    <dialog data-type="cancel">
        <article>
            <h2>Cancel Appointment</h2>
            <p>
                Are you sure to cancel your appointment below?
            </p>
            <ul>
                <li>Date: <span data-key="date"></span></li>
                <li>Time: <span data-key="time"></span></li>
                <li>Clinic: <span data-key="location"></span></li>
                <li>Service: <span data-key="service"></span></li>
            </ul>
            <footer>
                <button class="secondary">No</button>
                <button>Yes</button>
            </footer>
        </article>
    </dialog>

    <script type="module">
        import { initializeDialog } from "/js/dialog-helpers.js";

        initializeDialog(document.querySelector("dialog[data-type=cancel]"), "/appointments?action=cancel&id={id}", () => location.reload());
    </script>
</body>
</html>
