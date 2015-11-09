package com.micharl.serialportdemo;

/**
 * Gps的一个坐标，包含经纬度和高度
 */
public class GpsCoordinate {
    public GpsCoordinate(double latitude,double longitude,double altitude){
        this.longitude=longitude;
        this.latitude=latitude;
        this.altitude=altitude;
    }

    /**
     * 纬度
     */
    public double latitude=0.0;
    /**
     * 经度
     */
    public double longitude=0.0;
    /**
     * 高度
     */
    public double altitude=0.0;

}
