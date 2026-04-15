<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="en-HK" />

<jsp:useBean id="timeslots" scope="request" type="java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.Timeslot>" />

<option selected disabled></option>
<c:forEach items="${timeslots}" var="timeslot">
    <c:set var="vacancy" value="${timeslot.capacity - timeslot.bookedCount}" />

    <fmt:formatDate value="${timeslot.startTime}" type="time" timeStyle="short" var="startTime" />
    <fmt:formatDate value="${timeslot.endTime}" type="time" timeStyle="short" var="endTime" />
    <option value="${timeslot.id}" ${(vacancy <= 0) ? 'disabled' : ''}>${startTime} &ndash; ${endTime} (Vacancy: ${vacancy})</option>
</c:forEach>