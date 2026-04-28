package dev.jasonhk.hkiit.itp4511.clinicman.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class QueueTicket
{
    private int id;
    private int patientId;
    private int clinicServiceId;
    private LocalDate queueDate;
    private int queueNumber;
    private QueueTicketStatus status;

    public QueueTicket(int id, int patientId, int clinicServiceId, LocalDate queueDate, int queueNumber, QueueTicketStatus status)
    {
        this.id = id;
        this.patientId = patientId;
        this.clinicServiceId = clinicServiceId;
        this.queueDate = queueDate;
        this.queueNumber = queueNumber;
        this.status = status;
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

    public int getClinicServiceId()
    {
        return clinicServiceId;
    }

    public void setClinicServiceId(int clinicServiceId)
    {
        this.clinicServiceId = clinicServiceId;
    }

    public LocalDate getQueueDate()
    {
        return queueDate;
    }

    public void setQueueDate(LocalDate queueDate)
    {
        this.queueDate = queueDate;
    }

    public int getQueueNumber()
    {
        return queueNumber;
    }

    public void setQueueNumber(int queueNumber)
    {
        this.queueNumber = queueNumber;
    }

    public QueueTicketStatus getStatus()
    {
        return status;
    }

    public void setStatus(QueueTicketStatus status)
    {
        this.status = status;
    }

    public String formatTicketNumber()
    {
        return String.format("%d%03d", clinicServiceId, queueNumber);
    }

    public static QueueTicket from(ResultSet rs) throws SQLException
    {
        return new QueueTicket(
                rs.getInt("id"),
                rs.getInt("patient_id"),
                rs.getInt("clinic_service_id"),
                rs.getObject("queue_date", LocalDate.class),
                rs.getInt("queue_number"),
                QueueTicketStatus.valueOf(rs.getString("status")));
    }
}
