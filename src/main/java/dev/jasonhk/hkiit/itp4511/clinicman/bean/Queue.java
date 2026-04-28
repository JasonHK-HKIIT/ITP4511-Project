package dev.jasonhk.hkiit.itp4511.clinicman.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Queue
{
    private int id;
    private int clinicServiceId;
    private LocalDate queueDate;
    private int lastQueueNumber;

    public Queue(int id, int clinicServiceId, LocalDate queueDate, int lastQueueNumber)
    {
        this.id = id;
        this.clinicServiceId = clinicServiceId;
        this.queueDate = queueDate;
        this.lastQueueNumber = lastQueueNumber;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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

    public int getLastQueueNumber()
    {
        return lastQueueNumber;
    }

    public void setLastQueueNumber(int lastQueueNumber)
    {
        this.lastQueueNumber = lastQueueNumber;
    }

    public static Queue from(ResultSet rs)
            throws SQLException
    {
        return new Queue(
                rs.getInt("id"),
                rs.getInt("clinic_service_id"),
                rs.getObject("queue_date", LocalDate.class),
                rs.getInt("last_queue_number"));
    }
}
