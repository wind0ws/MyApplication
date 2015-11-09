package android_km3serialport_api.gps;


import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;


/**
 * Gps数据分析器
 */
public class GpsDataParser {

    private static final String TAG="GPS解析";

    public GpsDataParser( GpsProperties gpsProperties) {
        this.gpsProtocolType = EGpsProtocolType.GPS_PROTOCOL_GPGGA;
        generateGpsProtocolHead();
        this.calSpeedStyle = EGpsProtocolType.GPS_PROTOCOL_GPGGA;
        this.timeStyle = EGpsProtocolType.GPS_PROTOCOL_GPGGA;
        this.gpsProperties = gpsProperties;
        xyzCalculate = new XYZCalculate(this.gpsProperties.getBaseGpsCoordinate());
        timeSpan = -1;
        lastCoordinate = new GpsCoordinate(0, 0, 0);
    }

    private XYZCalculate xyzCalculate;
    private GpsProperties gpsProperties;
    private EGpsProtocolType gpsProtocolType;
    private String gpsProtocolHead;
    private EGpsProtocolType calSpeedStyle;
    private EGpsProtocolType timeStyle;

    public EGpsProtocolType getCalSpeedStyle() {
        return calSpeedStyle;
    }
    public void setCalSpeedStyle(EGpsProtocolType calSpeedStyle){
        if(calSpeedStyle.equals(EGpsProtocolType.GPS_PROTOCOL_ALL)) {
            throw new IllegalArgumentException("setCalSpeedStyle(EGpsProtocolType calSpeedStyle)中的calSpeedStyle不能设为All");
        }
        this.calSpeedStyle = calSpeedStyle;
    }

    public EGpsProtocolType getTimeStyle() {
        return timeStyle;
    }

    public void setTimeStyle(EGpsProtocolType timeStyle) {
        if(timeStyle.equals(EGpsProtocolType.GPS_PROTOCOL_ALL)) {
            throw new IllegalArgumentException("setTimeStyle(EGpsProtocolType timeStyle)中的timeStyle不能设为All");
        }
        this.timeStyle = timeStyle;
    }

    public EGpsProtocolType getGpsProtocolType() { return gpsProtocolType; }
    public void setGpsProtocolType(EGpsProtocolType gpsProtocolType) {
        this.gpsProtocolType = gpsProtocolType;
        generateGpsProtocolHead();
    }

    public GpsProperties getGpsProperties() {
        return gpsProperties;
    }

    protected double timeSpan;
    protected GpsCoordinate lastCoordinate;

    private void generateGpsProtocolHead(){
        switch (gpsProtocolType){
            default:
            case GPS_PROTOCOL_GPGGA:
                gpsProtocolHead = "$GPGGA";
                break;
            case GPS_PROTOCOL_GPRMC:
                gpsProtocolHead = "$GPRMC";
                break;
            case GPS_PROTOCOL_ALL:
                gpsProtocolHead="All";
        }
    }



    public void parseGpsProtocol(String singleGpsData){
       String[] gpsSplitData=  singleGpsData.split(",");
       if(gpsSplitData.length<13){
           Log.d(TAG, "Gps数据位数小余13。丢弃");
           return;
       }
        String gpsFrameHead = gpsSplitData[0];
        if(gpsProtocolType.equals(EGpsProtocolType.GPS_PROTOCOL_ALL)){
            switch (gpsFrameHead){
                case "$GPGGA":
                    ggaAnalyze(gpsSplitData);
                    break;
                case "$GPRMC":
                   rmcAnalyze(gpsSplitData);
                    break;
            }
        }else {
           if(!gpsFrameHead.equals(gpsProtocolHead)) {
               // Log.d(TAG, "Gps数据头不对或位数不对。丢弃");
                return;
            }
          switch (gpsProtocolType){
                default:
                case GPS_PROTOCOL_GPGGA:
                  ggaAnalyze(gpsSplitData);
                   break;
               case GPS_PROTOCOL_GPRMC:
                   rmcAnalyze(gpsSplitData);
                   break;
             }
        }
    }

    private void ggaAnalyze(String[] gpsSplitData){
        ggaAnalyzeTime(gpsSplitData[1]);
        ggaAnalyzeCoordinate(gpsSplitData[2], gpsSplitData[4], gpsSplitData[9]);
        ggaAnalyzeSatelliteAmount(gpsSplitData[7]);
        ggaAnalyzePositioningQuality(gpsSplitData[6]);
        ggaCalcSpeed();
    }

    private void rmcAnalyze(String[] gpsSplitData){
        rmcAnalyzeTime(gpsSplitData[1],gpsSplitData[9]);
        rmcAnalyzeCoordinate(gpsSplitData[3],gpsSplitData[5]);
        rmcAnalyzePositioningQuality(gpsSplitData[12]);
        rmcSpeed(gpsSplitData[7]);
        rmcAnalyzeAzimuth(gpsSplitData[8]);
        rmcAnalyzeMagneticDeclination(gpsSplitData[10]);
        rmcAnalyzeMagneticDeclinationDirection(gpsSplitData[11]);
    }



    private void ggaAnalyzeTime(String utcTimeSection) {
        if(utcTimeSection.isEmpty()){
            return;
        }
        setTimeSpan(utcTimeSection);
        if (timeStyle.equals(EGpsProtocolType.GPS_PROTOCOL_GPGGA)){
        gpsProperties.setRealTime(getLocalTime(utcTimeSection));}
    }

    private void rmcAnalyzeTime(String utcTimeSection,String utcDateSection){
        if(utcTimeSection.isEmpty()||utcDateSection.isEmpty()){
            return;
        }
        setTimeSpan(utcTimeSection);
        if(timeStyle.equals(EGpsProtocolType.GPS_PROTOCOL_GPRMC)){
            gpsProperties.setRealTime(getLocalDate(utcDateSection)+" "+getLocalTime(utcTimeSection));
        }
    }

    private void ggaAnalyzeCoordinate(String latitudeSection,String longitudeSection,String altitudeSection){
        if(latitudeSection.isEmpty()||longitudeSection.isEmpty()||altitudeSection.isEmpty()){
            return;
        }
        gpsProperties.setGpsCoordinate(getCurrentGpsCoordinate(latitudeSection,longitudeSection,altitudeSection));
    }

    private void rmcAnalyzeCoordinate(String latitudeSection,String longitudeSection){
        if(latitudeSection.isEmpty()||longitudeSection.isEmpty()){
            return;
        }
        if(!gpsProtocolType.equals(EGpsProtocolType.GPS_PROTOCOL_ALL)){ //GPS解析协议不为All的时候才解析坐标（因为为All的时候坐标由GPGGA协议解析）
            gpsProperties.setGpsCoordinate(getCurrentGpsCoordinate(latitudeSection,longitudeSection,"0"));
        }
    }

    private void ggaAnalyzeSatelliteAmount(String satelliteSection) {
        if(satelliteSection.isEmpty()){
            return;
        }
        int amount = -1;
        try {
            amount = Integer.parseInt(satelliteSection);
        }catch (NumberFormatException e) {
            Log.d(TAG, "解析GPS卫星数量出错");
        }
        gpsProperties.setSatelliteAmount(amount);
    }

    private void ggaAnalyzePositioningQuality(String positioningQualitySection){
        if(positioningQualitySection.isEmpty()){
            return;
        }
      /*  int quality=-1;
        try {
            quality = Integer.parseInt(positioningQualitySection);
        }catch (NumberFormatException e) {
            Log.d(TAG, "解析GPS定位质量出错");
        }*/
        gpsProperties.setPositioningQuality(positioningQualitySection);
    }

    private void rmcAnalyzePositioningQuality(String positioningQualitySection){
        if(!gpsProtocolType.equals(EGpsProtocolType.GPS_PROTOCOL_ALL)){
       ggaAnalyzePositioningQuality(positioningQualitySection);}
    }

    private void ggaCalcSpeed(){
        //时间间隔已经知道了就是timeSpan
        if(timeSpan==-1||lastCoordinate.longitude==0||lastCoordinate.latitude==0){
            return;
        }
        if(calSpeedStyle.equals(EGpsProtocolType.GPS_PROTOCOL_GPGGA)){
        double distance = xyzCalculate.distanceOfTwoPointsInThreeDimensional(
                lastCoordinate, gpsProperties.getGpsCoordinate());
        gpsProperties.setImmediatelySpeed(xyzCalculate.speed(distance,timeSpan));}
    }

    private void rmcSpeed(String speedSection){
        if(speedSection.isEmpty()){
            return;
        }
        if(calSpeedStyle.equals(EGpsProtocolType.GPS_PROTOCOL_GPRMC)){
        double knotsSpeed = Double.parseDouble(speedSection);
        gpsProperties.setImmediatelySpeed(knotsSpeed*1.852);}
    }



    private void setTimeSpan(String utcTimeSection){
        double utcTime = Double.parseDouble(utcTimeSection);
        timeSpan=-1;
        if(gpsProperties.utcTime!=-1){
            timeSpan=utcTime-gpsProperties.utcTime;
        }
        gpsProperties.utcTime = utcTime;
    }


    private String getLocalTime(String utcTimeSection){
        int utcHour = Integer.parseInt(utcTimeSection.substring(0, 2)) + 8;
        String time = String.valueOf(utcHour);
        time = time + ":" + utcTimeSection.substring(2, 4) + ":" + utcTimeSection.substring(4, 6)+ utcTimeSection.substring(6);
        return time;
    }

    private String getLocalDate(String utcDateSection) {
        int day=Integer.parseInt(utcDateSection.substring(0, 2));
        int month=Integer.parseInt(utcDateSection.substring(2,4));
        int year=Integer.parseInt(utcDateSection.substring(4,6));

        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month-1, day);
        int utcHour=Integer.parseInt(String.valueOf(gpsProperties.utcTime).substring(0, 2));
        if(utcHour>=16){
            gregorianCalendar.add(GregorianCalendar.DAY_OF_YEAR, 1);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(gregorianCalendar.getTime());
    }

    private GpsCoordinate getCurrentGpsCoordinate(String latitudeSection,String longitudeSection,String altitudeSection){
        lastCoordinate=gpsProperties.getGpsCoordinate();
        double latitude=Double.parseDouble(latitudeSection.substring(0,2))+Double.parseDouble(latitudeSection.substring(2))/60;
        double longitude=Double.parseDouble(longitudeSection.substring(0,3))+Double.parseDouble(longitudeSection.substring(3))/60;
        double altitude=Double.parseDouble(altitudeSection);
        return new GpsCoordinate(latitude, longitude, altitude);
    }

    private void rmcAnalyzeAzimuth(String azimuthSection){
        if(azimuthSection.isEmpty()){
            return;
        }
        double azimuth = Double.parseDouble(azimuthSection);
        gpsProperties.setAzimuth(azimuth);
    }

    private void rmcAnalyzeMagneticDeclination(String magneticDeclinationSection){
        if(magneticDeclinationSection.isEmpty()){
            return;
        }
        double magneticDeclination = Double.parseDouble(magneticDeclinationSection);
        gpsProperties.setMagneticDeclination(magneticDeclination);
    }

    private void rmcAnalyzeMagneticDeclinationDirection(String magneticDeclinationDirectionSection){
        if(magneticDeclinationDirectionSection.isEmpty()){
            return;
        }
        gpsProperties.setMagneticDeclinationDirection(magneticDeclinationDirectionSection);
    }




}
