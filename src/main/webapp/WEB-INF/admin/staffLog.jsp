<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<jsp:useBean id="staffLog" scope="request" type="java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.StaffLog>" />


<!DOCTYPE html>
<html>
<head>
    <title>Log</title>
    <link rel="stylesheet" href="/css/pico.jade.min.css">
    <style>
        #appointments td:nth-child(2)
        {
            white-space: nowrap;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<main class="container">
    <h1>Logs</h1>

    <table>
        <thead>
        <tr>
            <th scope="col">Table</th>
            <th scope="col">Old Data</th>
            <th scope="col">New Data</th>
            <th scope="col">UserName</th>
            <th scope="col">Change time</th>
            <th scope="col"></th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty staffLog}">
            <tr>
                <td colspan="6" style="text-align: center">No records.</td>
            </tr>
        </c:if>
        <c:forEach items="${staffLog}" var="log">
            <tr>

                <td>${log.table}</td>
                <td>${log.old_row_data}</td>
                <td>${log.new_row_data}</td>
                <td>${log.username}</td>
                <td>
                    ${log.dml_timestamp}
                </td>
                <td>

                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</main>


</body>
</html>
