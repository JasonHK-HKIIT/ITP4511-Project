package dev.jasonhk.hkiit.itp4511.clinicman.bean;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment
{
    private Clinic clinic;
    private Service service;
    private LocalDate date;
    private LocalTime time;
    private AppointmentStatus status = AppointmentStatus.APPOINTED;

    public Appointment(Clinic clinic, Service service, LocalDate date, LocalTime time)
    {
        this.clinic = clinic;
        this.service = service;
        this.date = date;
        this.time = time;
    }

    public Appointment(Clinic clinic, Service service, LocalDate date, LocalTime time, AppointmentStatus status)
    {
        this.clinic = clinic;
        this.service = service;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public Clinic getClinic()
    {
        return clinic;
    }

    public void setClinic(Clinic clinic)
    {
        this.clinic = clinic;
    }

    public Service getService()
    {
        return service;
    }

    public void setService(Service service)
    {
        this.service = service;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }

    public AppointmentStatus getStatus()
    {
        return status;
    }

    public void setStatus(AppointmentStatus status)
    {
        this.status = status;
    }
}
