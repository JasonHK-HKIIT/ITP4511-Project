package dev.jasonhk.hkiit.itp4511.clinicman.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Notification {
    private int id;
    private int user_id;
    private String type;
    private String title;
    private String message;
    private int related_appoitnment_id;
    private int related_queue_ticket_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRelated_appoitnment_id() {
        return related_appoitnment_id;
    }

    public void setRelated_appoitnment_id(int related_appoitnment_id) {
        this.related_appoitnment_id = related_appoitnment_id;
    }

    public int getRelated_queue_ticket_id() {
        return related_queue_ticket_id;
    }

    public void setRelated_queue_ticket_id(int related_queue_ticket_id) {
        this.related_queue_ticket_id = related_queue_ticket_id;
    }

    public Notification(int user_id, String type, String title, String message, int related_id) {
        this.user_id = user_id;
        this.type = type;
        this.title = title;
        this.message = message;
        if (type.equals("APPOINTMENT"))
            this.related_appoitnment_id = related_appoitnment_id;
        else if (type.equals("QUEUE_TICKET"))
            this.related_queue_ticket_id = related_queue_ticket_id;
        else this.title = "warning,message wrong";
    }

    public static Notification from(ResultSet rs) throws SQLException
    {
        String type = "APPOINTMENT";
        int related_id = rs.getInt("related_appointment_id");
            if (rs.wasNull()) {
                type = "QUEUE_TICKET";
                related_id = rs.getInt("related_queue_ticket_id");
                if (rs.wasNull())
                {
                    related_id = -1;
                    type = "error retriving type";
                }
            }

        return new Notification(
                rs.getInt("user_id"),
                type,
                rs.getString("title"),
                rs.getString("message"),
                related_id);
        }

}
