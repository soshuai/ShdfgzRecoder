/**
 * 
 */
package cn.cherish.shdfgzrecoder.okhttp.utils;

/**
 * @author Veryxyf
 * 
 */
public class MyLocation {

    private double latitude  = 0.0d;
    private double longitude = 0.0d;

    public MyLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
