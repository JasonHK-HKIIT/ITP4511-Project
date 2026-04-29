package dev.jasonhk.hkiit.itp4511.clinicman.controller.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
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
import dev.jasonhk.hkiit.itp4511.clinicman.bean.User;
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
            case "no-show" ->
            {
                var records = new HashMap<Integer, Integer>();
                try (var c = database.getConnection())
                {
                    var rs = c.createStatement().executeQuery(
                            """
                            SELECT
                                clinic_services.id AS id, COUNT(appointments.id) AS total_no_shows
                            FROM appointments
                                INNER JOIN timeslots ON appointments.timeslot_id = timeslots.id
                                INNER JOIN clinic_services ON timeslots.clinic_service_id = clinic_services.id
                            WHERE appointments.status = 'NO_SHOW'
                            GROUP BY clinic_services.clinic_id, clinic_services.service_id
                            ORDER BY
                                total_no_shows DESC,
                                clinic_services.clinic_id,
                                clinic_services.service_id
                            """);

                    while (rs.next())
                    {
                        records.put(rs.getInt("id"), rs.getInt("total_no_shows"));
                    }
                }
                catch (SQLException e)
                {
                    throw new RuntimeException(e);
                }

                var clinics = database.getClinics().stream()
                        .collect(Collectors.toMap(Clinic::getId, Function.identity()));
                var services = database.getServices().stream()
                        .collect(Collectors.toMap(Service::getId, Function.identity()));
                var clinicServices = database.getClinicServices().stream()
                        .collect(Collectors.toMap(ClinicService::getId, Function.identity()));

                request.setAttribute("records", records);
                request.setAttribute("clinics", clinics);
                request.setAttribute("services", services);
                request.setAttribute("clinicServices", clinicServices);
                request.getRequestDispatcher("/WEB-INF/admin/reports/no-show.jsp").forward(request, response);
            }
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Report type %s is not supported", type));
        }
    }
}
