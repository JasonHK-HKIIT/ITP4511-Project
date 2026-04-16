package dev.jasonhk.hkiit.itp4511.clinicman.controller.patient;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dev.jasonhk.hkiit.itp4511.clinicman.bean.ClinicService;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.Service;
import dev.jasonhk.hkiit.itp4511.clinicman.controller.Controller;

@WebServlet("/clinics")
public class ClinicsController extends Controller
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        var clinics = database.getClinics();
        var clinicServices = database.getClinicServices().stream()
                .collect(Collectors.groupingBy(ClinicService::getClinicId));
        var services = database.getServices().stream()
                .collect(Collectors.toMap(Service::getId, Function.identity()));

        request.setAttribute("clinics", clinics);
        request.setAttribute("clinicServices", clinicServices);
        request.setAttribute("services", services);
        request.getRequestDispatcher("/WEB-INF/patient/clinics.jsp").forward(request, response);
    }
}
