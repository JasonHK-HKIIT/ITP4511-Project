package dev.jasonhk.hkiit.itp4511.clinicman.controller.admin;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import dev.jasonhk.hkiit.itp4511.clinicman.bean.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dev.jasonhk.hkiit.itp4511.clinicman.controller.Controller;

@WebServlet("/admin/reports")
public class ReportsController extends Controller
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        var type = Objects.requireNonNullElse(request.getParameter("type"), "").toLowerCase();
        switch (type)
        {
            case "" ->
            {
                var clinics = database.getClinics();

                request.setAttribute("clinics", clinics);
                request.getRequestDispatcher("/WEB-INF/admin/reports/index.jsp").forward(request, response);
            }
            case "appointments" ->
            {
                var clinicId = Integer.parseInt(request.getParameter("clinicId"));
                var appointments = database.getAppointmentsByClinic(clinicId);

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

                request.setAttribute("appointments", appointments);
                request.setAttribute("patients", patients);
                request.setAttribute("clinics", clinics);
                request.setAttribute("services", services);
                request.setAttribute("clinicServices", clinicServices);
                request.setAttribute("timeslots", timeslots);
                request.getRequestDispatcher("/WEB-INF/admin/reports/appointments.jsp").forward(request, response);
            }
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Report type %s is not supported", type));
        }
    }
}
