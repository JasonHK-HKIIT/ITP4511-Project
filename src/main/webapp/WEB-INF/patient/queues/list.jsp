<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="queueTickets" scope="request" type="java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.QueueTicket>" />
<jsp:useBean id="clinics" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.Clinic>" />
<jsp:useBean id="services" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.Service>" />
<jsp:useBean id="clinicServices" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.ClinicService>" />

<!DOCTYPE html>
<html>
<head>
    <title>Queues</title>
    <link rel="stylesheet" href="/css/pico.jade.min.css">
    <style>
        #appointments td:nth-child(2)
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
        <h1>Queues</h1>

        <table>
            <thead>
                <tr>
                    <th scope="col">Date</th>
                    <th scope="col">Clinic</th>
                    <th scope="col">Service</th>
                    <th scope="col">Ticket #</th>
                    <th scope="col">Status</th>
                    <th scope="col"></th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${empty queueTickets}">
                    <tr>
                        <td colspan="6" style="text-align: center">No records.</td>
                    </tr>
                </c:if>
                <c:forEach items="${queueTickets}" var="ticket">
                    <tr>
                        <c:set var="clinicService" value="${clinicServices.get(ticket.clinicServiceId)}" />
                        <td>${ticket.queueDate}</td>
                        <td>${clinics.get(clinicService.clinicId).location}</td>
                        <td>${services.get(clinicService.serviceId).name}</td>
                        <td>${ticket.formatTicketNumber()}</td>
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
                                <a href data-action="leave" data-id="${ticket.id}"
                                   data-location="${clinics.get(clinicService.clinicId).location}"
                                   data-service="${services.get(clinicService.serviceId).name}"
                                   data-number="${ticket.formatTicketNumber()}"
                                >
                                    [Leave]
                                </a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </main>

    <dialog data-type="leave">
        <article>
            <h2>Leave Queue</h2>
            <p>
                Are you sure to leave the queue below?
            </p>
            <ul>
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

        initializeDialog(document.querySelector("dialog[data-type=leave]"), "/queues?action=leave&id={id}", () => location.reload());
    </script>
</body>
</html>
