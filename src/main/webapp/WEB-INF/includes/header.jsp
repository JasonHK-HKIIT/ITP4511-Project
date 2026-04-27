<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="container-fluid">
    <nav>
        <ul>
            <li><strong>Community Care Health Consortium</strong></li>
        </ul>
        <ul>
            <li><a href="/">Home</a></li>
            <c:choose>
                <c:when test="${param.type == 'patient'}">
                    <li><a href="/clinics">Clinics</a></li>
                    <li><a href="/appointments">Appointments</a></li>
                    <li><a href="/queues">Queues</a></li>
                </c:when>
                <c:when test="${param.type == 'staff'}">
                    <li><a href="/staff/appointments">Appointments</a></li>
                    <li><a href="/staff/queues">Queues</a></li>
                </c:when>
            </c:choose>
            <li><a href="/profile">Profile</a></li>
            <li><a href="/logout">Sign Out</a></li>
        </ul>
    </nav>
</header>