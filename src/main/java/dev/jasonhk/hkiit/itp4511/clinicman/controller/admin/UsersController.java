package dev.jasonhk.hkiit.itp4511.clinicman.controller.admin;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dev.jasonhk.hkiit.itp4511.clinicman.bean.Clinic;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.QueueTicketStatus;
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
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Action %s is not supported", action));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        var action = Objects.requireNonNullElse(request.getParameter("action"), "null").toLowerCase();
        switch (action)
        {
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
