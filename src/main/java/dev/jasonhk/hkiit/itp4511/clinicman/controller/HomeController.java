package dev.jasonhk.hkiit.itp4511.clinicman.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dev.jasonhk.hkiit.itp4511.clinicman.mixin.WithUser;

@WebServlet("/index.jsp")
public class HomeController extends HttpServlet implements WithUser
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.getRequestDispatcher("WEB-INF/home.jsp").forward(request, response);
    }
}
