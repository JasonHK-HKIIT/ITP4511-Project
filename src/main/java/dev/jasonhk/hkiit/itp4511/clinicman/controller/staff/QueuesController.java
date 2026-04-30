package dev.jasonhk.hkiit.itp4511.clinicman.controller.staff;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dev.jasonhk.hkiit.itp4511.clinicman.bean.QueueTicket;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.QueueTicketStatus;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.Service;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.User;
import dev.jasonhk.hkiit.itp4511.clinicman.controller.Controller;

@WebServlet("/staff/queues")
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
                var queueTickets = database.getQueueTicketsByClinicAndDate(Objects.requireNonNull(user.getClinicId()), LocalDate.now()).stream()
                        .collect(Collectors.groupingBy(QueueTicket::getClinicServiceId));

                var patients = database.getUsers().stream()
                        .collect(Collectors.toMap(User::getId, Function.identity()));
                var services = database.getServices().stream()
                        .collect(Collectors.toMap(Service::getId, Function.identity()));
                var clinicServices = database.getClinicServicesByClinic(user.getClinicId());

                request.setAttribute("queueTickets", queueTickets);
                request.setAttribute("patients", patients);
                request.setAttribute("services", services);
                request.setAttribute("clinicServices", clinicServices);
                request.getRequestDispatcher("/WEB-INF/staff/queues/list.jsp").forward(request, response);
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
            case "call" ->
            {
                var id = Integer.parseInt(request.getParameter("id"));
                var queueTicket = database.getQueueTicketById(id);
                queueTicket.setStatus(QueueTicketStatus.CALLED);

                database.updateQueueTicket(queueTicket);
                //database.createQueueTicketNotification(queueTicket);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            case "complete" ->
            {
                var id = Integer.parseInt(request.getParameter("id"));
                var queueTicket = database.getQueueTicketById(id);
                queueTicket.setStatus(QueueTicketStatus.COMPLETED);

                database.updateQueueTicket(queueTicket);
                //database.removeNotificationOnUpdate(queueTicket);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            case "skip" ->
            {
                var id = Integer.parseInt(request.getParameter("id"));
                var queueTicket = database.getQueueTicketById(id);
                queueTicket.setStatus(QueueTicketStatus.SKIPPED);

                database.updateQueueTicket(queueTicket);
                //database.removeNotificationOnUpdate(queueTicket);
                //database.createQueueTicketNotification(queueTicket);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Action %s is not supported", action));
        }
    }
}