package dev.jasonhk.hkiit.itp4511.clinicman.bean;

public enum Clinic
{
    CHAI_WAN(1, "Chai Wan"),
    TSEUNG_KWAN_O(2, "Tseung Kwan O"),
    SHA_TIN(3, "Sha Tin"),
    TUEN_MUN(4, "Tuen Mun"),
    TSING_YI(5, "Tsing Yi");

    private final int id;
    private final String name;

    Clinic(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public static Clinic findById(int id)
    {
        for (Clinic clinic : Clinic.values())
        {
            if (clinic.id == id) { return clinic; }
        }

        throw new IllegalArgumentException(String.format("Clinic #%d does not exist.", id));
    }

    public static Clinic findByName(String name)
    {
        for (Clinic clinic : Clinic.values())
        {
            if (clinic.name.equalsIgnoreCase(name)) { return clinic; }
        }

        throw new IllegalArgumentException(String.format("Clinic \"%s\" does not exist.", name));
    }
}
