package com.michael.jiang.earthquake;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * EarthQuake封装类，封装了EarthQuake相关属性
 */
public class Quake {
    private Date date;
    private double magnitude;
    private Location location;
    private String details;
    private String link;
    public Date getDate() {
        return date;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public Location getLocation() {
        return location;
    }

    public String getDetails() {
        return details;
    }

    public String getLink() {
        return link;
    }

    public Quake(Date date,String details,double magnitude,Location location,String link){
        this.date=date;
        this.details=details;
        this.magnitude=magnitude;
        this.location=location;
        this.link=link;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm");
        String dateString = dateFormat.format(date);
        return dateString+"_"+magnitude+"_"+details;
    }
}
