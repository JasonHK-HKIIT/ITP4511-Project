<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="queueTickets" scope="request" type="java.util.Map<java.lang.Integer, java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.QueueTicket>>" />
<jsp:useBean id="patients" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.User>" />
<jsp:useBean id="services" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.Service>" />
<jsp:useBean id="clinicServices" scope="request" type="java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.ClinicService>" />

<!DOCTYPE html>
<html>
<head>
    <title>Queues</title>
    <link rel="stylesheet" href="/css/pico.jade.min.css">
</head>
<body>
    <jsp:include page="/WEB-INF/includes/header.jsp" />

    <main class="container">
        <h1>Queues</h1>

        <c:forEach items="${clinicServices}" var="clinicService">
            <section>
                <h2>${services.get(clinicService.serviceId).name}</h2>

                <table>
                    <thead>
                    <tr>
                        <th scope="col">Ticket #</th>
                        <th scope="col">Patient</th>
                        <th scope="col">Status</th>
                        <th scope="col"></th>
                    </tr>
                    </thead>
                    <tbody>
                        <c:set var="tickets" value="${queueTickets.get(clinicService.id)}" />
                        <c:if test="${empty tickets}">
                            <tr>
                                <td colspan="6" style="text-align: center">No records.</td>
                            </tr>
                        </c:if>
                        <c:forEach items="${tickets}" var="ticket">
                            <tr>
                                <td>${ticket.formatTicketNumber()}</td>
                                <td>${patients.get(ticket.patientId).fullName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${ticket.status == 'WAITING'}">Waiting</c:when>
                                        <c:when test="${ticket.status == 'CALLED'}">Called</c:when>
                                        <c:when test="${ticket.status == 'COMPLETED'}">Completed</c:when>
                                        <c:when test="${ticket.status == 'SKIPPED'}">Skipped</c:when>
                                        <c:when test="${ticket.status == 'LEFT'}">Left</c:when>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:if test="${ticket.status == 'WAITING'}">
                                        <a href data-action="call" data-id="${ticket.id}"
                                           data-service="${services.get(clinicService.serviceId).name}"
                                           data-number="${ticket.formatTicketNumber()}"
                                           data-patient="${patients.get(ticket.patientId).fullName}"
                                        >
                                            [Call]
                                        </a>
                                    </c:if>
                                    <c:if test="${ticket.status == 'CALLED'}">
                                        <a href data-action="complete" data-id="${ticket.id}"
                                           data-service="${services.get(clinicService.serviceId).name}"
                                           data-number="${ticket.formatTicketNumber()}"
                                           data-patient="${patients.get(ticket.patientId).fullName}"
                                        >
                                            [Complete]
                                        </a>
                                        <a href data-action="skip" data-id="${ticket.id}"
                                           data-service="${services.get(clinicService.serviceId).name}"
                                           data-number="${ticket.formatTicketNumber()}"
                                           data-patient="${patients.get(ticket.patientId).fullName}"
                                        >
                                            [Skip]
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </section>
        </c:forEach>
    </main>

    <dialog data-type="call">
        <article>
            <h2>Call Queue Ticket</h2>
            <p>
                Are you sure to call the queue ticket below?
            </p>
            <ul>
                <li>Service: <span data-key="service"></span></li>
                <li>Ticket No.: <span data-key="number"></span></li>
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
            <h2>Complete Queue Ticket</h2>
            <p>
                Are you sure to complete the queue ticket below?
            </p>
            <ul>
                <li>Service: <span data-key="service"></span></li>
                <li>Ticket No.: <span data-key="number"></span></li>
                <li>Patient: <span data-key="patient"></span></li>
            </ul>
            <footer>
                <button class="secondary">No</button>
                <button>Yes</button>
            </footer>
        </article>
    </dialog>

    <dialog data-type="skip">
        <article>
            <h2>Skip Queue Ticket</h2>
            <p>
                Are you sure to skip the queue ticket below?
            </p>
            <ul>
                <li>Service: <span data-key="service"></span></li>
                <li>Ticket No.: <span data-key="number"></span></li>
                <li>Patient: <span data-key="patient"></span></li>
            </ul>
            <footer>
                <button class="secondary">No</button>
                <button>Yes</button>
            </footer>
        </article>
    </dialog>

    <script type="module">
        import { initializeDialog } from "/js/dialog-helpers.js";

        initializeDialog(document.querySelector("dialog[data-type=call]"), "/staff/queues?action=call&id={id}", () => location.reload());
        initializeDialog(document.querySelector("dialog[data-type=complete]"), "/staff/queues?action=complete&id={id}", () => location.reload());
        initializeDialog(document.querySelector("dialog[data-type=skip]"), "/staff/queues?action=skip&id={id}", () => location.reload());
    </script>
</body>
</html>
