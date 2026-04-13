package dev.jasonhk.hkiit.itp4511.clinicman.controller;

import java.io.IOException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dev.jasonhk.hkiit.itp4511.clinicman.Database;
import dev.jasonhk.hkiit.itp4511.clinicman.mixin.WithDatabase;
import dev.jasonhk.hkiit.itp4511.clinicman.mixin.WithUser;

@WebServlet("/login")
public class LoginController extends HttpServlet implements WithDatabase, WithUser
{
    private Database database;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        database = getDatabase(config);
    }

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
