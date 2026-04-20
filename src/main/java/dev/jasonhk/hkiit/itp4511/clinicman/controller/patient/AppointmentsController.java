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
import dev.jasonhk.hkiit.itp4511.clinicman.bean.Timeslot;
import dev.jasonhk.hkiit.itp4511.clinicman.controller.Controller;

@WebServlet("/appointments")
public class AppointmentsController extends Controller
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
                var appointments = database.getAppointmentsByPatient(user);
                var clinics = database.getClinics().stream()
                        .collect(Collectors.toMap(Clinic::getId, Function.identity()));
                var services = database.getServices().stream()
                        .collect(Collectors.toMap(Service::getId, Function.identity()));
                var clinicServices = database.getClinicServices().stream()
                        .collect(Collectors.toMap(ClinicService::getId, Function.identity()));
                var timeslots = database.getTimeslots().stream()
                        .collect(Collectors.toMap(Timeslot::getId, Function.identity()));

                request.setAttribute("appointments", appointments);
                request.setAttribute("clinics", clinics);
                request.setAttribute("services", services);
                request.setAttribute("clinicServices", clinicServices);
                request.setAttribute("timeslots", timeslots);
                request.getRequestDispatcher("/WEB-INF/patient/appointments/list.jsp").forward(request, response);
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
            case "reschedule" ->
            {
                var user = getCurrentUser(request);
                var id = Integer.parseInt(request.getParameter("id"));
                var appointment = database.getAppointmentByIdAndPatient(id, user);
                var timeslot = database.getTimeslotById(appointment.getTimeslotId());
                var clinicService = database.getClinicServiceById(timeslot.getClinicServiceId());
                var clinic = database.getClinicById(clinicService.getClinicId());
                var service = database.getServiceById(clinicService.getServiceId());
                var timeslots = database.getTimeslotsByClinicServiceAndDate(clinicService, timeslot.getSlotDate());

                request.setAttribute("timeslot", timeslot);
                request.setAttribute("clinicService", clinicService);
                request.setAttribute("clinic", clinic);
                request.setAttribute("service", service);
                request.setAttribute("timeslots", timeslots);
                request.getRequestDispatcher("/WEB-INF/patient/appointments/reschedule.jsp").forward(request, response);
            }
            case "timeslots" ->
            {
                var id = Integer.parseInt(request.getParameter("service"));
                var date = LocalDate.parse(request.getParameter("date"));
                var timeslots = database.getTimeslotsByClinicServiceAndDate(id, date);

                request.setAttribute("timeslots", timeslots);
                request.getRequestDispatcher("/WEB-INF/patient/appointments/timeslots.jsp").forward(request, response);
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
            case "book" ->
            {
                var user = getCurrentUser(request);
                var timeslotId = Integer.parseInt(request.getParameter("timeslot"));
                var appointment = database.bookAppointment(user.getId(), timeslotId);

                response.sendRedirect("/appointments");
            }
            case "reschedule" ->
            {
                var user = getCurrentUser(request);
                var id = Integer.parseInt(request.getParameter("id"));
                var timeslotId = Integer.parseInt(request.getParameter("timeslot"));
                database.rescheduleAppointmentByPatient(id, timeslotId, user);

                response.sendRedirect("/appointments");
            }
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Action %s is not supported", action));
        }
    }
}
