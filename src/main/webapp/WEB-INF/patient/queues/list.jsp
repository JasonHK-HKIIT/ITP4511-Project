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
                                <c:when test="${ticket.status.name() == 'WAITING'}">Waiting</c:when>
                                <c:when test="${ticket.status.name() == 'CALLED'}">Called</c:when>
                                <c:when test="${ticket.status.name() == 'COMPLETED'}">Completed</c:when>
                                <c:when test="${ticket.status.name() == 'SKIPPED'}">Skipped</c:when>
                                <c:when test="${ticket.status.name() == 'LEFT'}">Left</c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${ticket.status.ordinal() < 1}">
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

    <script>
        /** @type {HTMLDialogElement} */
        const leaveDialog = document.querySelector("dialog[data-type=leave]");
        leaveDialog.querySelector("button.secondary").addEventListener("click", () => leaveDialog.close("No"));
        leaveDialog.querySelector("button:not(.secondary)").addEventListener("click", () => leaveDialog.close("Yes"));

        document.querySelectorAll("[data-action=leave]").forEach((target) =>
        {
            target.addEventListener("click", (event) =>
            {
                event.preventDefault();

                for (const key of Object.keys(target.dataset))
                {
                    const placeholder = leaveDialog.querySelector(`[data-key="\${key}"]`);
                    if (placeholder) { placeholder.textContent = target.dataset[key]; }
                }

                leaveDialog.addEventListener("close", async () =>
                {
                    if (leaveDialog.returnValue === "Yes")
                    {
                        const response = await fetch(`/queues?action=leave&id=\${target.dataset.id}`, { method: "POST" });
                        if (response.ok) { location.reload(); }
                    }
                }, { once: true });
                leaveDialog.showModal();
            });
        });
    </script>
</body>
</html>
