package dev.jasonhk.hkiit.itp4511.clinicman.controller.patient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dev.jasonhk.hkiit.itp4511.clinicman.bean.Clinic;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.ClinicService;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.Service;
import dev.jasonhk.hkiit.itp4511.clinicman.controller.Controller;

@WebServlet("/queues")
public class QueuesController extends Controller
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        var action = Objects.requireNonNullElse(request.getParameter("action"), "list").toLowerCase();
        switch (action)
        {
            case "list" ->
            {
                var user = getCurrentUser(request);
                var queueTickets = database.getQueueTicketsByPatient(user);
                var clinics = database.getClinics().stream()
                        .collect(Collectors.toMap(Clinic::getId, Function.identity()));
                var services = database.getServices().stream()
                        .collect(Collectors.toMap(Service::getId, Function.identity()));
                var clinicServices = database.getClinicServices().stream()
                        .collect(Collectors.toMap(ClinicService::getId, Function.identity()));

                request.setAttribute("queueTickets", queueTickets);
                request.setAttribute("clinics", clinics);
                request.setAttribute("services", services);
                request.setAttribute("clinicServices", clinicServices);
                request.getRequestDispatcher("/WEB-INF/patient/queues/list.jsp").forward(request, response);
            }
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Action %s is not supported", action));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        var action = Objects.requireNonNullElse(request.getParameter("action"), "list").toLowerCase();
        switch (action)
        {
            case "join" ->
            {
                var user = getCurrentUser(request);
                var id = Integer.parseInt(request.getParameter("service"));
                database.joinQueue(user, id);

                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            case "leave" ->
            {
                var user = getCurrentUser(request);
                var id = Integer.parseInt(request.getParameter("id"));
                database.leaveQueueByPatient(id, user);

                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Action %s is not supported", action));
        }
    }
}
