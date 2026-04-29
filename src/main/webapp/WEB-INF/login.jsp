<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
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
        <h1>Login</h1>

        <form action="/login" method="post">
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
            <a href="/register" role="button" class="secondary" style="width: 100%">Register</a>
        </form>
    </main>
</body>
</html>
