package dev.jasonhk.hkiit.itp4511.clinicman.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Service
{
    private int id;
    private String name;
    private int defaultDurationMinutes;

    public Service(String name, int defaultDurationMinutes)
    {
        this.name = name;
        this.defaultDurationMinutes = defaultDurationMinutes;
    }

    public Service(int id, String name, int defaultDurationMinutes)
    {
        this.id = id;
        this.name = name;
        this.defaultDurationMinutes = defaultDurationMinutes;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getDefaultDurationMinutes()
    {
        return defaultDurationMinutes;
    }

    public void setDefaultDurationMinutes(int defaultDurationMinutes)
    {
        this.defaultDurationMinutes = defaultDurationMinutes;
    }

    public static Service from(ResultSet rs) throws SQLException
    {
        return new Service(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("default_duration_minutes"));
    }
}
