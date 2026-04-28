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

import dev.jasonhk.hkiit.itp4511.clinicman.bean.AppointmentStatus;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.Clinic;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.ClinicService;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.Service;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.Timeslot;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.User;
import dev.jasonhk.hkiit.itp4511.clinicman.controller.Controller;

@WebServlet("/staff/appointments")
public class AppointmentsController extends Controller
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        var action = Objects.requireNonNullElse(request.getParameter("action"), "list").toLowerCase();
        //noinspection SwitchStatementWithTooFewBranches
        switch (action)
        {
            case "list" ->
            {
                var user = getCurrentUser(request);
                var appointments = database.getAppointmentsByClinic(Objects.requireNonNull(user.getClinicId()));

                var patients = database.getUsers().stream()
                        .collect(Collectors.toMap(User::getId, Function.identity()));
                var clinics = database.getClinics().stream()
                        .collect(Collectors.toMap(Clinic::getId, Function.identity()));
                var services = database.getServices().stream()
                        .collect(Collectors.toMap(Service::getId, Function.identity()));
                var clinicServices = database.getClinicServices().stream()
                        .collect(Collectors.toMap(ClinicService::getId, Function.identity()));
                var timeslots = database.getTimeslots().stream()
                        .collect(Collectors.toMap(Timeslot::getId, Function.identity()));
                var today = LocalDate.now();

                request.setAttribute("appointments", appointments);
                request.setAttribute("patients", patients);
                request.setAttribute("clinics", clinics);
                request.setAttribute("services", services);
                request.setAttribute("clinicServices", clinicServices);
                request.setAttribute("timeslots", timeslots);
                request.setAttribute("today", today);
                request.getRequestDispatcher("/WEB-INF/staff/appointments/list.jsp").forward(request, response);
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
                database.bookAppointment(user.getId(), timeslotId);

                response.sendRedirect("/appointments");
            }
            case "confirm" ->
            {
                var id = Integer.parseInt(request.getParameter("id"));
                var appointment = database.getAppointmentById(id);
                appointment.setStatus(AppointmentStatus.CONFIRMED);

                database.updateAppointment(appointment);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            case "check-in" ->
            {
                var id = Integer.parseInt(request.getParameter("id"));
                var appointment = database.getAppointmentById(id);
                appointment.setStatus(AppointmentStatus.ARRIVED);

                database.updateAppointment(appointment);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            case "complete" ->
            {
                var id = Integer.parseInt(request.getParameter("id"));
                var appointment = database.getAppointmentById(id);
                appointment.setStatus(AppointmentStatus.COMPLETED);

                database.updateAppointment(appointment);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            case "no-show" ->
            {
                var id = Integer.parseInt(request.getParameter("id"));
                var appointment = database.getAppointmentById(id);
                appointment.setStatus(AppointmentStatus.NO_SHOW);

                database.updateAppointment(appointment);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            case "cancel" ->
            {
                var id = Integer.parseInt(request.getParameter("id"));
                var cancelReason = request.getParameter("cancelReason");
                database.cancelAppointmentByStaff(id, ((cancelReason != null) && !cancelReason.isBlank()) ? cancelReason : null);

                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Action %s is not supported", action));
        }
    }
}
