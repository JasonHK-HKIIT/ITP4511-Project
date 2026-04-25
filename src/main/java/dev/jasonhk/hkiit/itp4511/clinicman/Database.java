package dev.jasonhk.hkiit.itp4511.clinicman;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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

    public Connection getConnection(boolean autoCommit) throws SQLException
    {
        var connection = getConnection();
        connection.setAutoCommit(autoCommit);
        return connection;
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

    public List<Clinic> getClinics()
    {
        var clinics = new ArrayList<Clinic>();

        try (var c = getConnection())
        {
            var s = c.createStatement();
            var rs = s.executeQuery("SELECT * FROM clinics");
            while (rs.next()) { clinics.add(Clinic.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return clinics;
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

    public List<Service> getServices()
    {
        var services = new ArrayList<Service>();

        try (var c = getConnection())
        {
            var s = c.createStatement();
            var rs = s.executeQuery("SELECT * FROM services");
            while (rs.next()) { services.add(Service.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return services;
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

    public List<ClinicService> getClinicServices()
    {
        var clinicServices = new ArrayList<ClinicService>();

        try (var c = getConnection())
        {
            var s = c.createStatement();
            var rs = s.executeQuery("SELECT * FROM clinic_services");
            while (rs.next()) { clinicServices.add(ClinicService.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return clinicServices;
    }

    public Timeslot getTimeslotById(int id)
    {
        try (var c = getConnection())
        {
            var ps = c.prepareStatement("SELECT * FROM timeslots WHERE id = ?");
            ps.setInt(1, id);

            var rs = ps.executeQuery();
            if (rs.next()) { return Timeslot.from(rs); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public List<Timeslot> getTimeslots()
    {
        var timeslots = new ArrayList<Timeslot>();

        try (var c = getConnection())
        {
            var s = c.createStatement();
            var rs = s.executeQuery("SELECT * FROM timeslots");
            while (rs.next()) { timeslots.add(Timeslot.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return timeslots;
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

    public Appointment getAppointmentById(int id)
    {
        try (var c = getConnection())
        {
            var ps = c.prepareStatement("SELECT * FROM appointments WHERE id = ?");
            ps.setInt(1, id);

            var rs = ps.executeQuery();
            if (rs.next()) { return Appointment.from(rs); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public Appointment getAppointmentByIdAndPatient(int id, User patient)
    {
        return getAppointmentByIdAndPatient(id, patient.getId());
    }

    public Appointment getAppointmentByIdAndPatient(int id, int patientId)
    {
        try (var c = getConnection())
        {
            var ps = c.prepareStatement("SELECT * FROM appointments WHERE id = ? AND patient_id = ?");
            ps.setInt(1, id);
            ps.setInt(2, patientId);

            var rs = ps.executeQuery();
            if (rs.next()) { return Appointment.from(rs); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public Appointment bookAppointment(int patientId, int timeslotId)
    {
        try (var c = getConnection())
        {
            c.setAutoCommit(false);

            int id;
            try (var ps = c.prepareStatement("INSERT INTO appointments (patient_id, timeslot_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS))
            {
                ps.setInt(1, patientId);
                ps.setInt(2, timeslotId);

                ps.executeUpdate();
                var rs = ps.getGeneratedKeys();
                if (!rs.next()) { throw new IllegalStateException("No appointment was inserted"); }

                id = rs.getInt(1);
            }
            catch (SQLException | IllegalStateException e)
            {
                c.rollback();
                throw e;
            }

            updateBookedCountForTimeslot(c, timeslotId, 1);

            try (var ps = c.prepareStatement("SELECT * FROM appointments WHERE id = ?"))
            {
                ps.setInt(1, id);

                var rs = ps.executeQuery();
                if (!rs.next()) { throw new IllegalStateException(String.format("Failed to query appointment #%d", id)); }

                c.commit();
                return Appointment.from(rs);
            }
            catch (SQLException | IllegalStateException e)
            {
                c.rollback();
                throw e;
            }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void rescheduleAppointmentByPatient(int id, int timeslotId, User patient)
    {
        rescheduleAppointmentByPatient(id, timeslotId, patient.getId());
    }

    public void rescheduleAppointmentByPatient(int id, int timeslotId, int patientId)
    {
        try (var c = getConnection(false))
        {
            int oldTimeslotId;
            try (var ps = c.prepareStatement("SELECT timeslot_id FROM appointments WHERE id = ? AND patient_id = ?"))
            {
                ps.setInt(1, id);
                ps.setInt(2, patientId);

                var rs = ps.executeQuery();
                if (!rs.next()) { throw new IllegalStateException(String.format("Failed to query appointment #%d", id)); }

                oldTimeslotId = rs.getInt("timeslot_id");
            }
            catch (SQLException | IllegalStateException e)
            {
                c.rollback();
                throw e;
            }

            try (var ps = c.prepareStatement("UPDATE appointments SET timeslot_id = ? WHERE id = ?"))
            {
                ps.setInt(1, timeslotId);
                ps.setInt(2, id);

                var affectedRows = ps.executeUpdate();
                if (affectedRows == 0) { throw new IllegalStateException(String.format("Failed to update timeslot #%s", timeslotId)); }
            }
            catch (SQLException | IllegalStateException e)
            {
                c.rollback();
                throw e;
            }

            updateBookedCountForTimeslot(c, oldTimeslotId, -1);
            updateBookedCountForTimeslot(c, timeslotId, 1);

            c.commit();
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void cancelAppointmentByPatient(int id, User patient)
    {
        cancelAppointmentByPatient(id, patient.getId());
    }

    public void cancelAppointmentByPatient(int id, int patientId)
    {
        try (var c = getConnection(false))
        {
            int timeslotId;
            try (var ps = c.prepareStatement("SELECT timeslot_id FROM appointments WHERE id = ? AND patient_id = ?"))
            {
                ps.setInt(1, id);
                ps.setInt(2, patientId);

                var rs = ps.executeQuery();
                if (!rs.next()) { throw new IllegalStateException(String.format("Failed to query appointment #%d", id)); }

                timeslotId = rs.getInt("timeslot_id");
            }
            catch (SQLException | IllegalStateException e)
            {
                c.rollback();
                throw e;
            }

            try (var ps = c.prepareStatement("UPDATE appointments SET status = 'CANCELLED', cancel_reason = 'Cancelled by patient' WHERE id = ?"))
            {
                ps.setInt(1, id);

                var affectedRows = ps.executeUpdate();
                if (affectedRows == 0) { throw new IllegalStateException(String.format("Failed to update timeslot #%s", timeslotId)); }
            }
            catch (SQLException | IllegalStateException e)
            {
                c.rollback();
                throw e;
            }

            updateBookedCountForTimeslot(c, timeslotId, -1);

            c.commit();
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    private void updateBookedCountForTimeslot(Connection c, int timeslotId, int delta) throws SQLException
    {
        try (var ps = c.prepareStatement("UPDATE timeslots SET booked_count = booked_count + ? WHERE id = ?"))
        {
            ps.setInt(1, delta);
            ps.setInt(2, timeslotId);

            var affectedRows = ps.executeUpdate();
            if (affectedRows == 0) { throw new IllegalStateException(String.format("Failed to update timeslot #%s", timeslotId)); }
        }
        catch (SQLException | IllegalStateException e)
        {
            c.rollback();
            throw e;
        }
    }

    public List<Appointment> getAppointmentsByPatient(User patient)
    {
        var appointments = new ArrayList<Appointment>();

        try (var c = getConnection())
        {
            var ps = c.prepareStatement(
                    """
                    SELECT appointments.* FROM appointments
                        LEFT JOIN timeslots ON appointments.timeslot_id = timeslots.id
                    WHERE patient_id = ?
                    ORDER BY slot_date DESC, start_time
                    """);
            ps.setInt(1, patient.getId());

            var rs = ps.executeQuery();
            while (rs.next()) { appointments.add(Appointment.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return appointments;
    }

    public QueueTicket joinQueue(int patientId, int clinicServiceId)
    {
        var today = LocalDate.now();

        try (var c = getConnection(false))
        {
            int queueNumber;
            try (var ps = c.prepareStatement("INSERT INTO queues (clinic_service_id, queue_date, last_queue_number) VALUES (?, ?, 1) ON DUPLICATE KEY UPDATE last_queue_number = LAST_INSERT_ID(last_queue_number + 1)", Statement.RETURN_GENERATED_KEYS))
            {
                ps.setInt(1, clinicServiceId);
                ps.setObject(2, today);

                ps.executeUpdate();
                var rs = ps.getGeneratedKeys();
                if (!rs.next()) { throw new IllegalStateException("No queue was inserted/updated"); }

                queueNumber = rs.getInt(1);
            }
            catch (SQLException | IllegalStateException e)
            {
                c.rollback();
                throw e;
            }

            int id;
            try (var ps = c.prepareStatement("INSERT INTO queue_tickets (patient_id, clinic_service_id, queue_date, queue_number) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS))
            {
                ps.setInt(1, patientId);
                ps.setInt(2, clinicServiceId);
                ps.setObject(3, today);
                ps.setInt(4, queueNumber);

                ps.executeUpdate();
                var rs = ps.getGeneratedKeys();
                if (!rs.next()) { throw new IllegalStateException("No queue ticket was inserted"); }

                id = rs.getInt(1);
            }
            catch (SQLException | IllegalStateException e)
            {
                c.rollback();
                throw e;
            }

            try (var ps = c.prepareStatement("SELECT * FROM queue_tickets WHERE id = ?"))
            {
                ps.setInt(1, id);

                var rs = ps.executeQuery();
                if (!rs.next()) { throw new IllegalStateException(String.format("Failed to query queue ticket #%d", id)); }

                c.commit();
                return QueueTicket.from(rs);
            }
            catch (SQLException | IllegalStateException e)
            {
                c.rollback();
                throw e;
            }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<QueueTicket> getQueueTicketsByPatient(User patient)
    {
        var appointments = new ArrayList<QueueTicket>();

        try (var c = getConnection())
        {
            var ps = c.prepareStatement("SELECT * FROM queue_tickets WHERE patient_id = ? ORDER BY queue_date DESC");
            ps.setInt(1, patient.getId());

            var rs = ps.executeQuery();
            while (rs.next()) { appointments.add(QueueTicket.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return appointments;
    }
}
