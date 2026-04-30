package dev.jasonhk.hkiit.itp4511.clinicman.bean;

import org.mariadb.jdbc.client.column.JsonColumn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;



public class StaffLog {
    private String table;
    private String old_row_data;
    private String new_row_data;
    private String dml_timestamp;
    private String username;
    private int staff_id_stamp;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getOld_row_data() {
        return old_row_data;
    }

    public void setOld_row_data(String old_row_data) {
        this.old_row_data = old_row_data;
    }

    public String getNew_row_data() {
        return new_row_data;
    }

    public void setNew_row_data(String new_row_data) {
        this.new_row_data = new_row_data;
    }

    public String getDml_timestamp() {
        return dml_timestamp;
    }

    public void setDml_timestamp(String dml_timestamp) {
        this.dml_timestamp = dml_timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStaff_id_stamp() {
        return staff_id_stamp;
    }

    public void setStaff_id_stamp(int staff_id_stamp) {
        this.staff_id_stamp = staff_id_stamp;
    }

    public StaffLog(String tbl_name, String old_row_data, String new_row_data, String dml_timestamp, String username, int staff_id_stamp){
        this.table = tbl_name;
        this.old_row_data = old_row_data;
        this.new_row_data = new_row_data;
        this.dml_timestamp = dml_timestamp;
        this.username = username;
        this.staff_id_stamp = staff_id_stamp;


    }

    public static StaffLog from(ResultSet rs)
            throws SQLException
    {
        return new StaffLog(
                rs.getString("tbl_name"),
                rs.getString("old_row_data"),
                rs.getString("new_row_data"),
                rs.getString("dml_timestamp"),
                rs.getString("username"),
                rs.getInt("staff_id_stamp"));
    }


}
