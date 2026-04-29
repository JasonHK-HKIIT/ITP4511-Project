<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="title" scope="request" type="java.lang.String" />
<jsp:useBean id="error" scope="request" type="java.lang.String" />
<jsp:useBean id="message" scope="request" type="java.lang.String" />

<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
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
        <h1>${error}</h1>

        <p>${message}</p>
        <a href="/" role="button">Return to Home</a>
    </main>
</body>
</html>
