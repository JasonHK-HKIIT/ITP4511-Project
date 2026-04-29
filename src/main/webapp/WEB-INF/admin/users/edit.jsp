<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="target" scope="request" type="dev.jasonhk.hkiit.itp4511.clinicman.bean.User" />
<jsp:useBean id="clinics" scope="request" type="java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.Clinic>" />

<!DOCTYPE html>
<html>
<head>
    <title>Edit User</title>
    <link rel="stylesheet" href="/css/pico.jade.min.css">
</head>
<body>
    <jsp:include page="/WEB-INF/includes/header.jsp" />

    <main class="container">
        <h1>Edit User</h1>

        <form method="post">
            <fieldset>
                <label>
                    Username
                    <input name="username" type="text" value="${target.username}" autocomplete="username" required />
                </label>
                <label>
                    New Password
                    <input name="password" type="password" autocomplete="new-password" />
                </label>
            </fieldset>

            <fieldset>
                <label>
                    Full Name
                    <input name="fullName" type="text" value="${target.fullName}" autocomplete="name" required />
                </label>
                <label>
                    Phone No.
                    <input name="phone" type="tel" value="${target.phone}" autocomplete="tel" />
                </label>
                <label>
                    Gender
                    <select name="gender">
                        <option value=""></option>
                        <option value="MALE"${(target.gender == 'MALE') ? ' selected' : ''}>Male</option>
                        <option value="FEMALE"${(target.gender == 'FEMALE') ? ' selected' : ''}>Female</option>
                    </select>
                </label>
                <label>
                    Date of Birth
                    <input name="dateOfBirth" type="date" value="${target.dateOfBirth}" autocomplete="bday" />
                </label>
            </fieldset>

            <fieldset>
                <label>
                    Role
                    <select id="role" name="role" required>
                        <option value="PATIENT"${(target.role == 'PATIENT') ? ' selected' : ''}>Patient</option>
                        <option value="STAFF"${(target.role == 'STAFF') ? ' selected' : ''}>Staff</option>
                        <option value="ADMIN"${(target.role == 'ADMIN') ? ' selected' : ''}>Admin</option>
                    </select>
                </label>
                <label>
                    Clinic
                    <select id="clinic" name="clinicId" required${(target.role != 'STAFF') ? ' disabled' : ''}>
                        <option value=""></option>
                        <c:forEach items="${clinics}" var="clinic">
                            <option value="${clinic.id}"${(target.clinicId == clinic.id) ? ' selected' : ''}>${clinic.location}</option>
                        </c:forEach>
                    </select>
                </label>
            </fieldset>

            <button type="submit">Save</button>
        </form>
    </main>

    <script>
        /** @type {HTMLSelectElement} */
        const clinicField = document.getElementById("clinic");

        /** @type {HTMLSelectElement} */
        const roleField = document.getElementById("role");
        roleField.addEventListener("change", () =>
        {
            clinicField.disabled = (roleField.value !== "STAFF");
        });
    </script>
</body>
</html>
