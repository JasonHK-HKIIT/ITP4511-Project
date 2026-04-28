<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Home</title>
    <link rel="stylesheet" href="/css/pico.jade.min.css">
</head>
<body>
    <jsp:include page="/WEB-INF/includes/header.jsp">
        <jsp:param name="type" value="patient" />
    </jsp:include>

    <main class="container">
        <section>
            <h2>Notifications</h2>
            <article>
                <header>Header</header>
                Body
                <footer>Footer</footer>
            </article>
        </section>
    </main>
</body>
</html>