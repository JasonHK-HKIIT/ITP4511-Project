package dev.jasonhk.hkiit.itp4511.clinicman.bean;

public enum Service
{
    GENERAL_CONSULTATION(1, "General Consultation"),
    VACCINATION(2, "Vaccination"),
    BASIC_HEALTH_SCREENING(3, "Basic Health Screening");

    private final int id;
    private final String name;

    Service(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public static Service findById(int id)
    {
        for (Service service : Service.values())
        {
            if (service.id == id) { return service; }
        }

        throw new IllegalArgumentException(String.format("Service #%d does not exist.", id));
    }

    public static Service findByName(String name)
    {
        for (Service service : Service.values())
        {
            if (service.name.equalsIgnoreCase(name)) { return service; }
        }

        throw new IllegalArgumentException(String.format("Service \"%s\" does not exist.", name));
    }
}
