package dev.jasonhk.hkiit.itp4511.clinicman.mixin;

import jakarta.servlet.http.HttpServletRequest;

import dev.jasonhk.hkiit.itp4511.clinicman.bean.User;

public interface WithUser
{
    default User getCurrentUser(HttpServletRequest request)
    {
        var session = request.getSession();
        return (User) session.getAttribute("user");
    }

    default void setCurrentUser(HttpServletRequest request, User user)
    {
        var session = request.getSession();
        if (user == null)
        {
            session.invalidate();
        }
        else
        {
            session.setAttribute("user", user);
        }
    }
}
