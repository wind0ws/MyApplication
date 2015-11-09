package com.micharl.serialportdemo;


import android.util.Log;

import java.util.List;

/**
 * Gps数据分析器
 */
public class GpsDataParser {

    private static final String TAG="GPS解析";

    public GpsDataParser(GpsProperties gpsProperties) {
        this.gpsProperties=gpsProperties;
        xyzCalculate=new XYZCalculate(this.gpsProperties.getBaseGpsCoordinate());
        timeSpan=-1;
        lastCoordinate = new GpsCoordinate(0, 0, 0);
    }

    private XYZCalculate xyzCalculate;
    private GpsProperties gpsProperties;

    public GpsProperties getGpsProperties() {
        return gpsProperties;
    }

    protected double timeSpan;
    protected GpsCoordinate lastCoordinate;

    public void parseGpsProtocol(List<String> gpsDataList){
       if(!gpsDataList.get(0).equals("$GPGGA")) {
           Log.d(TAG, "Gps数据头不对。");
           return;
       }
        analyzeTime(gpsDataList.get(1));
        analyzeCoordinate(gpsDataList.get(2),gpsDataList.get(4),gpsDataList.get(9));
        analyzeSatelliteAmount(gpsDataList.get(7));
        analyzePositioningQuality(gpsDataList.get(6));
        calcSpeed();
    }


    public void analyzeTime(String timeSection) {
        double timeNumber= Double.parseDouble(timeSection);
        if(gpsProperties.timeNumber!=-1){
            timeSpan=timeNumber-gpsProperties.timeNumber;
        }
        gpsProperties.timeNumber = Double.parseDouble(timeSection);
        int utcHour = Integer.parseInt(timeSection.substring(0, 2)) + 8;
        String time = String.valueOf(utcHour);
        time = time + ":" + timeSection.substring(2, 4) + ":" + timeSection.substring(4, 6);
        gpsProperties.setRealTime(time);
    }

    public void analyzeCoordinate(String latitudeSection,String longitudeSection,String altitudeSection){
        if(lastCoordinate.longitude>0.1&&lastCoordinate.latitude>0.1){
            lastCoordinate=gpsProperties.getGpsCoordinate();
        }
        double latitude=Double.parseDouble(latitudeSection.substring(0,2))+Double.parseDouble(latitudeSection.substring(2))/60;
        double longitude=Double.parseDouble(longitudeSection.substring(0,3))+Double.parseDouble(longitudeSection.substring(3))/60;
        double altitude=Double.parseDouble(altitudeSection);
        GpsCoordinate gpsCoordinate = new GpsCoordinate(latitude, longitude, altitude);
        gpsProperties.setGpsCoordinate(gpsCoordinate);
    }

    public void analyzeSatelliteAmount(String satelliteSection) {
        int amount = -1;
        try {
            amount = Integer.parseInt(satelliteSection);
        }catch (NumberFormatException e) {
            Log.d(TAG, "解析GPS卫星数量出错");
        }
        gpsProperties.setSatelliteAmount(amount);
    }

    public void analyzePositioningQuality(String positioningQualitySection){
        int quality=-1;
        try {
            quality = Integer.parseInt(positioningQualitySection);
        }catch (NumberFormatException e) {
            Log.d(TAG, "解析GPS定位质量出错");
        }
        gpsProperties.setPositioningQuality(quality);
    }


    public void calcSpeed(){
        //时间间隔已经知道了就是timeSpan
        if(timeSpan==-1){
            return;
        }
        double distance = xyzCalculate.distanceOfTwoPointsInThreeDimensional(
                lastCoordinate, gpsProperties.getGpsCoordinate());
        gpsProperties.setImmediatelySpeed(xyzCalculate.speed(distance,timeSpan));
    }



}
