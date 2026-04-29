<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="clinics" scope="request" type="java.util.List<dev.jasonhk.hkiit.itp4511.clinicman.bean.Clinic>" />

<!DOCTYPE html>
<html>
<head>
    <title>Create User</title>
    <link rel="stylesheet" href="/css/pico.jade.min.css">
</head>
<body>
    <jsp:include page="/WEB-INF/includes/header.jsp" />

    <main class="container">
        <h1>Create User</h1>

        <form method="post">
            <fieldset>
                <label>
                    Username
                    <input name="username" type="text" autocomplete="username" required />
                </label>
                <label>
                    Password
                    <input name="password" type="password" autocomplete="new-password" required />
                </label>
            </fieldset>

            <fieldset>
                <label>
                    Full Name
                    <input name="fullName" type="text" autocomplete="name" required />
                </label>
                <label>
                    Phone No.
                    <input name="phone" type="tel" autocomplete="tel" />
                </label>
                <label>
                    Gender
                    <select name="gender">
                        <option value=""></option>
                        <option value="MALE">Male</option>
                        <option value="FEMALE">Female</option>
                    </select>
                </label>
                <label>
                    Date of Birth
                    <input name="dateOfBirth" type="date" autocomplete="bday" />
                </label>
            </fieldset>

            <fieldset>
                <label>
                    Role
                    <select id="role" name="role" required>
                        <option value="PATIENT">Patient</option>
                        <option value="STAFF">Staff</option>
                        <option value="ADMIN">Admin</option>
                    </select>
                </label>
                <label>
                    Clinic
                    <select id="clinic" name="clinicId" required disabled>
                        <option value=""></option>
                        <c:forEach items="${clinics}" var="clinic">
                            <option value="${clinic.id}">${clinic.location}</option>
                        </c:forEach>
                    </select>
                </label>
            </fieldset>

            <button type="submit">Create</button>
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
