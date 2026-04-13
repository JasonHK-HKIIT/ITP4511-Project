package dev.jasonhk.hkiit.itp4511.clinicman.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class User
{
    private int id;
    private String username;
    private String fullName;
    private String phone;
    private Role role;
    private Gender gender;
    private Date dateOfBirth;

    public User(String username, String fullName, Role role)
    {
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    public User(int id, String username, String fullName, String phone, Role role, Gender gender, Date dateOfBirth)
    {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public Role getRole()
    {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }

    public Gender getGender()
    {
        return gender;
    }

    public void setGender(Gender gender)
    {
        this.gender = gender;
    }

    public Date getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public static User from(ResultSet rs) throws SQLException
    {
        var role = rs.getString("role");
        var gender = rs.getString("gender");

        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("full_name"),
                rs.getString("phone"),
                Role.valueOf(role),
                (gender != null) ? Gender.valueOf(gender) : null,
                rs.getDate("date_of_birth"));
    }
}
