<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Home</title>
  <link rel="stylesheet" href="/css/pico.slate.min.css">
</head>
<body>
<header class="container-fluid">
  <nav>
    <ul>
      <li><strong>Community Care Health Consortium(Staff Page for testing rn)</strong></li>
    </ul>
    <ul>
      <li><a href="/clinics">Clinics</a></li>
      <li><a href="/appointments">Appointments</a></li>
      <li><a href="/profile">Profile</a></li>
      <li><a href="/logout">Sign Out</a></li>
    </ul>
  </nav>
</header>
<main class="container">
  <section>
    <h2>Notifications</h2>
    <article>
      <header>HEADREEE</header>
      if im staff we good
      ${sessionScope.user.fullName}
      <footer>FootREEEE</footer>
    </article>
  </section>
</main>
</body>
</html>