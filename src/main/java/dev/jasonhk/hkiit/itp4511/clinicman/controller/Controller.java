package dev.jasonhk.hkiit.itp4511.clinicman.controller;

import dev.jasonhk.hkiit.itp4511.clinicman.Database;
import dev.jasonhk.hkiit.itp4511.clinicman.mixin.WithDatabase;
import dev.jasonhk.hkiit.itp4511.clinicman.mixin.WithUser;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

public class Controller extends HttpServlet implements WithDatabase, WithUser
{
    protected Database database;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        database = getDatabase(config);
    }
}
