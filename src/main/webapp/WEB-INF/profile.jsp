<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="user" scope="request" type="dev.jasonhk.hkiit.itp4511.clinicman.bean.User" />

<html>
<head>
    <title>Profile</title>
    <link rel="stylesheet" href="/css/pico.jade.min.css">
</head>
<body>
    <jsp:include page="/WEB-INF/includes/header.jsp">
        <jsp:param name="type" value="${user.role.name().toLowerCase()}" />
    </jsp:include>

    <main class="container">
        <h1>Profile</h1>

        <form action="/profile" method="post">
            <fieldset>
                <label>
                    Username
                    <input name="username" type="text" value="${user.username}" autocomplete="username" required />
                </label>
                <label>
                    Full Name
                    <input name="fullName" type="text" value="${user.fullName}" autocomplete="name" required />
                </label>
                <label>
                    Phone No.
                    <input name="phone" type="tel" value="${user.phone}" autocomplete="tel" />
                </label>
                <label>
                    Gender
                    <select name="gender">
                        <option value="">Unspecified</option>
                        <option value="MALE"${(user.gender.name() == 'MALE') ? ' selected' : ''}>Male</option>
                        <option value="FEMALE"${(user.gender.name() == 'FEMALE') ? ' selected' : ''}>Female</option>
                    </select>
                </label>
                <label>
                    Date of Birth
                    <input name="dateOfBirth" type="date" value="${user.dateOfBirth}" autocomplete="bday" />
                </label>
            </fieldset>

            <button type="submit">Save</button>
        </form>
    </main>
</body>
</html>
