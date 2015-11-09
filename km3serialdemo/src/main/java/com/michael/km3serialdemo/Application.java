package com.michael.km3serialdemo;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;


import android.content.SharedPreferences;

import com.michael.crash.CrashHandler;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

    public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    private SerialPort mSerialPort = null;

    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        SharedPreferences sp = getSharedPreferences("com.michael.km3serialdemo_preferences", MODE_PRIVATE);
        String display_device=sp.getString("DISPLAY_DEVICE","Car");
        if (display_device != null) {
            switch (display_device){
                default:
                case "Car":
                    return getCarSerialPort();
                case "Gps":
                    return getGpsSerialPort();
            }
        }
        return getCarSerialPort();
    }

    public String getShowMode(){
        SharedPreferences sp = getSharedPreferences("com.michael.km3serialdemo_preferences", MODE_PRIVATE);
        String display_device=sp.getString("DISPLAY_DEVICE","Car");
        if (display_device != null) {
            switch (display_device){
                default:
                case "Car":
                    return sp.getString("SHOW_MODE_CAR","hex");
                case "Gps":
                    return sp.getString("SHOW_MODE_GPS","string");
            }
        }
        return sp.getString("SHOW_MODE_CAR","hex");
    }

    private SerialPort getCarSerialPort() throws SecurityException, IOException, InvalidParameterException {
       // if (mSerialPort == null) {
			/* Read serial port parameters */
            SharedPreferences sp = getSharedPreferences("com.michael.km3serialdemo_preferences", MODE_PRIVATE);
            String path = sp.getString("DEVICE_CAR", "");
            int baudrate = Integer.decode(sp.getString("BAUDRATE_CAR", "-1"));

			/* Check parameters */
            if ((path==null)|| (path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }

			/* Open the serial port */
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
        //}
        return mSerialPort;
    }

    private SerialPort getGpsSerialPort() throws SecurityException, IOException, InvalidParameterException {
        //if (mSerialPort == null) {
			/* Read serial port parameters */
            SharedPreferences sp = getSharedPreferences("com.michael.km3serialdemo_preferences", MODE_PRIVATE);
            String path = sp.getString("DEVICE_GPS", "");
            int baudrate = Integer.decode(sp.getString("BAUDRATE_GPS", "-1"));

			/* Check parameters */
            if ( (path==null)||(path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }

			/* Open the serial port */
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
        //}
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }
}
