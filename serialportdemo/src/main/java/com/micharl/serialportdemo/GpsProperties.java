package com.micharl.serialportdemo;

import com.micharl.serialportdemo.attributeevent.AttrChangedEvent;
import com.micharl.serialportdemo.attributeevent.IAttrChangedListener;
import com.micharl.serialportdemo.attributeevent.ListenerManager;

/**
 * GPS设备属性
 */
public class GpsProperties  {

    private ListenerManager listenerManager;

    public GpsProperties(GpsCoordinate baseGpsCoordinate) {
        this.baseGpsCoordinate=baseGpsCoordinate;
        listenerManager=new ListenerManager();
        init();
    }

    protected void init() {
        satelliteAmount=-1;
        gpsCoordinate=new GpsCoordinate(0,0,0);
        realTime="00:00:00";
        immediatelySpeed=-1;
        timeNumber=-1;
        positioningQuality=-1;
    }

    public static final int BASE_GPS_COORDINATE=51;
    public static final int SATELLITE_AMOUNT=51;
    public static final int GPS_COORDINATE=52;
    public static final int REAL_TIME=53;
    public static final int IMMEDIATELY_SPEED=54;
    public static final int POSITIONING_QUALITY=55;


    private GpsCoordinate baseGpsCoordinate;

    private int satelliteAmount;

    private GpsCoordinate gpsCoordinate;

    private String realTime;
    protected double timeNumber;

    private double immediatelySpeed;

    private int positioningQuality;


    public int getPositioningQuality() {
        return positioningQuality;
    }

    protected void setPositioningQuality(int positioningQuality) {
        if(fireAEvent(POSITIONING_QUALITY,this.positioningQuality,positioningQuality)){
        this.positioningQuality = positioningQuality;}
    }


    public double getImmediatelySpeed() {  return immediatelySpeed; }

    protected void setImmediatelySpeed(double immediatelySpeed) {
        if(fireAEvent(IMMEDIATELY_SPEED,this.immediatelySpeed,immediatelySpeed)){
            this.immediatelySpeed = immediatelySpeed;
        }
    }

    public GpsCoordinate getBaseGpsCoordinate() {
        return baseGpsCoordinate;
    }

    protected void setBaseGpsCoordinate(GpsCoordinate baseGpsCoordinate) {
        if(fireAEvent(BASE_GPS_COORDINATE,this.baseGpsCoordinate,baseGpsCoordinate)){
        this.baseGpsCoordinate = baseGpsCoordinate;}
    }

    public int getSatelliteAmount() {
        return satelliteAmount;
    }

    protected void setSatelliteAmount(int satelliteAmount) {
        if(fireAEvent(SATELLITE_AMOUNT,this.satelliteAmount,satelliteAmount)){
        this.satelliteAmount = satelliteAmount;}
    }

    public GpsCoordinate getGpsCoordinate() {
        return gpsCoordinate;
    }

    protected void setGpsCoordinate(GpsCoordinate gpsCoordinate) {
        if(fireAEvent(GPS_COORDINATE,this.gpsCoordinate,gpsCoordinate)){
        this.gpsCoordinate = gpsCoordinate;}
    }

    public String getRealTime() {
        return realTime;
    }

    protected void setRealTime(String realTime) {
        if(fireAEvent(REAL_TIME,this.realTime,realTime)){
        this.realTime = realTime;}
    }





    public void addListener(IAttrChangedListener listener){
        listenerManager.addListener(listener);
    }

    public void removeListener(IAttrChangedListener listener){
        listenerManager.removeListener(listener);
    }

    public boolean fireAEvent(int source,Object oldVal,Object newVal){
        if(!oldVal.equals(newVal)){
            listenerManager.fireAEvent(new AttrChangedEvent(source,oldVal,newVal));
            return true;
        }
        return false;
    }

}
