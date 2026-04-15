package dev.jasonhk.hkiit.itp4511.clinicman.controller.patient;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

import dev.jasonhk.hkiit.itp4511.clinicman.bean.Appointment;
import dev.jasonhk.hkiit.itp4511.clinicman.mixin.WithUser;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dev.jasonhk.hkiit.itp4511.clinicman.Database;
import dev.jasonhk.hkiit.itp4511.clinicman.mixin.WithDatabase;

@WebServlet("/appointments")
public class AppointmentsController extends HttpServlet implements WithDatabase, WithUser
{
    private Database database;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        database = getDatabase(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        var action = Objects.requireNonNullElse(request.getParameter("action"), "list").toLowerCase();
        switch (action)
        {
            case "list" ->
            {

            }
            case "book" ->
            {
                var clinicServiceId = Integer.parseInt(request.getParameter("service"));
                var clinicService = database.getClinicServiceById(clinicServiceId);
                var clinic = database.getClinicById(clinicService.getClinicId());
                var service = database.getServiceById(clinicService.getServiceId());
                var tomorrow = LocalDate.now().plusDays(1);
                var timeslots = database.getTimeslotsByClinicServiceAndDate(clinicService, tomorrow);

                request.setAttribute("clinicService", clinicService);
                request.setAttribute("clinic", clinic);
                request.setAttribute("service", service);
                request.setAttribute("tomorrow", tomorrow);
                request.setAttribute("timeslots", timeslots);
                request.getRequestDispatcher("/WEB-INF/patient/appointments/book.jsp").forward(request, response);
            }
            case "fetch" ->
            {
                var type = Objects.requireNonNullElse(request.getParameter("type"), "null").toLowerCase();
                //noinspection SwitchStatementWithTooFewBranches
                switch (type)
                {
                    case "timeslots" ->
                    {
                        var id = Integer.parseInt(request.getParameter("service"));
                        var date = LocalDate.parse(request.getParameter("date"));
                        var timeslots = database.getTimeslotsByClinicServiceAndDate(id, date);

                        request.setAttribute("timeslots", timeslots);
                        request.getRequestDispatcher("/WEB-INF/patient/appointments/book-timeslots.jsp").forward(request, response);
                    }
                    default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Type %s is not supported", type));
                }
            }
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Action %s is not supported", action));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        var action = Objects.requireNonNullElse(request.getParameter("action"), "list").toLowerCase();
        //noinspection SwitchStatementWithTooFewBranches
        switch (action)
        {
            case "book" ->
            {
                var user = getCurrentUser(request);
                var timeslotId = Integer.parseInt(request.getParameter("timeslot"));
                // TODO
                // var appointment = new Appointment(user.getId(), timeslotId);
            }
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Action %s is not supported", action));
        }
    }
}
