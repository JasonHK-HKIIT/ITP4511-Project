package dev.jasonhk.hkiit.itp4511.clinicman;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dev.jasonhk.hkiit.itp4511.clinicman.bean.*;

public class Database
{
    private final String url;
    private final String user;
    private final String password;

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
            Class.forName("org.mariadb.jdbc.Driver");
        }
        catch (ClassNotFoundException e) { throw new RuntimeException(e); }
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
            if (rs.next()) { return User.from(rs); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public List<Clinic> getClinics()
    {
        var clinics = new ArrayList<Clinic>();

        try (var c = getConnection())
        {
            var ps = c.prepareStatement("SELECT * FROM clinics");
            var rs = ps.executeQuery();
            while (rs.next()) { clinics.add(Clinic.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return clinics;
    }

    public Clinic getClinicById(int id)
    {
        try (var c = getConnection())
        {
            var ps = c.prepareStatement("SELECT * FROM clinics WHERE id = ?");
            ps.setInt(1, id);

            var rs = ps.executeQuery();
            if (rs.next()) { return Clinic.from(rs); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public List<Service> getServices()
    {
        var services = new ArrayList<Service>();

        try (var c = getConnection())
        {
            var ps = c.prepareStatement("SELECT * FROM services");
            var rs = ps.executeQuery();
            while (rs.next()) { services.add(Service.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return services;
    }

    public Service getServiceById(int id)
    {
        try (var c = getConnection())
        {
            var ps = c.prepareStatement("SELECT * FROM services WHERE id = ?");
            ps.setInt(1, id);

            var rs = ps.executeQuery();
            if (rs.next()) { return Service.from(rs); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public List<ClinicService> getClinicServices()
    {
        var clinicServices = new ArrayList<ClinicService>();

        try (var c = getConnection())
        {
            var ps = c.prepareStatement("SELECT * FROM clinic_services");
            var rs = ps.executeQuery();
            while (rs.next()) { clinicServices.add(ClinicService.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return clinicServices;
    }

    public ClinicService getClinicServiceById(int id)
    {
        try (var c = getConnection())
        {
            var ps = c.prepareStatement("SELECT * FROM clinic_services WHERE id = ?");
            ps.setInt(1, id);

            var rs = ps.executeQuery();
            if (rs.next()) { return ClinicService.from(rs); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public List<Timeslot> getTimeslotsByClinicServiceAndDate(ClinicService clinicService, LocalDate date)
    {
        return getTimeslotsByClinicServiceAndDate(clinicService.getId(), date);
    }

    public List<Timeslot> getTimeslotsByClinicServiceAndDate(int clinicServiceId, LocalDate date)
    {
        var timeslots = new ArrayList<Timeslot>();

        try (var c = getConnection())
        {
            var ps = c.prepareStatement("SELECT * FROM timeslots WHERE clinic_service_id = ? AND slot_date = ? ORDER BY start_time");
            ps.setInt(1, clinicServiceId);
            ps.setObject(2, date);

            var rs = ps.executeQuery();
            while (rs.next()) { timeslots.add(Timeslot.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return timeslots;
    }

//    public List<Appointment> getAppointmentsByPatient(User user)
//    {
//
//    }
}
