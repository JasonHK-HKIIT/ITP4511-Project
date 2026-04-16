package dev.jasonhk.hkiit.itp4511.clinicman.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Timeslot
{
    private int id;
    private int clinicServiceId;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private int capacity;
    private int bookedCount;

    public Timeslot(int id, int clinicServiceId, LocalDate slotDate, LocalTime startTime, LocalTime endTime, int capacity, int bookedCount)
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

    public LocalDate getSlotDate()
    {
        return slotDate;
    }

    public void setSlotDate(LocalDate slotDate)
    {
        this.slotDate = slotDate;
    }

    public LocalTime getStartTime()
    {
        return startTime;
    }

    public void setStartTime(LocalTime startTime)
    {
        this.startTime = startTime;
    }

    public LocalTime getEndTime()
    {
        return endTime;
    }

    public void setEndTime(LocalTime endTime)
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
                rs.getObject("slot_date", LocalDate.class),
                rs.getObject("start_time", LocalTime.class),
                rs.getObject("end_time", LocalTime.class),
                rs.getInt("capacity"),
                rs.getInt("booked_count"));
    }
}
