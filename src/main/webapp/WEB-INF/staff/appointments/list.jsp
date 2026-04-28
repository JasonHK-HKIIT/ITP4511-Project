<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="appointments" scope="request" type="java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.Appointment>" />
<jsp:useBean id="patients" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.User>" />
<jsp:useBean id="clinics" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.Clinic>" />
<jsp:useBean id="services" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.Service>" />
<jsp:useBean id="clinicServices" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.ClinicService>" />
<jsp:useBean id="timeslots" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.Timeslot>" />
<jsp:useBean id="today" scope="request" type="java.time.LocalDate" />

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
    <jsp:include page="/WEB-INF/includes/header.jsp">
        <jsp:param name="type" value="staff" />
    </jsp:include>

    <main class="container">
        <h1>Appointments</h1>

        <table>
            <thead>
            <tr>
                <th scope="col">Date</th>
                <th scope="col">Time</th>
                <th scope="col">Service</th>
                <th scope="col">Patient</th>
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
                        <td>${services.get(clinicService.serviceId).name}</td>
                        <td>${patients.get(appointment.patientId).fullName}</td>
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
                            <c:choose>
                                <c:when test="${timeslot.slotDate.isAfter(today)}">
                                    <c:if test="${appointment.status == 'PENDING'}">
                                        <a href data-action="confirm" data-id="${appointment.id}"
                                           data-date="${timeslot.slotDate}"
                                           data-time="${timeslot.startTime}"
                                           data-location="${clinics.get(clinicService.clinicId).location}"
                                           data-service="${services.get(clinicService.serviceId).name}"
                                           data-patient="${patients.get(appointment.patientId).fullName}"
                                        >
                                            [Confirm]
                                        </a>
                                    </c:if>
                                    <c:if test="${(appointment.status == 'PENDING') || (appointment.status == 'CONFIRMED')}">
                                        <a href data-action="cancel" data-id="${appointment.id}"
                                           data-date="${timeslot.slotDate}"
                                           data-time="${timeslot.startTime}"
                                           data-location="${clinics.get(clinicService.clinicId).location}"
                                           data-service="${services.get(clinicService.serviceId).name}"
                                           data-patient="${patients.get(appointment.patientId).fullName}"
                                        >
                                            [Cancel]
                                        </a>
                                    </c:if>
                                </c:when>
                                <c:when test="${timeslot.slotDate.isEqual(today)}">
                                    <c:if test="${appointment.status == 'CONFIRMED'}">
                                        <a href data-action="check-in" data-id="${appointment.id}"
                                           data-date="${timeslot.slotDate}"
                                           data-time="${timeslot.startTime}"
                                           data-location="${clinics.get(clinicService.clinicId).location}"
                                           data-service="${services.get(clinicService.serviceId).name}"
                                           data-patient="${patients.get(appointment.patientId).fullName}"
                                        >
                                            [Check-in]
                                        </a>
                                        <a href data-action="no-show" data-id="${appointment.id}"
                                           data-date="${timeslot.slotDate}"
                                           data-time="${timeslot.startTime}"
                                           data-location="${clinics.get(clinicService.clinicId).location}"
                                           data-service="${services.get(clinicService.serviceId).name}"
                                           data-patient="${patients.get(appointment.patientId).fullName}"
                                        >
                                            [No Show]
                                        </a>
                                    </c:if>
                                    <c:if test="${appointment.status == 'ARRIVED'}">
                                        <a href data-action="complete" data-id="${appointment.id}"
                                           data-date="${timeslot.slotDate}"
                                           data-time="${timeslot.startTime}"
                                           data-location="${clinics.get(clinicService.clinicId).location}"
                                           data-service="${services.get(clinicService.serviceId).name}"
                                           data-patient="${patients.get(appointment.patientId).fullName}"
                                        >
                                            [Complete]
                                        </a>
                                    </c:if>
                                </c:when>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </main>

    <dialog data-type="confirm">
        <article>
            <h2>Confirm Appointment</h2>
            <p>
                Are you sure to confirm the appointment below?
            </p>
            <ul>
                <li>Date: <span data-key="date"></span></li>
                <li>Time: <span data-key="time"></span></li>
                <li>Clinic: <span data-key="location"></span></li>
                <li>Service: <span data-key="service"></span></li>
                <li>Patient: <span data-key="patient"></span></li>
            </ul>
            <footer>
                <button class="secondary">No</button>
                <button>Yes</button>
            </footer>
        </article>
    </dialog>

    <dialog data-type="check-in">
        <article>
            <h2>Check-in Appointment</h2>
            <p>
                Are you sure to check in the appointment below?
            </p>
            <ul>
                <li>Date: <span data-key="date"></span></li>
                <li>Time: <span data-key="time"></span></li>
                <li>Clinic: <span data-key="location"></span></li>
                <li>Service: <span data-key="service"></span></li>
                <li>Patient: <span data-key="patient"></span></li>
            </ul>
            <footer>
                <button class="secondary">No</button>
                <button>Yes</button>
            </footer>
        </article>
    </dialog>

    <dialog data-type="complete">
        <article>
            <h2>Complete Appointment</h2>
            <p>
                Are you sure to complete the appointment below?
            </p>
            <ul>
                <li>Date: <span data-key="date"></span></li>
                <li>Time: <span data-key="time"></span></li>
                <li>Clinic: <span data-key="location"></span></li>
                <li>Service: <span data-key="service"></span></li>
                <li>Patient: <span data-key="patient"></span></li>
            </ul>
            <footer>
                <button class="secondary">No</button>
                <button>Yes</button>
            </footer>
        </article>
    </dialog>

    <dialog data-type="no-show">
        <article>
            <h2>No Show Appointment</h2>
            <p>
                Are you sure to mark the appointment below as &ldquo;no show&rdquo;?
            </p>
            <ul>
                <li>Date: <span data-key="date"></span></li>
                <li>Time: <span data-key="time"></span></li>
                <li>Clinic: <span data-key="location"></span></li>
                <li>Service: <span data-key="service"></span></li>
                <li>Patient: <span data-key="patient"></span></li>
            </ul>
            <footer>
                <button class="secondary">No</button>
                <button>Yes</button>
            </footer>
        </article>
    </dialog>

    <dialog data-type="cancel">
        <article>
            <h2>Cancel Appointment</h2>
            <p>
                Are you sure to cancel the appointment below?
            </p>
            <ul>
                <li>Date: <span data-key="date"></span></li>
                <li>Time: <span data-key="time"></span></li>
                <li>Clinic: <span data-key="location"></span></li>
                <li>Service: <span data-key="service"></span></li>
                <li>Patient: <span data-key="patient"></span></li>
            </ul>
            <label>
                Reason
                <input name="cancelReason" type="text" />
            </label>
            <footer>
                <button class="secondary">No</button>
                <button>Yes</button>
            </footer>
        </article>
    </dialog>

    <script type="module">
        import { initializeDialog } from "/js/dialog-helpers.js";

        initializeDialog(document.querySelector("dialog[data-type=confirm]"), "/staff/appointments?action=confirm&id={id}", () => location.reload());
        initializeDialog(document.querySelector("dialog[data-type=check-in]"), "/staff/appointments?action=check-in&id={id}", () => location.reload());
        initializeDialog(document.querySelector("dialog[data-type=complete]"), "/staff/appointments?action=complete&id={id}", () => location.reload());
        initializeDialog(document.querySelector("dialog[data-type=no-show]"), "/staff/appointments?action=no-show&id={id}", () => location.reload());
        initializeDialog(document.querySelector("dialog[data-type=cancel]"), "/staff/appointments?action=cancel&id={id}", () => location.reload());
    </script>
</body>
</html>
