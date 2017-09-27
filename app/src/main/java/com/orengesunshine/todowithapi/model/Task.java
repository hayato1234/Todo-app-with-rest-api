package com.orengesunshine.todowithapi.model;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class Task {

    private int id;
    private String task;
    private int status;
    private String created_at;
    private Date created_at_date;

    public int getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public int getStatus() {
        return status;
    }

    public String  getCreated_at() {
        return created_at;
    }

    public String getCreated_at_date(){
        // time from server is UTC, so converting to local time of the device
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date;
        try {
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = format.parse(created_at);
            format.setTimeZone(TimeZone.getDefault());
            return format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
