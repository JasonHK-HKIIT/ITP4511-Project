<%--
  Created by IntelliJ IDEA.
  User: jasonhk
  Date: 14/4/2026
  Time: 上午12:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pico.slate.min.css">
</head>
<body>
    <main class="container">
        <% if (request.getAttribute("isCredentialsError") != null) { %>
            <h1>Error</h1>
            <p>Incorrect username or password.</p>
            <a href="${pageContext.request.contextPath}/login" role="button">Try Again</a>
        <% } else { %>
            <h1>Login</h1>
            <form action="${pageContext.request.contextPath}/login" method="post">
                <fieldset>
                    <label>
                        Username
                        <input name="username" type="text" autocomplete="username" required />
                    </label>
                    <label>
                        Password
                        <input name="password" type="password" autocomplete="current-password" required />
                    </label>
                </fieldset>

                <button type="submit">Login</button>
            </form>
        <% } %>
    </main>
</body>
</html>
