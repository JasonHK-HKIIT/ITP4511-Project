package dev.jasonhk.hkiit.itp4511.clinicman.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public class Clinic
{
    private int id;
    private String location;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private boolean walkinEnabled;

    public Clinic(int id, String location, LocalTime openingTime, LocalTime closingTime, boolean walkinEnabled)
    {
        this.id = id;
        this.location = location;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.walkinEnabled = walkinEnabled;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public LocalTime getOpeningTime()
    {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime)
    {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime()
    {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime)
    {
        this.closingTime = closingTime;
    }

    public boolean isWalkinEnabled()
    {
        return walkinEnabled;
    }

    public void setWalkinEnabled(boolean walkinEnabled)
    {
        this.walkinEnabled = walkinEnabled;
    }

    public static Clinic from(ResultSet rs) throws SQLException
    {
        return new Clinic(
                rs.getInt("id"),
                rs.getString("location"),
                rs.getObject("opening_time", LocalTime.class),
                rs.getObject("closing_time", LocalTime.class),
                rs.getBoolean("is_walkin_enabled"));
    }
}
