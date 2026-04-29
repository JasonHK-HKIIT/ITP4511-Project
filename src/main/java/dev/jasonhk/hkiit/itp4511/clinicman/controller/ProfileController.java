package dev.jasonhk.hkiit.itp4511.clinicman.controller;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dev.jasonhk.hkiit.itp4511.clinicman.bean.Gender;

@WebServlet("/profile")
public class ProfileController extends Controller
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        var user = database.getUserById(getCurrentUser(request).getId());

        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        var password = request.getParameter("password");
        var gender = request.getParameter("gender");
        var dateOfBirth = request.getParameter("dateOfBirth");

        var user = database.getUserById(getCurrentUser(request).getId());
        user.setUsername(request.getParameter("username"));
        user.setFullName(request.getParameter("fullName"));
        user.setPhone(request.getParameter("phone"));
        user.setGender(((gender != null) && !gender.isBlank()) ? Gender.valueOf(gender) : null);
        user.setDateOfBirth(((dateOfBirth != null) && !dateOfBirth.isBlank()) ? LocalDate.parse(dateOfBirth) : null);

        if (database.updateUser(user)) { setCurrentUser(request, user); }
        if ((password != null) && !password.isEmpty()) { database.updateUserPassword(user, password); }
        doGet(request, response);
    }
}
