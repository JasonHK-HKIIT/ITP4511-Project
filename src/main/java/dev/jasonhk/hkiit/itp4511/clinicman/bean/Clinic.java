package dev.jasonhk.hkiit.itp4511.clinicman.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.format.DateTimeFormatter;

public class Clinic
{
    private static final DateTimeFormatter OPENING_HOURS = DateTimeFormatter.ofPattern("HH:mm");

    private int id;
    private String location;
    private Time openingTime;
    private Time closingTime;
    private boolean walkinEnabled;

    public Clinic(String location, Time openingTime, Time closingTime, boolean walkinEnabled)
    {
        this.location = location;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.walkinEnabled = walkinEnabled;
    }

    public Clinic(int id, String location, Time openingTime, Time closingTime, boolean walkinEnabled)
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

    public Time getOpeningTime()
    {
        return openingTime;
    }

    public void setOpeningTime(Time openingTime)
    {
        this.openingTime = openingTime;
    }

    public Time getClosingTime()
    {
        return closingTime;
    }

    public void setClosingTime(Time closingTime)
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

    public String toFormattedOpeningTime()
    {
        return openingTime.toLocalTime().format(OPENING_HOURS);
    }

    public String toFormattedClosingTime()
    {
        return closingTime.toLocalTime().format(OPENING_HOURS);
    }

    public static Clinic from(ResultSet rs) throws SQLException
    {
        return new Clinic(
                rs.getInt("id"),
                rs.getString("location"),
                rs.getTime("opening_time"),
                rs.getTime("closing_time"),
                rs.getBoolean("is_walkin_enabled"));
    }
}
