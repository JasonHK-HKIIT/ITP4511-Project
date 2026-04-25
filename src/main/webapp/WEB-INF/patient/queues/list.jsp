<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="queueTickets" scope="request" type="java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.QueueTicket>" />
<jsp:useBean id="clinics" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.Clinic>" />
<jsp:useBean id="services" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.Service>" />
<jsp:useBean id="clinicServices" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.ClinicService>" />

<html>
<head>
    <title>Queues</title>
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
                                <c:when test="${appointment.status.name() == 'WAITING'}">Waiting</c:when>
                                <c:when test="${appointment.status.name() == 'CALLED'}">Called</c:when>
                                <c:when test="${appointment.status.name() == 'COMPLETED'}">Completed</c:when>
                                <c:when test="${appointment.status.name() == 'SKIPPED'}">Skipped</c:when>
                                <c:when test="${appointment.status.name() == 'CANCELLED'}">Cancelled</c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${appointment.status.ordinal() < 1}">
                                <a href data-action="cancel" data-id="${ticket.id}"
                                   data-date="${ticket.queueDate}"
                                   data-location="${clinics.get(clinicService.clinicId).location}"
                                   data-service="${services.get(clinicService.serviceId).name}"
                                   data-number="${ticket.formatTicketNumber()}"
                                >
                                    [Cancel]
                                </a>
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

    <script>
        /** @type {HTMLDialogElement} */
        const cancelDialog = document.querySelector("dialog[data-type=cancel]");
        cancelDialog.querySelector("button.secondary").addEventListener("click", () => cancelDialog.close("No"));
        cancelDialog.querySelector("button:not(.secondary)").addEventListener("click", () => cancelDialog.close("Yes"));

        document.querySelectorAll("[data-action=cancel]").forEach((target) =>
        {
            target.addEventListener("click", (event) =>
            {
                event.preventDefault();

                for (const key of Object.keys(target.dataset))
                {
                    const placeholder = cancelDialog.querySelector(`[data-key="\${key}"]`);
                    if (placeholder) { placeholder.textContent = target.dataset[key]; }
                }

                cancelDialog.addEventListener("close", async () =>
                {
                    if (cancelDialog.returnValue === "Yes")
                    {
                        const response = await fetch(`/appointments?action=cancel&id=\${target.dataset.id}`, { method: "POST" });
                        if (response.ok) { location.reload(); }
                    }
                }, { once: true });
                cancelDialog.showModal();
            });
        });
    </script>
</body>
</html>
