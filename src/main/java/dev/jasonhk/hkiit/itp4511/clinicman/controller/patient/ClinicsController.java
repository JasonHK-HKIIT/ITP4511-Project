package dev.jasonhk.hkiit.itp4511.clinicman.controller.patient;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Collectors;

import dev.jasonhk.hkiit.itp4511.clinicman.bean.ClinicService;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.Service;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import dev.jasonhk.hkiit.itp4511.clinicman.Database;
import dev.jasonhk.hkiit.itp4511.clinicman.mixin.WithDatabase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/clinics")
public class ClinicsController extends HttpServlet implements WithDatabase
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
