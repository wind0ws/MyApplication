package android_km3serialport_api.gps;

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

    @Override
    public String toString() { return "latitude:"+String.format("%.8f",latitude)
            +" longitude:"+String.format("%.8f",longitude)+" altitude:"
            +String.format("%.8f",altitude);}
}
