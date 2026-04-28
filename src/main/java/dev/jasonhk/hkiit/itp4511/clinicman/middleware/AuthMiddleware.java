package dev.jasonhk.hkiit.itp4511.clinicman.middleware;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dev.jasonhk.hkiit.itp4511.clinicman.mixin.WithUser;

@WebFilter("/*")
public class AuthMiddleware implements Filter, WithUser
{
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException
    {
        var request = (HttpServletRequest) req;
        var response = (HttpServletResponse) res;

        var requestPath = request.getRequestURI();
        if (requestPath.startsWith("/css") || requestPath.startsWith("/js"))
        {
            chain.doFilter(req, res);
            return;
        }

        switch (requestPath)
        {
            case "/login", "/logout", "/register" -> chain.doFilter(req, res);
            default ->
            {
                var user = getCurrentUser(request);
                if (user == null)
                {
                    response.sendRedirect("/login");
                    return;
                }

                var isAccessible = switch (requestPath)
                {
                    case "/", "/profile" -> true;
                    default -> switch (user.getRole())
                    {
                        case PATIENT -> (!requestPath.startsWith("/staff") && !requestPath.startsWith("/admin"));
                        case STAFF -> requestPath.startsWith("/staff");
                        case ADMIN -> requestPath.startsWith("/admin");
                    };
                };

                if (isAccessible)
                {
                    chain.doFilter(req, res);
                    return;
                }

                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Permission denied");
            }
        }
    }
}
