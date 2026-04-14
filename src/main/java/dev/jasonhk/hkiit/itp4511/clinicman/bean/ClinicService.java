package dev.jasonhk.hkiit.itp4511.clinicman.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClinicService
{
    private int id;
    private int clinicId;
    private int serviceId;
    private int slotCapacity;

    public ClinicService(int clinicId, int serviceId, int slotCapacity)
    {
        this.clinicId = clinicId;
        this.serviceId = serviceId;
        this.slotCapacity = slotCapacity;
    }

    public ClinicService(int id, int clinicId, int serviceId, int slotCapacity)
    {
        this.id = id;
        this.clinicId = clinicId;
        this.serviceId = serviceId;
        this.slotCapacity = slotCapacity;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getClinicId()
    {
        return clinicId;
    }

    public void setClinicId(int clinicId)
    {
        this.clinicId = clinicId;
    }

    public int getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(int serviceId)
    {
        this.serviceId = serviceId;
    }

    public int getSlotCapacity()
    {
        return slotCapacity;
    }

    public void setSlotCapacity(int slotCapacity)
    {
        this.slotCapacity = slotCapacity;
    }

    public static ClinicService from(ResultSet rs) throws SQLException
    {
        return new ClinicService(
                rs.getInt("id"),
                rs.getInt("clinic_id"),
                rs.getInt("service_id"),
                rs.getInt("slot_capacity"));
    }
}
