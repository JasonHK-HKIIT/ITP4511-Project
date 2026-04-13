package dev.jasonhk.hkiit.itp4511.clinicman;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import dev.jasonhk.hkiit.itp4511.clinicman.bean.Gender;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.Role;
import dev.jasonhk.hkiit.itp4511.clinicman.bean.User;

public class Database
{
    private String url;
    private String user;
    private String password;

    public Database(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }

        return DriverManager.getConnection(url, user, password);
    }

    public User getUserByCredentials(String username, String password)
    {
        try (var c = getConnection())
        {
            var ps = c.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);

            var rs = ps.executeQuery();
            if (rs.next())
            {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getObject("role", Role.class),
                        rs.getObject("gender", Gender.class),
                        rs.getDate("date_of_birth"));
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return null;
    }
}
