package dev.jasonhk.hkiit.itp4511.clinicman.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginController extends Controller
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (getCurrentUser(request) != null)
        {
            response.sendRedirect("/");
            return;
        }

        request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (getCurrentUser(request) != null)
        {
            response.sendRedirect("/");
            return;
        }

        var username = request.getParameter("username");
        var password = request.getParameter("password");

        var user = database.getUserByCredentials(username, password);
        if (user == null)
        {
            request.setAttribute("isCredentialsError", true);
            request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
            return;
        }

        setCurrentUser(request, user);
        response.sendRedirect("/");
    }
}
