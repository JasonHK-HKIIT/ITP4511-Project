package dev.jasonhk.hkiit.itp4511.clinicman.controller;

import java.io.IOException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dev.jasonhk.hkiit.itp4511.clinicman.Database;
import dev.jasonhk.hkiit.itp4511.clinicman.mixin.WithDatabase;
import dev.jasonhk.hkiit.itp4511.clinicman.mixin.WithUser;

public class Controller extends HttpServlet implements WithDatabase, WithUser
{
    protected Database database;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        database = getDatabase(config);
    }

    protected void showErrorPage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException
    {
        showErrorPage(request, response, "Error", message);
    }

    protected void showErrorPage(HttpServletRequest request, HttpServletResponse response, String error, String message) throws ServletException, IOException
    {
        showErrorPage(request, response, error, message, error);
    }

    protected void showErrorPage(HttpServletRequest request, HttpServletResponse response, String error, String message, String title) throws ServletException, IOException
    {
        request.setAttribute("title", title);
        request.setAttribute("error", error);
        request.setAttribute("message", message);
        request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        var user = getCurrentUser(request);
        request.setAttribute("user", user);

        super.service(request, response);
    }
}
