package dev.jasonhk.hkiit.itp4511.clinicman.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/index.jsp")
public class HomeController extends Controller
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        var user = getCurrentUser(request);
        switch (user.getRole())
        {
            case PATIENT ->
            {
                request.getRequestDispatcher("/WEB-INF/patient/home.jsp").forward(request, response);
            }
            case STAFF ->
            {
                request.getRequestDispatcher("/WEB-INF/staff/home.jsp").forward(request, response);
            }
            case ADMIN ->
            {
                request.getRequestDispatcher("/WEB-INF/admin/home.jsp").forward(request, response);
            }
        }
    }
}
