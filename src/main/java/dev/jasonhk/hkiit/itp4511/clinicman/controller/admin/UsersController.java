package dev.jasonhk.hkiit.itp4511.clinicman.controller.admin;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import dev.jasonhk.hkiit.itp4511.clinicman.bean.Gender;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dev.jasonhk.hkiit.itp4511.clinicman.bean.Clinic;
import dev.jasonhk.hkiit.itp4511.clinicman.controller.Controller;

@WebServlet("/admin/users")
public class UsersController extends Controller
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        var action = Objects.requireNonNullElse(request.getParameter("action"), "list").toLowerCase();
        switch (action)
        {
            case "list" ->
            {
                var users = database.getUsers();
                var clinics = database.getClinics().stream()
                        .collect(Collectors.toMap(Clinic::getId, Function.identity()));

                request.setAttribute("users", users);
                request.setAttribute("clinics", clinics);
                request.getRequestDispatcher("/WEB-INF/admin/users/list.jsp").forward(request, response);
            }
            case "create" ->
            {
                var clinics = database.getClinics();

                request.setAttribute("clinics", clinics);
                request.getRequestDispatcher("/WEB-INF/admin/users/create.jsp").forward(request, response);
            }
            case "edit" ->
            {
                var id = Integer.parseInt(request.getParameter("id"));
                var target = database.getUserById(id);

                var clinics = database.getClinics();

                request.setAttribute("target", target);
                request.setAttribute("clinics", clinics);
                request.getRequestDispatcher("/WEB-INF/admin/users/edit.jsp").forward(request, response);
            }
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Action %s is not supported", action));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        var action = Objects.requireNonNullElse(request.getParameter("action"), "null").toLowerCase();
        switch (action)
        {
            case "create" ->
            {
                var role = Role.valueOf(request.getParameter("role"));
                var gender = request.getParameter("gender");
                var dateOfBirth = request.getParameter("dateOfBirth");

                database.createUser(
                        request.getParameter("username"),
                        request.getParameter("password"),
                        request.getParameter("fullName"),
                        request.getParameter("phone"),
                        role,
                        (role == Role.STAFF) ? Integer.parseInt(request.getParameter("clinicId")) : null,
                        ((gender != null) && !gender.isBlank()) ? Gender.valueOf(gender) : null,
                        ((dateOfBirth != null) && !dateOfBirth.isBlank()) ? LocalDate.parse(dateOfBirth) : null);

                response.sendRedirect("/admin/users");
            }
            case "edit" ->
            {
                var id = Integer.parseInt(request.getParameter("id"));
                var target = database.getUserById(id);

                var password = request.getParameter("password");
                var role = Role.valueOf(request.getParameter("role"));
                var gender = request.getParameter("gender");
                var dateOfBirth = request.getParameter("dateOfBirth");

                target.setUsername(request.getParameter("username"));
                target.setFullName(request.getParameter("fullName"));
                target.setPhone(request.getParameter("phone"));
                target.setRole(role);
                target.setClinicId((role == Role.STAFF) ? Integer.parseInt(request.getParameter("clinicId")) : null);
                target.setGender(((gender != null) && !gender.isBlank()) ? Gender.valueOf(gender) : null);
                target.setDateOfBirth(((dateOfBirth != null) && !dateOfBirth.isBlank()) ? LocalDate.parse(dateOfBirth) : null);

                if (database.updateUser(target) && (getCurrentUser(request).getId() == target.getId())) { setCurrentUser(request, target); }
                if ((password != null) && !password.isEmpty()) { database.updateUserPassword(target, password); }

                doGet(request, response);
            }
            case "delete" ->
            {
                var id = Integer.parseInt(request.getParameter("id"));
                database.deleteUserById(id);

                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Action %s is not supported", action));
        }
    }
}
