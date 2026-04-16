package dev.jasonhk.hkiit.itp4511.clinicman.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Appointment
{
    private int id;
    private int patientId;
    private int timeslotId;
    private AppointmentStatus status;
    private LocalDateTime bookedAt;
    private String cancelReason;

    public Appointment(int id, int patientId, int timeslotId)
    {
        this.id = id;
        this.patientId = patientId;
        this.timeslotId = timeslotId;
    }

    public Appointment(int id, int patientId, int timeslotId, AppointmentStatus status, LocalDateTime bookedAt, String cancelReason)
    {
        this.id = id;
        this.patientId = patientId;
        this.timeslotId = timeslotId;
        this.status = status;
        this.bookedAt = bookedAt;
        this.cancelReason = cancelReason;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getPatientId()
    {
        return patientId;
    }

    public void setPatientId(int patientId)
    {
        this.patientId = patientId;
    }

    public int getTimeslotId()
    {
        return timeslotId;
    }

    public void setTimeslotId(int timeslotId)
    {
        this.timeslotId = timeslotId;
    }

    public AppointmentStatus getStatus()
    {
        return status;
    }

    public void setStatus(AppointmentStatus status)
    {
        this.status = status;
    }

    public LocalDateTime getBookedAt()
    {
        return bookedAt;
    }

    public void setBookedAt(LocalDateTime bookedAt)
    {
        this.bookedAt = bookedAt;
    }

    public String getCancelReason()
    {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason)
    {
        this.cancelReason = cancelReason;
    }

    public static Appointment from(ResultSet rs) throws SQLException
    {
        return new Appointment(
                rs.getInt("id"),
                rs.getInt("patient_id"),
                rs.getInt("timeslot_id"),
                AppointmentStatus.valueOf(rs.getString("status")),
                rs.getObject("booked_at", LocalDateTime.class),
                rs.getString("cancel_reason"));
    }
}
