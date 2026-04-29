<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="/css/pico.jade.min.css">
</head>
<body>
    <header class="container">
        <nav>
            <ul>
                <li><strong>Community Care Health Consortium</strong></li>
            </ul>
        </nav>
    </header>

    <main class="container">
        <h1>Register</h1>

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

            <button type="submit">Register</button>
            <a href="/login" role="button" class="secondary" style="width: 100%">Login</a>
        </form>
    </main>
</body>
</html>
