package com.michael.km3serialdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;

import java.io.IOException;

import android_km3serialport_api.attributeevent.AttrChangedEvent;
import android_km3serialport_api.attributeevent.IAttrChangedListener;
import android_km3serialport_api.gps.EGpsProtocolType;
import android_km3serialport_api.gps.GpsCoordinate;
import android_km3serialport_api.gps.GpsProperties;
import android_km3serialport_api.gps.GpsSerialPort;

/**
 * 测试GPS解析器
 */
public class GpsParserTestActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_gps_console);

        initControls();

        gpsProperties = new GpsProperties(new GpsCoordinate(31.86, 117.21,26.11));
        try {
            gpsSerialPort = new GpsSerialPort("/dev/ttymxc1", 115200,gpsProperties);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gpsProperties.addListener(new IAttrChangedListener() {
            @Override
            public void onAttrChanged(final AttrChangedEvent event) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editGpsSensor.append(event.toString());
                        editGpsSensor.append("\n");
                    }
                });
            }
        });

        //HandlerThread handlerThread = new HandlerThread("GenerateGpsData");
        threadHandler = new Handler();
        threadHandler.postDelayed(runnable, 200);
       // gpsSerialPort.threadAnalyze.start();
    }

    private void initControls() {
        editGpsSensor = (EditText) this.findViewById(R.id.EditTextGpsSensor);
        editGpsStatus = (EditText) this.findViewById(R.id.EditTextGpsStatus);
    }

    private EditText editGpsSensor;
    private EditText editGpsStatus;

    private GpsProperties gpsProperties;
    private GpsSerialPort gpsSerialPort;

    private Handler threadHandler;
    final String gpsData1 = "015658.40,3151.7448827,N";
    final String gpsData2 = "$GPGGA";
    final String gpsData3 = ",015658.40,3151.7448827,N,11712.4895777,E,";
    final String gpsData4 = "4,21,0.7,26.119,M,-3.308,M,1.4,0333*61";
    final String gpsData5 = "$GPGGA,015658.80,3151.7444355,N,11712.4890677,E,3,21,0.7,26.123,M,-3.308,M,0.8,0333*6F";
    int i=0;
    final Runnable runnable=new Runnable() {
        @Override
        public void run() {
            switch (i%5){
                case 0:
                   // gpsSerialPort.stringBuffer.append(gpsData1);
                    editGpsStatus.append(gpsData1);
                    break;
                case 1:
                   // gpsSerialPort.stringBuffer.append(gpsData2);
                    editGpsStatus.append(gpsData2);
                    break;
                case 2:
                   // gpsSerialPort.stringBuffer.append(gpsData3);
                    editGpsStatus.append(gpsData3);
                    break;
                case 3:
                   // gpsSerialPort.stringBuffer.append(gpsData4);
                    editGpsStatus.append(gpsData4);
                    break;
                case 4:
                   // gpsSerialPort.stringBuffer.append(gpsData5);
                    editGpsStatus.append(gpsData5);
                    break;
            }
            i++;
            if(i==1024){
                i=0;
            }
            threadHandler.postDelayed(runnable, 200);
        }
    };



}
