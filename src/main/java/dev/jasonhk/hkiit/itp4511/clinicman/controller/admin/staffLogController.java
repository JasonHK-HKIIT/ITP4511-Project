package dev.jasonhk.hkiit.itp4511.clinicman.controller.admin;

import dev.jasonhk.hkiit.itp4511.clinicman.bean.ClinicService;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.Service;
import dev.jasonhk.hkiit.itp4511.clinicman.controller.Controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Collectors;


@WebServlet("/admin/staffLog")
public class staffLogController extends Controller {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        var log = database.getStaffLog();
        request.setAttribute("staffLog", log);
        request.getRequestDispatcher("/WEB-INF/admin/staffLog.jsp").forward(request, response);
    }
}
