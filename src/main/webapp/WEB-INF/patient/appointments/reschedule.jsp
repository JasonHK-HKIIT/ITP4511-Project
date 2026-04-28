<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="en-HK" />

<jsp:useBean id="timeslot" scope="request" type="dev.jasonhk.hkiit.itp4511.clinicman.bean.Timeslot" />
<jsp:useBean id="clinicService" scope="request" type="dev.jasonhk.hkiit.itp4511.clinicman.bean.ClinicService" />
<jsp:useBean id="clinic" scope="request" type="dev.jasonhk.hkiit.itp4511.clinicman.bean.Clinic" />
<jsp:useBean id="service" scope="request" type="dev.jasonhk.hkiit.itp4511.clinicman.bean.Service" />
<jsp:useBean id="timeslots" scope="request" type="java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.Timeslot>" />

<html>
<head>
    <title>Reschedule Appointment</title>
    <link rel="stylesheet" href="/css/pico.jade.min.css">
</head>
<body>
    <jsp:include page="/WEB-INF/includes/header.jsp">
        <jsp:param name="type" value="patient" />
    </jsp:include>

    <main class="container">
        <h1>Reschedule Appointment</h1>

        <form method="post">
            <fieldset>
                <label>
                    Clinic
                    <select disabled>
                        <option selected>${clinic.location}</option>
                    </select>
                </label>
                <label>
                    Service
                    <select disabled>
                        <option selected>${service.name}</option>
                    </select>
                </label>
                <label>
                    Date
                    <input id="date" type="date" value="${timeslot.slotDate}" min="${timeslot.slotDate}" required />
                </label>
                <label>
                    Timeslot
                    <select id="timeslot" name="timeslot" required>
                        <jsp:include page="timeslots.jsp">
                            <jsp:param name="timeslotId" value="${timeslot.id}"/>
                        </jsp:include>
                    </select>
                </label>
            </fieldset>

            <button type="submit">Reschedule</button>
        </form>
    </main>

    <script>
        const dateField = document.getElementById("date");
        const timeslotField = document.getElementById("timeslot");

        dateField.addEventListener("change", async () =>
        {
            const response = await fetch(`/appointments?action=timeslots&service=${clinicService.id}&date=\${dateField.value}`);
            timeslotField.innerHTML = await response.text();
        });
    </script>
</body>
</html>
