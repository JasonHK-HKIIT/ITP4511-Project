package dev.jasonhk.hkiit.itp4511.clinicman;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public User getUserById(int id)
    {
        try (var c = getConnection())
        {
            var ps = c.prepareStatement("SELECT * FROM users WHERE id = ?");
            ps.setInt(1, id);

            var rs = ps.executeQuery();
            if (rs.next()) { return User.from(rs); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return null;
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

    public User createUser(String username, String password, String fullName, String phone, Role role, Integer clinicId, Gender gender, LocalDate dateOfBirth)
    {
        try (var c = getConnection(false))
        {
            int id;
            try (var ps = c.prepareStatement("INSERT INTO users (username, password, full_name, phone, role, clinic_id, gender, date_of_birth) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS))
            {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, fullName);
                ps.setString(4, phone);
                ps.setString(5, role.name());
                ps.setObject(6, clinicId);
                ps.setString(7, (gender != null) ? gender.name() : null);
                ps.setObject(8, dateOfBirth);

                ps.executeUpdate();
                var rs = ps.getGeneratedKeys();
                if (!rs.next()) { throw new IllegalStateException("No user was inserted"); }

                id = rs.getInt(1);
            }
            catch (SQLException | IllegalStateException e)
            {
                c.rollback();
                throw e;
            }

            try (var ps = c.prepareStatement("SELECT * FROM users WHERE id = ?"))
            {
                ps.setInt(1, id);

                var rs = ps.executeQuery();
                if (!rs.next()) { throw new IllegalStateException(String.format("Failed to query user #%d", id)); }

                c.commit();
                return User.from(rs);
            }
            catch (SQLException | IllegalStateException e)
            {
                c.rollback();
                throw e;
            }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean updateUser(User user)
    {
        try (var c = getConnection())
        {
            var gender = user.getGender();

            var ps = c.prepareStatement("UPDATE users SET username = ?, full_name = ?, phone = ?, role = ?, clinic_id = ?, gender = ?, date_of_birth = ? WHERE id = ?");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getRole().name());
            ps.setObject(5, user.getClinicId());
            ps.setString(6, (gender != null) ? gender.name() : null);
            ps.setObject(7, user.getDateOfBirth());
            ps.setInt(8, user.getId());

            var affectedRows = ps.executeUpdate();
            return (affectedRows > 0);
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean updateUserPassword(User user, String password)
    {
        return updateUserPassword(user.getId(), password);
    }

    public boolean updateUserPassword(int id, String password)
    {
        try (var c = getConnection())
        {
            var ps = c.prepareStatement("UPDATE users SET password = ? WHERE id = ?");
            ps.setString(1, password);
            ps.setInt(2, id);

            var affectedRows = ps.executeUpdate();
            return (affectedRows > 0);
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean deleteUserById(int id)
    {
        try (var c = getConnection())
        {
            var ps = c.prepareStatement("DELETE FROM users WHERE id = ?");
            ps.setInt(1, id);

            var affectedRows = ps.executeUpdate();
            return (affectedRows > 0);
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<User> getUsers()
    {
        var users = new ArrayList<User>();

        try (var c = getConnection())
        {
            var s = c.createStatement();
            var rs = s.executeQuery("SELECT * FROM users ORDER BY username");
            while (rs.next()) { users.add(User.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return users;
    }

    public List<User> getUsersByIds(List<Integer> ids)
    {
        var users = new ArrayList<User>();

        try (var c = getConnection())
        {
            var ps = c.prepareStatement("SELECT * FROM users WHERE id IN (?)");
            ps.setObject(1, ids.toArray());

            var rs = ps.executeQuery();
            while (rs.next()) { users.add(User.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return users;
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

    public List<ClinicService> getClinicServicesByClinic(int clinicId)
    {
        var clinicServices = new ArrayList<ClinicService>();

        try (var c = getConnection())
        {
            var ps = c.prepareStatement("SELECT * FROM clinic_services WHERE clinic_id = ?");
            ps.setInt(1, clinicId);

            var rs = ps.executeQuery();
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

    public void cancelAppointmentByStaff(int id, String cancelReason)
    {
        try (var c = getConnection(false))
        {
            int timeslotId;
            try (var ps = c.prepareStatement("SELECT timeslot_id FROM appointments WHERE id = ?"))
            {
                ps.setInt(1, id);

                var rs = ps.executeQuery();
                if (!rs.next()) { throw new IllegalStateException(String.format("Failed to query appointment #%d", id)); }

                timeslotId = rs.getInt("timeslot_id");
            }
            catch (SQLException | IllegalStateException e)
            {
                c.rollback();
                throw e;
            }

            try (var ps = c.prepareStatement("UPDATE appointments SET status = 'CANCELLED', cancel_reason = ? WHERE id = ?"))
            {
                ps.setString(1, Objects.requireNonNullElse(cancelReason, "Cancelled by staff"));
                ps.setInt(2, id);

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

    public boolean updateAppointment(Appointment appointment)
    {
        try (var c = getConnection())
        {
            var ps = c.prepareStatement("UPDATE appointments SET patient_id = ?, timeslot_id = ?, status = ?, booked_at = ?, cancel_reason = ? WHERE id = ?");
            ps.setInt(1, appointment.getPatientId());
            ps.setInt(2, appointment.getTimeslotId());
            ps.setString(3, appointment.getStatus().name());
            ps.setObject(4, appointment.getBookedAt());
            ps.setString(5, appointment.getCancelReason());
            ps.setInt(6, appointment.getId());

            var affectedRows = ps.executeUpdate();
            return (affectedRows > 0);
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Appointment> getAppointments()
    {
        var appointments = new ArrayList<Appointment>();

        try (var c = getConnection())
        {
            var s = c.createStatement();
            var rs = s.executeQuery("""
                    SELECT appointments.* FROM appointments
                        LEFT JOIN timeslots ON appointments.timeslot_id = timeslots.id
                    ORDER BY slot_date DESC, start_time
                    """);

            while (rs.next()) { appointments.add(Appointment.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return appointments;
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

    public List<Appointment> getAppointmentsByClinic(Clinic clinic)
    {
        return getAppointmentsByClinic(clinic.getId());
    }

    public List<Appointment> getAppointmentsByClinic(int clinicId)
    {
        var appointments = new ArrayList<Appointment>();

        try (var c = getConnection())
        {
            var ps = c.prepareStatement(
                    """
                    SELECT appointments.* FROM appointments
                        LEFT JOIN timeslots ON appointments.timeslot_id = timeslots.id
                        LEFT JOIN clinic_services ON timeslots.clinic_service_id = clinic_services.id
                    WHERE clinic_id = ?
                    ORDER BY slot_date DESC, start_time
                    """);
            ps.setInt(1, clinicId);

            var rs = ps.executeQuery();
            while (rs.next()) { appointments.add(Appointment.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return appointments;
    }

    public List<Appointment> getAppointmentsByService(ClinicService clinicService)
    {
        return getAppointmentsByService(clinicService.getId());
    }

    public List<Appointment> getAppointmentsByService(int clinicServiceId)
    {
        var appointments = new ArrayList<Appointment>();

        try (var c = getConnection())
        {
            var ps = c.prepareStatement(
                    """
                    SELECT appointments.* FROM appointments
                        LEFT JOIN timeslots ON appointments.timeslot_id = timeslots.id
                    WHERE clinic_service_id = ?
                    ORDER BY slot_date DESC, start_time
                    """);
            ps.setInt(1, clinicServiceId);

            var rs = ps.executeQuery();
            while (rs.next()) { appointments.add(Appointment.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return appointments;
    }

    public List<Appointment> getAppointmentsByServiceAndDate(ClinicService clinicService, LocalDate date)
    {
        return getAppointmentsByServiceAndDate(clinicService.getId(), date);
    }

    public List<Appointment> getAppointmentsByServiceAndDate(int clinicServiceId, LocalDate date)
    {
        var appointments = new ArrayList<Appointment>();

        try (var c = getConnection())
        {
            var ps = c.prepareStatement(
                    """
                    SELECT appointments.* FROM appointments
                        LEFT JOIN timeslots ON appointments.timeslot_id = timeslots.id
                    WHERE clinic_service_id = ? AND slot_date = ?
                    ORDER BY slot_date DESC, start_time
                    """);
            ps.setInt(1, clinicServiceId);
            ps.setObject(2, date);

            var rs = ps.executeQuery();
            while (rs.next()) { appointments.add(Appointment.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return appointments;
    }

    public QueueTicket getQueueTicketById(int id)
    {
        try (var c = getConnection())
        {
            var ps = c.prepareStatement("SELECT * FROM queue_tickets WHERE id = ?");
            ps.setInt(1, id);

            var rs = ps.executeQuery();
            if (rs.next()) { return QueueTicket.from(rs); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public QueueTicket joinQueue(User patient, int clinicServiceId)
    {
        return joinQueue(patient.getId(), clinicServiceId);
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

    public void leaveQueueByPatient(int id, User patient)
    {
        leaveQueueByPatient(id, patient.getId());
    }

    public void leaveQueueByPatient(int id, int patientId)
    {
        try (var c = getConnection())
        {
            var ps = c.prepareStatement("UPDATE queue_tickets SET status = 'LEFT' WHERE id = ? AND patient_id = ?");
            ps.setInt(1, id);
            ps.setInt(2, patientId);

            var affectedRows = ps.executeUpdate();
            if (affectedRows == 0) { throw new IllegalStateException(String.format("Failed to update queue ticket #%s", id)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean updateQueueTicket(QueueTicket queueTicket)
    {
        try (var c = getConnection())
        {
            var ps = c.prepareStatement("UPDATE queue_tickets SET patient_id = ?, clinic_service_id = ?, queue_date = ?, queue_number = ?, status = ? WHERE id = ?");
            ps.setInt(1, queueTicket.getPatientId());
            ps.setInt(2, queueTicket.getClinicServiceId());
            ps.setObject(3, queueTicket.getQueueDate());
            ps.setInt(4, queueTicket.getQueueNumber());
            ps.setString(5, queueTicket.getStatus().name());
            ps.setInt(6, queueTicket.getId());

            var affectedRows = ps.executeUpdate();
            return (affectedRows > 0);
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

    public List<QueueTicket> getQueueTicketsByClinicAndDate(int clinicId, LocalDate date)
    {
        var queueTickets = new ArrayList<QueueTicket>();

        try (var c = getConnection())
        {
            var ps = c.prepareStatement(
                    """
                    SELECT queue_tickets.* FROM queue_tickets
                        LEFT JOIN clinic_services ON queue_tickets.clinic_service_id = clinic_services.id
                    WHERE clinic_id = ? AND queue_date = ?
                    """);
            ps.setInt(1, clinicId);
            ps.setObject(2, date);

            var rs = ps.executeQuery();
            while (rs.next()) { queueTickets.add(QueueTicket.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return queueTickets;
    }

    public boolean createAppointmentNotification(Appointment appointment)
    {
        try (var c = getConnection())
        {
            Timeslot timeslot =  getTimeslotById(appointment.getTimeslotId());

            var ps = c.prepareStatement("INSERT INTO notifications (user_id,type,title,message,related_appointment_id) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, appointment.getPatientId());
            if(appointment.getStatus() == AppointmentStatus.CONFIRMED){
                ps.setString(2, "APPOINTMENT");
                ps.setString(3, "Appointment submitted at " + appointment.getBookedAt().toString() + " confrimed!");
                ps.setString(4, "Please come on or before " + timeslot.getSlotDate() + " " + timeslot.getStartTime() + " ! We are happy to see you there!");
                ps.setInt(5,appointment.getId());
            }else if (appointment.getStatus() == AppointmentStatus.CANCELLED){
                ps.setString(2, "APPOINTMENT");
                ps.setString(3, "Appointment submitted at " + appointment.getBookedAt().toString() + " is cancelled!");
                ps.setString(4, "Your appointment is cancelled due to the following reason: " + appointment.getCancelReason() + " We are sorry for the inconvinence!");
                ps.setInt(5,appointment.getId());
            }else {return false;}

            var affectedRows = ps.executeUpdate();

            return (affectedRows > 0);
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean createQueueTicketNotification(QueueTicket queueTicket)
    {
        try (var c = getConnection())
        {
            Timeslot timeslot =  getTimeslotById(queueTicket.getPatientId());

            var ps = c.prepareStatement("INSERT INTO notifications (user_id,type,title,message,related_queue_ticket_id) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, queueTicket.getPatientId());
            if(queueTicket.getStatus() == QueueTicketStatus.SKIPPED){
                ps.setString(2, "QUEUE_TICKET");
                ps.setString(3, "Ticket submitted at " + queueTicket.getQueueDate().toString() + " was skipped!");
                ps.setString(4, "Your number was skipped ! We are sorry for the inconvienice!");
                ps.setInt(5,queueTicket.getId());
            }else if (queueTicket.getStatus() == QueueTicketStatus.CALLED){
                ps.setString(2, "QUEUE_TICKET");
                ps.setString(3, "Appointment submitted at " + queueTicket.getQueueDate().toString() + " is called!");
                ps.setString(4, "Your number "+ queueTicket.getQueueNumber() +" has been called ! Please get to the clinic as soon as possible.");
                ps.setInt(5,queueTicket.getId());
            }else {return false;}

            var affectedRows = ps.executeUpdate();
            return (affectedRows > 0);
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }


    public ArrayList<Notification> getNotificationsByID(int user_id) {

        var notifications = new ArrayList<Notification>();

        try (var c = getConnection())
        {
            var ps = c.prepareStatement(
                    """
                    SELECT * FROM notifications
                        
                    WHERE user_id = ? 
                    """);
            ps.setInt(1, user_id);

            var rs = ps.executeQuery();
            while (rs.next()) { notifications.add(0,Notification.from(rs)); }
        }
        catch (SQLException e) { throw new RuntimeException(e); }
        return notifications;

    }

    public boolean removeNotificationOnUpdate(Appointment appointment){

        try (var c = getConnection())
        {
            var ps = c.prepareStatement("DELETE FROM notifications WHERE related_appointment_id = ? ", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, appointment.getId());
            var affectedRows = ps.executeUpdate();
            return (affectedRows > 0);
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean removeNotificationOnUpdate(QueueTicket QueueTicket){
        try (var c = getConnection())
        {
            var ps = c.prepareStatement("DELETE FROM notifications WHERE related_queue_ticket_id = ? ", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, QueueTicket.getId());
            var affectedRows = ps.executeUpdate();
            return (affectedRows > 0);
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}
