package dev.jasonhk.hkiit.itp4511.clinicman.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;

public class Timeslot
{
    private int id;
    private int clinicServiceId;
    private Date slotDate;
    private Time startTime;
    private Time endTime;
    private int capacity;
    private int bookedCount;

    public Timeslot(int clinicServiceId, Date slotDate, Time startTime, Time endTime, int capacity, int bookedCount)
    {
        this.clinicServiceId = clinicServiceId;
        this.slotDate = slotDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.bookedCount = bookedCount;
    }

    public Timeslot(int id, int clinicServiceId, Date slotDate, Time startTime, Time endTime, int capacity, int bookedCount)
    {
        this.id = id;
        this.clinicServiceId = clinicServiceId;
        this.slotDate = slotDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.bookedCount = bookedCount;
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

    public Date getSlotDate()
    {
        return slotDate;
    }

    public void setSlotDate(Date slotDate)
    {
        this.slotDate = slotDate;
    }

    public Time getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Time startTime)
    {
        this.startTime = startTime;
    }

    public Time getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Time endTime)
    {
        this.endTime = endTime;
    }

    public int getCapacity()
    {
        return capacity;
    }

    public void setCapacity(int capacity)
    {
        this.capacity = capacity;
    }

    public int getBookedCount()
    {
        return bookedCount;
    }

    public void setBookedCount(int bookedCount)
    {
        this.bookedCount = bookedCount;
    }

    public static Timeslot from(ResultSet rs) throws SQLException
    {
        return new Timeslot(
                rs.getInt("id"),
                rs.getInt("clinic_service_id"),
                rs.getDate("slot_date"),
                rs.getTime("start_time"),
                rs.getTime("end_time"),
                rs.getInt("capacity"),
                rs.getInt("booked_count"));
    }
}
