<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="users" scope="request" type="java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.User>" />
<jsp:useBean id="clinics" scope="request" type="java.util.Map<java.lang.Integer, dev.jasonhk.hkiit.itp4511.clinicman.bean.Clinic>" />

<!DOCTYPE html>
<html>
<head>
    <title>Users</title>
    <link rel="stylesheet" href="/css/pico.jade.min.css">
    <style>
        #appointments td:nth-child(4)
        {
            white-space: nowrap;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/includes/header.jsp" />

    <main class="container">
        <hgroup>
            <h1>Users</h1>
            <p>
                <a href="/admin/users?action=create">[Create]</a>
            </p>
        </hgroup>

        <table>
            <thead>
            <tr>
                <th scope="col">Username</th>
                <th scope="col">Full Name</th>
                <th scope="col">Role</th>
                <th scope="col">Clinic</th>
                <th scope="col"></th>
            </tr>
            </thead>
            <tbody>
                <c:if test="${empty users}">
                    <tr>
                        <td colspan="6" style="text-align: center">No records.</td>
                    </tr>
                </c:if>
                <c:forEach items="${users}" var="user">
                    <tr>
                        <c:set var="timeslot" value="${timeslots.get(appointment.timeslotId)}" />
                        <c:set var="clinicService" value="${clinicServices.get(timeslot.clinicServiceId)}" />
                        <td>${user.username}</td>
                        <td>${user.fullName}</td>
                        <td>
                            <c:choose>
                                <c:when test="${user.role == 'PATIENT'}">Patient</c:when>
                                <c:when test="${user.role == 'STAFF'}">Staff</c:when>
                                <c:when test="${user.role == 'ADMIN'}">Admin</c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${user.role == 'STAFF'}">${clinics.get(user.clinicId).location}</c:if>
                        </td>
                        <td>
                            <c:url value="/admin/users" var="editUser">
                                <c:param name="action" value="edit" />
                                <c:param name="id" value="${user.id}" />
                            </c:url>
                            <a href="${editUser}">[Edit]</a>
                            <a href data-action="delete" data-id="${user.id}"
                               data-username="${user.username}"
                               data-full-name="${user.fullName}"
                            >
                                [Delete]
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </main>

    <dialog data-type="delete">
        <article>
            <h2>Delete User</h2>
            <p>
                Are you sure to delete the user below?
            </p>
            <ul>
                <li>Username: <span data-key="username"></span></li>
                <li>Full Name: <span data-key="fullName"></span></li>
            </ul>
            <footer>
                <button class="secondary">No</button>
                <button>Yes</button>
            </footer>
        </article>
    </dialog>

    <script type="module">
        import { initializeDialog } from "/js/dialog-helpers.js";

        initializeDialog(document.querySelector("dialog[data-type=delete]"), "/admin/users?action=delete&id={id}", () => location.reload());
    </script>
</body>
</html>
