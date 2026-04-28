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

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        var user = getCurrentUser(request);
        request.setAttribute("user", user);

        super.service(request, response);
    }
}
