package dev.jasonhk.hkiit.itp4511.clinicman.controller.staff;

import dev.jasonhk.hkiit.itp4511.clinicman.controller.Controller;
import dev.jasonhk.hkiit.itp4511.clinicman.mixin.WithUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/staffBoard")
public class StaffBoardController extends Controller implements WithUser {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (!ensureLoggedIn(request, response)) { return; }

        request.getRequestDispatcher("WEB-INF/staff/staffBoard.jsp").forward(request, response);
    }
}
