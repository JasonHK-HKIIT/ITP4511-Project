<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="en-HK" />

<jsp:useBean id="timeslots" scope="request" type="java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.Timeslot>" />

<option selected disabled></option>
<c:forEach items="${timeslots}" var="timeslot">
    <c:set var="vacancy" value="${timeslot.capacity - timeslot.bookedCount}" />
    <option value="${timeslot.id}"${((vacancy <= 0) && (timeslot.id != param.timeslotId)) ? ' disabled' : ''}${(timeslot.id == param.timeslotId) ? ' selected' : ''}>
            ${timeslot.startTime} &ndash; ${timeslot.endTime} (Vacancy: ${vacancy})
    </option>
</c:forEach>