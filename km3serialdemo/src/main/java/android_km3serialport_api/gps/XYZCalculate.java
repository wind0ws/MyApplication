package android_km3serialport_api.gps;

/**
 * 坐标系计算
 */
public class XYZCalculate {

    public XYZCalculate(GpsCoordinate baseCoordinate){
        this.baseCoordinate=baseCoordinate;
    }
    private GpsCoordinate baseCoordinate;


    private static double distanceOfTwoPoints(double lng1, double lat1, double lng2,
                                              double lat2, EGaussSphere gs)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * (gs == EGaussSphere.WGS84 ? 6378137.0 : (gs == EGaussSphere.Xian80 ? 6378140.0 : 6378245.0));
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    public static EGaussSphere getGaussSphere()
    {
        return EGaussSphere.WGS84;
    }

    public double getX(GpsCoordinate gpsCoordinate) {
        double x= distanceOfTwoPoints(baseCoordinate.longitude, baseCoordinate.latitude,
                gpsCoordinate.longitude, baseCoordinate.latitude, getGaussSphere());
        if(gpsCoordinate.longitude<baseCoordinate.longitude){
            x=-x;
        }
        return x;
    }

    public double getY(GpsCoordinate gpsCoordinate) {
        double y= distanceOfTwoPoints(baseCoordinate.longitude, baseCoordinate.latitude,
                baseCoordinate.longitude, gpsCoordinate.latitude, getGaussSphere());
        if(gpsCoordinate.latitude<baseCoordinate.latitude){
            y=-y;
        }
        return y;
    }

    public double getZ(GpsCoordinate gpsCoordinate){
        return baseCoordinate.altitude-gpsCoordinate.altitude;
    }

    /**
     * 计算空间两点间距离
     * @param gpsCoordinate1 第一个坐标点
     * @param gpsCoordinate2 第二个坐标点
     * @return 返回两点间的空间距离
     */
    public double distanceOfTwoPointsInThreeDimensional(GpsCoordinate gpsCoordinate1,GpsCoordinate gpsCoordinate2) {
        double tmp = Math.pow(gpsCoordinate1.longitude - gpsCoordinate2.longitude, 2) +
                Math.pow(gpsCoordinate1.latitude - gpsCoordinate2.latitude, 2) +
                Math.pow(gpsCoordinate1.altitude - gpsCoordinate2.altitude, 2);
        return Math.sqrt(tmp);
    }

    /**
     * 计算速度
     * @param distance 距离 单位 米
     * @param time 时间 单位秒
     * @return 返回速度 km/h
     */
    public double speed(double distance,double time){
        return distance*3.6/time;
    }







}
