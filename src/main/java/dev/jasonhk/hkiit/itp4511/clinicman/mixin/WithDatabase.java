package dev.jasonhk.hkiit.itp4511.clinicman.mixin;

import jakarta.servlet.ServletConfig;
import dev.jasonhk.hkiit.itp4511.clinicman.Database;

public interface WithDatabase
{
    default Database getDatabase(ServletConfig config)
    {
        var context = config.getServletContext();
        var url = context.getInitParameter("database_url");
        var user = context.getInitParameter("database_user");
        var password = context.getInitParameter("database_password");

        return new Database(url, user, password);
    }
}
