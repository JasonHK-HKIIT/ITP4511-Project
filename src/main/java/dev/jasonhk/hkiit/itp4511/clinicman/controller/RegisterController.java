package dev.jasonhk.hkiit.itp4511.clinicman.controller;

import java.io.IOException;
import java.time.LocalDate;

import dev.jasonhk.hkiit.itp4511.clinicman.bean.Gender;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterController extends Controller
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (getCurrentUser(request) != null)
        {
            response.sendRedirect("/");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (getCurrentUser(request) != null)
        {
            response.sendRedirect("/");
            return;
        }

        var gender = request.getParameter("gender");
        var dateOfBirth = request.getParameter("dateOfBirth");

        var user = database.createUser(
                request.getParameter("username"),
                request.getParameter("password"),
                request.getParameter("fullName"),
                request.getParameter("phone"),
                Role.PATIENT,
                null,
                ((gender != null) && !gender.isBlank()) ? Gender.valueOf(gender) : null,
                ((dateOfBirth != null) && !dateOfBirth.isBlank()) ? LocalDate.parse(dateOfBirth) : null);

        setCurrentUser(request, user);
        response.sendRedirect("/");
    }
}
